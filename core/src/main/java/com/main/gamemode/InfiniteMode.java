package com.main.gamemode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.main.Game;
import entity.Effect.*;
import entity.GameScreen;
import entity.ScoreManager;
import entity.object.Ball;
import entity.object.brick.Brick;
import entity.object.brick.BricksMap;
import entity.object.Paddle;
import entity.TextureManager;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class InfiniteMode extends GameMode {
    private final ArrayList<BricksMap> bricksMaps;
    private final ArrayList<Ball> balls;
    boolean flowPaddle = true;      // Ball follow paddle
    private Paddle paddle;
    private BricksMap currentMap;
    private ScoreManager scoreManager;
    GameScreen gameScreen;
    private EffectFactory effectFactory;

    public InfiniteMode(ScoreManager scoreManager, GameScreen gameScreen) {
        super();
        balls = new ArrayList<>();
        bricksMaps = new ArrayList<>();
        this.scoreManager = scoreManager;
        this.gameScreen = gameScreen;
        this.effectFactory = new EffectFactory();
        create();
    }

    @Override
    public void create() {
        gameScreen.create();

        for (int i = 1; i <= 5; i++) {
            String mapPath = "/maps/map" + i + ".txt";
            bricksMaps.add(new BricksMap(mapPath));
        }
        currentMap =  bricksMaps.get(0);
        paddle = new Paddle(Game.SCREEN_WIDTH / 2f - 48, 50, TextureManager.paddleTexture);
        balls.add(new Ball(paddle.getX() + paddle.getWidth() / 2f - 12,
            paddle.getY() + paddle.getHeight(),
            TextureManager.ballTexture,
            10.0f)
        );
    }

    @Override
    public void update(float delta) {
        currentMap.update(delta, this.scoreManager);
        paddle.update(delta);
        EffectItem.updateEffectItems(paddle, this.balls, delta);

        if (balls.isEmpty()) {
            scoreManager.deduction();
            this.reset();
        }

        if (flowPaddle) {       // follow paddle
            balls.get(0).setX(paddle.getX() + (paddle.getWidth() / 2f) - balls.get(0).getWidth() / 2f);
            balls.get(0).setY(paddle.getY() + paddle.getHeight());
            balls.get(0).setAngle((float)Math.PI / 2f);
        }
        for (Ball ball : balls) {
            ball.update(delta);
            ball.collisionWith(paddle);

            for (Brick brick : currentMap.getBricks()) {
                if (ball.checkCollision(brick)) {
                    brick.takeHit();
                    if (ball.isBig()) brick.setHitPoints(0);
                    if (brick.gethitPoints() == 0) {

                        EffectItem newEffectItem = effectFactory.tryCreateEffectItem(brick, paddle, ball);
                        if (newEffectItem != null) {
                            EffectItem.addEffectItem(newEffectItem);
                        }
                        scoreManager.comboScore(brick);
                        if (brick.getExplosion()) {
                            brick.startExplosion();
                        } else {
                            brick.setDestroyed(true);
                        }
                    }
                    float ballCenterX = ball.getX() + ball.getWidth() / 2f;
                    float ballCenterY = ball.getY() + ball.getHeight() / 2f;
                    //Bottom and top collision
                    if (ballCenterX > brick.getX() && ballCenterX < brick.getX() + brick.getWidth()) {
                        ball.reverseY();
                    }
                    //Left and right collision
                    else if (ballCenterY > brick.getY() && ballCenterY < brick.getY() + brick.getHeight()) {
                        ball.reverseX();
                    }
                    //Corner collision
                    else {
                        ball.reverseY();
                        ball.reverseX();
                    }
                    break;
                }
            }
        }
        balls.removeIf(Ball::isDestroyed);
    }

    @Override
    public void render(SpriteBatch sp, float delta) {
        this.update(delta);
        this.handleInput();
        this.draw(sp);
        gameScreen.render();
    }

    @Override
    public void handleInput() {
        //Press LEFT
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.LEFT)) {
            paddle.moveLeft();
        }
        //Press RIGHT
        else if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.RIGHT)) {
            paddle.moveRight();
        }
        //IF NO PRESS KEEP IT STAND
        else {
            paddle.setVelocity(0, 0);
        }
        // New state of the ball
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.SPACE)) {
            flowPaddle = false;             // pulled ball up
            balls.get(0).updateVelocity();
        }
    }

    @Override
    public void draw(SpriteBatch sp) {
        sp.draw(TextureManager.bgTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
        flowPaddle = true;
    }


}
