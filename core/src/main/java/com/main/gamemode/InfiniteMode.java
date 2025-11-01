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
import table.InfiDataHandler;

import java.util.ArrayList;

/**
 * Represents the Infinite Mode of the game, where the player continues playing
 * through multiple maps until all lives are lost.
 * Each cleared map increases difficulty and slightly reduces power-up drop rates.
 */
public class InfiniteMode extends GameMode {
    private final ArrayList<String> maps;
    private Paddle paddle;
    private BricksMap currentMap;
    private final ScoreManager scoreManager;
    GameScreen gameScreen;
    private final EffectFactory effectFactory;
    private int currentIdx;
    private float timePlayed;
    private int lives;

    /**
     * Creates a new InfiniteMode instance.
     * @param player the {@link Player} playing this mode.
     * @param scoreManager the {@link ScoreManager} managing the player’s score.
     */
    public InfiniteMode(Player player, ScoreManager scoreManager) {
        super();
        maps = new ArrayList<>();
        this.setPlayer(player);
        this.setEnd(false);
        this.scoreManager = scoreManager;
        this.gameScreen = new GameScreen(this.scoreManager);
        this.effectFactory = new EffectFactory();
        this.timePlayed = 0.0f;
        lives = 3;
        create();
    }

    /**
     * Initializes the Infinite Mode by loading maps, creating the paddle and ball,
     * and setting up input handling.
     */
    @Override
    public void create() {
        gameScreen.create();

        for (int i = 1; i <= 5; i++) {
            String mapPath = "/maps/map" + i + ".txt";
            maps.add(mapPath);
        }

        currentIdx = 0;
        currentMap = new BricksMap(maps.get(currentIdx));
        paddle = new Paddle(Game.SCREEN_WIDTH / 2f - 48, 50, TextureManager.paddleTexture);

        balls.add(new Ball(
            paddle.getX() + paddle.getWidth() / 2f - 12,
            paddle.getY() + paddle.getHeight(),
            TextureManager.ballTexture,
            5.0f
        ));

        this.inputHandler = new IngameInputHandler(this);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(gameScreen.getStage());
        multiplexer.addProcessor(this.inputHandler);
        Gdx.input.setInputProcessor(multiplexer);
    }

    /**
     * Updates game logic, handles collisions, effects, and map progression.
     * @param delta time elapsed since the last frame (in seconds).
     */
    @Override
    public void update(float delta) {
        if (!followPaddle)
            this.timePlayed += delta;

        currentMap.update(delta, this.scoreManager);
        paddle.update(delta);
        EffectItem.updateEffectItems(paddle, this.balls, currentMap, delta);

        // Handle ball loss and game over
        if (balls.isEmpty()) {
            scoreManager.deduction();
            EffectItem.ClearAllEffect(paddle, null, balls);
            this.reset();
            this.lives--;
            this.setEnd(lives == 0);
        }

        if (this.isEnd()) {
            this.isWin = false;
            EffectItem.clear();
            this.getPlayer().setScore(this.scoreManager.getScore());
            this.getPlayer().setTimePlayed(this.timePlayed);
            InfiDataHandler.addScore(
                getPlayer().getName(), getPlayer().getScore(), getPlayer().getTimePlayed()
            );
        }

        // Keep ball attached to paddle before launch
        if (followPaddle) {
            balls.get(0).setX(paddle.getX() + (paddle.getWidth() / 2f) - balls.get(0).getWidth() / 2f);
            balls.get(0).setY(paddle.getY() + paddle.getHeight());
            balls.get(0).setAngle((float)Math.PI / 2f);
        }

        for (Ball ball : balls)
            ball.update(delta);

        // Resolve ball-ball collisions
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

        // Handle collisions with paddle and bricks
        for (Ball ball : balls) {
            CollisionManager.handleBallBoundaryCollision(ball);
            CollisionManager.handleBallPaddleCollision(ball, paddle);

            Brick hitBrick = CollisionManager.handleBallBrickHit(ball, currentMap);
            if (hitBrick != null && hitBrick.gethitPoints() == 0) {
                double chance = generateChance();
                EffectItem newEffectItem = effectFactory.tryCreateEffectItem(
                    hitBrick, paddle, ball,
                    chance * 1, chance * 4, chance * 5, chance * 2, chance * 3,
                    chance * 6, chance * 7, chance * 8, chance * 9, chance * 10
                );
                if (newEffectItem != null)
                    EffectItem.addEffectItem(newEffectItem);

                scoreManager.comboScore(hitBrick);
                if (hitBrick.getExplosion())
                    hitBrick.startExplosion();
                else
                    hitBrick.setDestroyed(true);
            }
        }

        balls.removeIf(Ball::isDestroyed);

        // Proceed to next map
        if (currentMap.getBricks().isEmpty()) {
            lives = Math.min(lives + 1, 3);
            currentIdx = currentIdx < maps.size() - 1 ? currentIdx + 1 : currentIdx;
            currentMap = new BricksMap(maps.get(currentIdx));
            EffectItem.clear();
            reset();
        }
    }

    /**
     * Renders all visuals including game screen info.
     * @param sp the SpriteBatch used for drawing.
     * @param delta time elapsed since the last frame (in seconds).
     */
    @Override
    public void render(SpriteBatch sp, float delta) {
        this.draw(sp);
        gameScreen.setTime(this.timePlayed);
        gameScreen.setLives(this.lives);
        gameScreen.render();
    }

    /**
     * Handles player inputs (movement, launch, etc.).
     */
    @Override
    public void handleInput() {
        inputHandler.processMovement();
    }

    /**
     * Draws the game background, map, effects, paddle, and balls.
     * @param sp the SpriteBatch used for drawing
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

    /**
     * Resets the game state after losing a life or clearing a map.
     */
    public void reset() {
        balls.clear();
        balls.add(new Ball(
            paddle.getX() + (paddle.getWidth() / 2f) - 12,
            paddle.getY() + paddle.getHeight(),
            TextureManager.ballTexture, 5.0f
        ));
        paddle.setX(Game.SCREEN_WIDTH / 2f - paddle.getWidth() / 2f);
        paddle.setY(50);
        followPaddle = true;
    }

    /**
     * Gets the main paddle used in this mode.
     * @return the paddle instance.
     */
    @Override
    public Paddle getPaddle1() {
        return this.paddle;
    }

    /**
     * Generates the item drop chance for the current level.
     * The chance decreases as the level index increases.
     * @return the generated chance value.
     */
    private double generateChance() {
        double baseItemChance = 0.07;
        double decayPerLevel = 0.007;
        double chance = baseItemChance - decayPerLevel * currentIdx;
        double minItemChance = 0.001;
        return Math.max(chance, minItemChance);
    }

    /**
     * Gets the number of remaining lives.
     * @return current number of lives.
     */
    public int getLives() {
        return this.lives;
    }

    /**
     * Gets the total time the player has played.
     * @return total time played in seconds.
     */
    @Override
    public double getTimePlayed() {
        return this.timePlayed;
    }

    /**
     * Sets the number of remaining lives.
     * @param lives number of lives.
     */
    @Override
    public void setLives(int lives) {
        this.lives = lives;
    }

    /**
     * Sets the total time played.
     * @param time total time played in seconds.
     */
    @Override
    public void setTimePlayed(double time) {
        this.timePlayed = (float)time;
    }

    public Object getLevelNumber() {
        return this.currentIdx;
    }

    @Override
    public void resize(int width, int height) {
        if (gameScreen != null) {
            gameScreen.resize(width, height);
        }
    }

    @Override
    public void dispose() {
        if (gameScreen != null) {
            gameScreen.dispose();
        }
    }
}
