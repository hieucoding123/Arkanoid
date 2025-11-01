package com.main.gamemode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.main.Game;
import com.main.components.CollisionManager;
import com.main.components.IngameInputHandler;
import entity.Effect.EffectFactory;
import entity.Effect.EffectItem;
import entity.Effect.ShieldEffect;
import entity.GameScreen;
import entity.Player;
import entity.ScoreManager;
import entity.TextureManager;
import entity.object.Ball;
import entity.object.Paddle;
import entity.object.brick.Brick;
import entity.object.brick.BricksMap;
import table.CoopDataHandler;

import java.util.ArrayList;

public class CoopMode extends GameMode {
    private final ArrayList<BricksMap> bricksMaps;
    private Paddle paddle1;
    private Paddle paddle2;
    private BricksMap currentMap;
    private ScoreManager scoreManager;
    GameScreen gameScreen;
    private EffectFactory effectFactory;
    private int levelNumber;
    private int mapIndex;
    private int lives;
    private double timePlayed;

    public CoopMode(Player player, ScoreManager scoreManager, int levelNumber) {
        super();
        bricksMaps = new ArrayList<>();

        this.setPlayer(player);
        this.setEnd(false);
        this.scoreManager = scoreManager;
        this.gameScreen = new GameScreen(this.scoreManager);
        this.effectFactory = new EffectFactory();
        this.levelNumber = levelNumber;
        this.timePlayed = 0.0f;
        this.lives = 3;
        create();
    }

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

        paddle1 = new Paddle(Game.SCREEN_WIDTH / 2f - 48, 100, TextureManager.paddleTexture);
        paddle2 = new Paddle(Game.SCREEN_WIDTH / 2f - 48, 20, TextureManager.paddleTexture);

        balls.add(new Ball(paddle1.getX() + paddle1.getWidth() / 2f - 12,
            paddle1.getY() + paddle1.getHeight(),
            TextureManager.ballTexture,
            5.0f)
        );

        this.inputHandler = new IngameInputHandler(this);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(gameScreen.getStage());
        multiplexer.addProcessor(this.inputHandler);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void update(float delta) {
        if (this.isEnd()) {
            return;
        }

        if (start) {
            this.timePlayed += delta;
        }

        currentMap.update(delta, this.scoreManager);
        paddle1.update(delta);
        paddle2.update(delta);
        EffectItem.updateEffectItems(paddle1, this.balls, currentMap, delta);
        EffectItem.updateEffectItems(paddle2, this.balls, currentMap, delta);

        if (balls.isEmpty()) {
            scoreManager.deduction();
            EffectItem.ClearAllEffect(paddle1, paddle2, balls);
            this.lives--;
            this.reset();
        }

        if (followPaddle) {
            balls.get(0).setX(paddle1.getX() + paddle1.getWidth() / 2f - balls.get(0).getWidth() / 2f);
            balls.get(0).setY(paddle1.getY() + paddle1.getHeight());
            balls.get(0).setAngle((float) Math.PI / 2f);
        }

        for (Ball ball : balls) {
            ball.update(delta);
        }

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

        for (Ball ball : balls) {
            CollisionManager.handleBallBoundaryCollision(ball);
            CollisionManager.handleBallPaddleCollision(ball, paddle1);
            CollisionManager.handleBallPaddleCollision(ball, paddle2);

            Brick hitBrick = CollisionManager.handleBallBrickHit(ball, currentMap);

            if (hitBrick != null && hitBrick.gethitPoints() == 0) {
                EffectItem newEffectItem = null;
                if (mapIndex == 1) {
                    newEffectItem = effectFactory.tryCreateEffectItem(hitBrick, paddle1, ball,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 1);
                } else if  (mapIndex == 2) {
                    newEffectItem = effectFactory.tryCreateEffectItem(hitBrick, paddle1, ball,
                        0.04, 0.05, 0.05, 0.05, 0.07, 0.10, 0.12, 0.14, 0.16, 0.18);
                } else if (mapIndex == 3) {
                    newEffectItem = effectFactory.tryCreateEffectItem(hitBrick, paddle1, ball,
                        0.03, 0.05, 0.05, 0.05, 0.06, 0.08, 0.10, 0.12, 0.14, 0.15);
                } else if (mapIndex == 4) {
                    newEffectItem = effectFactory.tryCreateEffectItem(hitBrick, paddle1, ball,
                        0.03, 0.05, 0.05, 0.05, 0.05, 0.07, 0.08, 0.09, 0.10, 0.12);
                } else {
                    newEffectItem = effectFactory.tryCreateEffectItem(hitBrick, paddle1, ball,
                        0.02, 0.05, 0.05, 0.05, 0.04, 0.05, 0.06, 0.07, 0.08, 0.10);
                }

                if (newEffectItem != null) {
                    EffectItem.addEffectItem(newEffectItem);
                }

                scoreManager.comboScore(hitBrick);
                if (hitBrick.getExplosion()) {
                    hitBrick.startExplosion();
                } else {
                    hitBrick.setDestroyed(true);
                }
            }
        }

        balls.removeIf(Ball::isDestroyed);

        if (((currentMap.getBricks().isEmpty() || currentMap.getNumberBreakBrick() == 0) && !this.isEnd()) || (this.lives == 0)) {
            this.setEnd(true);
            double levelscore = this.scoreManager.getScore();
            double bonusscore = (300.0 - (double)this.timePlayed) * (levelscore / 300.0);

            double total_score = levelscore + bonusscore;
            if (total_score < 0) total_score = 0;

            if (currentMap.getBricks().isEmpty() && !this.isEnd())
                CoopDataHandler.updatePlayerScore(this.getPlayer().getName(), this.levelNumber, (double)((int)(total_score)), true);
            else
                CoopDataHandler.updatePlayerScore(this.getPlayer().getName(), this.levelNumber, (double)((int)(total_score)), false);
        }
    }

    @Override
    public void render(SpriteBatch sp, float delta) {
        this.draw(sp);
        gameScreen.setLives(this.lives);
        gameScreen.setTime(this.timePlayed);
        gameScreen.render();
    }

    @Override
    public void handleInput() {
        inputHandler.processMovement();
    }

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
        paddle1.draw(sp);
        paddle2.draw(sp);
    }

    public void reset() {
        balls.clear();
        balls.add(new Ball(paddle1.getX() + (paddle1.getWidth() / 2f) - 12,
            paddle1.getY() + paddle1.getHeight(),
            TextureManager.ballTexture, 5.0f));
        paddle1.setX(Game.SCREEN_WIDTH / 2f - paddle1.getWidth() / 2f);
        paddle1.setY(100);
        paddle2.setX(Game.SCREEN_WIDTH / 2f - paddle1.getWidth() / 2f);
        paddle2.setY(40);
        followPaddle = true;
    }

    @Override
    public Paddle getPaddle1() {
        return this.paddle1;
    }

    @Override
    public Paddle getPaddle2() {
        return this.paddle2;
    }

    public Object getLevelNumber() {
        return this.levelNumber;
    }

    public int getLives() {
        return this.lives;
    }

    @Override
    public void setLives(int lives) {
        this.lives = lives;
    }
    @Override
    public void setTimePlayed(double time) {
        this.timePlayed = time;
    }

    @Override
    public double getTimePlayed() {
        return this.timePlayed;
    }

    @Override
    public void resize(int width, int height) {
        if (gameScreen != null) {
            gameScreen.resize(width, height);
        }
    }
}
