package com.main.gamemode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.main.Game;
import com.main.components.CollisionManager;
import com.main.components.IngameInputHandler;
import entity.Effect.*;
import entity.GameScreen;
import entity.Player;
import entity.ScoreManager;
import entity.object.Ball;
import entity.object.brick.Brick;
import entity.object.brick.BricksMap;
import entity.object.Paddle;
import entity.TextureManager;
import table.LevelDataHandler;

import java.util.ArrayList;

/**
 * Represents the single-level gameplay mode where the player clears a predefined brick map.
 */
public class LevelMode extends GameMode {
    private final ArrayList<BricksMap> bricksMaps;
    private Paddle paddle;
    private BricksMap currentMap;
    private ScoreManager scoreManager;
    private GameScreen gameScreen;
    private EffectFactory effectFactory;
    private int levelNumber;
    private int mapIndex;
    private int lives;
    private double timePlayed;

    /**
     * Creates a new LevelMode instance.
     *
     * @param player the current player
     * @param scoreManager the score manager for tracking points
     * @param levelNumber the level index to load
     */
    public LevelMode(Player player, ScoreManager scoreManager, int levelNumber) {
        super();
        bricksMaps = new ArrayList<>();
        this.setPlayer(player);
        this.scoreManager = scoreManager;
        this.gameScreen = new GameScreen(this.scoreManager);
        this.effectFactory = new EffectFactory();
        this.levelNumber = levelNumber;
        this.lives = 3;
        this.setEnd(false);
        this.timePlayed = 0.0f;
        create();
    }

    /** Initializes maps, paddle, and input for this mode. */
    @Override
    public void create() {
        gameScreen.create();

        for (int i = 1; i <= 5; i++) {
            String mapPath = "/maps_for_levelmode/map" + i + ".txt";
            bricksMaps.add(new BricksMap(mapPath));
        }
        mapIndex = this.levelNumber;

        if (mapIndex <= 0 || mapIndex > bricksMaps.size()) {
            mapIndex = 1;
        }
        currentMap = bricksMaps.get(mapIndex - 1);
        paddle = new Paddle(Game.SCREEN_WIDTH / 2f - 48, 50, TextureManager.paddleTexture);
        balls.add(new Ball(paddle.getX() + paddle.getWidth() / 2f - 12,
            paddle.getY() + paddle.getHeight(),
            TextureManager.ballTexture,
            5.0f)
        );

        this.inputHandler = new IngameInputHandler(this);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(gameScreen.getStage());
        multiplexer.addProcessor(this.inputHandler);
        Gdx.input.setInputProcessor(multiplexer);
    }

    /**
     * Updates ball physics, effects, brick collisions, and level completion.
     *
     * @param delta time elapsed since the last frame (in seconds)
     */
    @Override
    public void update(float delta) {
        if (this.isEnd()) return;
        if (start) this.timePlayed += delta;
        currentMap.update(delta, this.scoreManager);
        paddle.update(delta);
        EffectItem.updateEffectItems(paddle, this.balls, currentMap, delta);

        if (balls.isEmpty()) {
            scoreManager.deduction();
            this.reset();
            this.lives--;
        }

        if (followPaddle) {
            balls.get(0).setX(paddle.getX() + (paddle.getWidth() / 2f) - balls.get(0).getWidth() / 2f);
            balls.get(0).setY(paddle.getY() + paddle.getHeight());
            balls.get(0).setAngle((float)Math.PI / 2f);
        }

        for (Ball ball : balls) {
            ball.update(delta);
        }

        // Ball collision resolution
        final int SOLVER_ITERATIONS = 5;
        for (int k = 0; k < SOLVER_ITERATIONS; k++) {
            for (int i = 0; i < balls.size(); i++) {
                Ball ball1 = balls.get(i);
                for (int j = i + 1; j < balls.size(); j++) {
                    Ball ball2 = balls.get(j);
                    CollisionManager.handleBallBallCollision(ball1, ball2);
                }
            }
        }

        // Ball collisions with environment
        for (Ball ball : balls) {
            CollisionManager.handleBallBoundaryCollision(ball);
            CollisionManager.handleBallPaddleCollision(ball, paddle);
            Brick hitBrick = CollisionManager.handleBallBrickHit(ball, currentMap);

            if (hitBrick != null && hitBrick.gethitPoints() == 0) {
                EffectItem newEffectItem;
                // Item spawn rates vary by map index
                if (mapIndex == 1) {
                    newEffectItem = effectFactory.tryCreateEffectItem(hitBrick, paddle, ball,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 1);
                } else if  (mapIndex == 2) {
                    newEffectItem = effectFactory.tryCreateEffectItem(hitBrick, paddle, ball,
                        0.04, 0.05, 0.05, 0.05, 0.07, 0.10, 0.12, 0.14, 0.16, 0.18);
                } else if (mapIndex == 3) {
                    newEffectItem = effectFactory.tryCreateEffectItem(hitBrick, paddle, ball,
                        0.03, 0.05, 0.05, 0.05, 0.06, 0.08, 0.10, 0.12, 0.14, 0.15);
                } else if (mapIndex == 4) {
                    newEffectItem = effectFactory.tryCreateEffectItem(hitBrick, paddle, ball,
                        0.03, 0.05, 0.05, 0.05, 0.05, 0.07, 0.08, 0.09, 0.10, 0.12);
                } else {
                    newEffectItem = effectFactory.tryCreateEffectItem(hitBrick, paddle, ball,
                        0.02, 0.05, 0.05, 0.05, 0.04, 0.05, 0.06, 0.07, 0.08, 0.10);
                }

                if (newEffectItem != null) {
                    EffectItem.addEffectItem(newEffectItem);
                }

                scoreManager.comboScore(hitBrick);
                if (hitBrick.getExplosion()) hitBrick.startExplosion();
                else hitBrick.setDestroyed(true);
            }
        }

        balls.removeIf(Ball::isDestroyed);

        // Check win/loss conditions
        if (((currentMap.getBricks().isEmpty() || currentMap.getNumberBreakBrick() == 0) && !this.isEnd()) || (this.lives == 0)) {
            this.setEnd(true);
            double levelscore = this.scoreManager.getScore();
            double bonusscore = (300.0 - (double)this.timePlayed) * (levelscore / 300.0);
            double total_score = Math.max(0, levelscore + bonusscore);
            boolean cleared = currentMap.getBricks().isEmpty();
            LevelDataHandler.updatePlayerScore(this.getPlayer().getName(), this.levelNumber, (double)((int)(total_score)), cleared);
        }

        printActiveEffects(delta);
    }

    /**
     * Renders all entities and UI on the screen.
     *
     * @param sp the SpriteBatch used for drawing
     * @param delta time elapsed since the last frame (in seconds)
     */
    @Override
    public void render(SpriteBatch sp, float delta) {
        this.draw(sp);
        gameScreen.setLives(this.lives);
        gameScreen.setTime(this.timePlayed);
        gameScreen.render();
    }

    /** Handles player input through the input handler. */
    @Override
    public void handleInput() {
        inputHandler.processMovement();
    }

    /**
     * Draws background, bricks, paddle, balls, and effect visuals.
     *
     * @param sp the SpriteBatch to draw with
     */
    @Override
    public void draw(SpriteBatch sp) {
        sp.draw(TextureManager.bgTexture, 0, 0, 800, 1000);
        currentMap.draw(sp);
        EffectItem.drawEffectItems(sp);
        if (ShieldEffect.isShield()) {
            sp.draw(
                TextureManager.lineTexture, Game.padding_left_right,
                0,
                Game.SCREEN_WIDTH - 2 * Game.padding_left_right,
                5
            );
        }
        for (Ball ball : balls) ball.draw(sp);
        paddle.draw(sp);
    }

    /** Resets paddle and ball positions after a lost life. */
    public void reset() {
        balls.clear();
        balls.add(new Ball(paddle.getX() + (paddle.getWidth() / 2f) - 12,
            paddle.getY() + paddle.getHeight(),
            TextureManager.ballTexture, 5.0f));
        paddle.setX(Game.SCREEN_WIDTH / 2f - paddle.getWidth() / 2f);
        paddle.setY(50);
        followPaddle = true;
    }

    /**
     * @return the primary paddle used in this mode
     */
    @Override
    public Paddle getPaddle1() {
        return this.paddle;
    }

    /**
     * @return the level number associated with this mode
     */
    public Object getLevelNumber() {
        return  this.levelNumber;
    }

    /**
     * @return the total time played for this level
     */
    @Override
    public double getTimePlayed() {
        return this.timePlayed;
    }

    /**
     * @return remaining player lives
     */
    public int getLives() {
        return this.lives;
    }

    /**
     * Sets the remaining lives.
     *
     * @param lives number of lives to set
     */
    @Override
    public void setLives(int lives) {
        this.lives = lives;
    }

    /**
     * Sets the total time played.
     *
     * @param time total time in seconds
     */
    @Override
    public void setTimePlayed(double time) {
        this.timePlayed = time;
    }
}
