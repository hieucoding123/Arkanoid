package com.main.gamemode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.main.Game;
import com.main.components.CollisionManager;
import com.main.components.IngameInputHandler;
import entity.Effect.*;
import com.main.gamescreen.GameScreen;
import entity.Player;
import entity.ScoreManager;
import entity.object.Ball;
import entity.object.brick.Brick;
import entity.object.brick.BricksMap;
import entity.object.Paddle;
import entity.TextureManager;
import datahandler.LevelDataHandler;

import java.util.ArrayList;

public class LevelMode extends GameMode {
    private final ArrayList<BricksMap> bricksMaps;
    private Paddle paddle;
    private BricksMap currentMap;
    private final ScoreManager scoreManager;
    GameScreen gameScreen;
    private final EffectFactory effectFactory;
    private final int levelNumber;
    private int mapIndex;
    private int lives;
    private double timePlayed;

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

    @Override
    public void create() {
        gameScreen.create();

        for (int i = 3; i <= 5; i++) {
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

    @Override
    public void update(float delta) {
        if (this.isEnd()) return;
        if (start) this.timePlayed += delta;
        currentMap.update(delta, this.scoreManager);
        paddle.update(delta);
        EffectItem.updateEffectItems(paddle, this.balls, currentMap, delta);

        if (balls.isEmpty()) {
            scoreManager.deduction();
            EffectItem.ClearAllEffect(paddle, null, balls);
            this.reset();
            this.lives--;
        }

        if (followPaddle) {       // follow paddle
            balls.get(0).setX(paddle.getX() + (paddle.getWidth() / 2f) - balls.get(0).getWidth() / 2f);
            balls.get(0).setY(paddle.getY() + paddle.getHeight());
            balls.get(0).setAngle((float)Math.PI / 2f);
        }

        for (Ball ball : balls) {
            ball.update(delta);
        }

        // Ball separation if multiple collision detected
        final int SOLVER_ITERATIONS = 5;
        // Ball Collision
        for (int k = 0; k < SOLVER_ITERATIONS; k++) {
            for (int i = 0; i < balls.size(); i++) {
                Ball ball1 = balls.get(i);
                for (int j = i + 1; j < balls.size(); j++) {
                    Ball ball2 = balls.get(j);
                    CollisionManager.handleBallBallCollision(ball1, ball2);
                }
            }
        }

        // Env collision
        for (Ball ball : balls) {
            CollisionManager.handleBallBoundaryCollision(ball);
            CollisionManager.handleBallPaddleCollision(ball, paddle);
            Brick hitBrick = CollisionManager.handleBallBrickHit(ball, currentMap);

            if (hitBrick != null && hitBrick.gethitPoints() == 0) {
                EffectItem newEffectItem;
                if (mapIndex == 1) {
                    newEffectItem = effectFactory.tryCreateEffectItem(hitBrick, paddle, ball,
                        0.02, 0.03, 0.03, 0.03, 0.04, 0.05, 0.06, 0.07, 0.08, 0.09);
                } else if  (mapIndex == 2) {
                    newEffectItem = effectFactory.tryCreateEffectItem(hitBrick, paddle, ball,
                        0.03, 0.03, 0.03, 0.03, 0.04, 0.06, 0.08, 0.09, 0.10, 0.11);
                } else if (mapIndex == 3) {
                    newEffectItem = effectFactory.tryCreateEffectItem(hitBrick, paddle, ball,
                        0.03, 0.04, 0.04, 0.04, 0.05, 0.07, 0.09, 0.10, 0.12, 0.13);
                } else if (mapIndex == 4) {
                    newEffectItem = effectFactory.tryCreateEffectItem(hitBrick, paddle, ball,
                        0.03, 0.04, 0.04, 0.04, 0.06, 0.08, 0.10, 0.12, 0.13, 0.15);
                } else {
                    newEffectItem = effectFactory.tryCreateEffectItem(hitBrick, paddle, ball,
                        0.04, 0.05, 0.05, 0.05, 0.07, 0.09, 0.11, 0.13, 0.15, 0.17);
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
            double bonusscore = (300.0 - this.timePlayed) * (levelscore / 300.0);

            double total_score = levelscore + bonusscore;
            if (total_score < 0) total_score = 0;

            boolean playerWon = (currentMap.getBricks().isEmpty() || currentMap.getNumberBreakBrick() == 0) && this.lives > 0;

            this.isWin = playerWon;

            LevelDataHandler.updatePlayerScore(this.getPlayer().getName(), this.levelNumber, (int)(total_score), playerWon);
        }
//        printActiveEffects(delta);
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
        paddle.draw(sp);
    }

    public void reset() {
        balls.clear();
        balls.add(new Ball(paddle.getX() + (paddle.getWidth() / 2f) - 12,
            paddle.getY() + paddle.getHeight(),
            TextureManager.ballTexture, 5.0f));
        paddle.setX(Game.SCREEN_WIDTH / 2f - paddle.getWidth() / 2f);
        paddle.setY(50);
        followPaddle = true;
    }

    @Override
    public Paddle getPaddle1() {
        return this.paddle;
    }

    public Object getLevelNumber() {
        return  this.levelNumber;
    }

    @Override
    public double getTimePlayed() {
        return this.timePlayed;
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
