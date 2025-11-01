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

public class InfiniteMode extends GameMode {
    private final ArrayList<String> maps;
    private Paddle paddle;
    private BricksMap currentMap;
    private ScoreManager scoreManager;
    GameScreen gameScreen;
    private EffectFactory effectFactory;
    private int currentIdx;
    private float timePlayed;
    private int lives;
    private final double baseItemChance = 0.07;
    private final double minItemChance = 0.001;
    private final double decayPerLevel = 0.007;

    public InfiniteMode(Player player, ScoreManager scoreManager, GameScreen gameScreen) {
        super();
        maps = new ArrayList<>();
        this.setPlayer(player);
        this.setEnd(false);
        this.scoreManager = scoreManager;
        this.gameScreen = gameScreen;
        this.effectFactory = new EffectFactory();
        this.timePlayed = 0.0f;
        lives = 3;

        create();
    }

    @Override
    public void create() {
        gameScreen.create();

        for (int i = 1; i <= 5; i++) {
            String mapPath = "/maps/map" + i + ".txt";
            maps.add(mapPath);
        }
        currentIdx = 0;
        currentMap =  new BricksMap(maps.get(currentIdx));
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
        if (!followPaddle)
            this.timePlayed += delta;
        currentMap.update(delta, this.scoreManager);
        paddle.update(delta);
        EffectItem.updateEffectItems(paddle, this.balls, currentMap, delta);

        if (balls.isEmpty()) {
            scoreManager.deduction();
            this.reset();
            this.lives--;
            this.setEnd(lives == 0);
        }
        if (this.isEnd()) {
            EffectItem.clear();
            this.getPlayer().setScore(this.scoreManager.getScore());
            this.getPlayer().setTimePlayed(this.timePlayed);
            InfiDataHandler.addScore(
                getPlayer().getName(), getPlayer().getScore(), getPlayer().getTimePlayed()
            );
        }

        if (followPaddle) {       // follow paddle
            balls.get(0).setX(paddle.getX() + (paddle.getWidth() / 2f) - balls.get(0).getWidth() / 2f);
            balls.get(0).setY(paddle.getY() + paddle.getHeight());
            balls.get(0).setAngle((float)Math.PI / 2f);
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
            CollisionManager.handleBallPaddleCollision(ball, paddle);

            Brick hitBrick = CollisionManager.handleBallBrickHit(ball, currentMap);

            if (hitBrick != null && hitBrick.gethitPoints() == 0) {
                double chance = generateChance();
                EffectItem newEffectItem = effectFactory.tryCreateEffectItem(
                    hitBrick, paddle, ball, // Was 'brick', now 'hitBrick'
                    chance * 1,
                    chance * 4,
                    chance * 5,
                    chance * 2,
                    chance * 3,
                    chance * 6,
                    chance * 7,
                    chance * 8,
                    chance * 9,
                    chance * 10
                );
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
        if (currentMap.getBricks().isEmpty()) {
            lives = Math.min(lives + 1, 3);
            currentIdx = currentIdx < maps.size() - 1 ? currentIdx + 1 : currentIdx;
            currentMap =  new BricksMap(maps.get(currentIdx));
            EffectItem.clear();
            reset();
        }
    }

    @Override
    public void render(SpriteBatch sp, float delta) {
        this.draw(sp);
        gameScreen.setTime(this.timePlayed);
        gameScreen.setLives(this.lives);
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

    private double generateChance() {
        double chance = baseItemChance - decayPerLevel * currentIdx;

        return Math.max(chance, minItemChance);
    }

    public int getLives() {
        return this.lives;
    }

    @Override
    public double getTimePlayed() {
        return this.timePlayed;
    }

    @Override
    public void setLives(int lives) {
        this.lives = lives;
    }

    @Override
    public void setTimePlayed(double time) {
        this.timePlayed = (float)time;
    }
}
