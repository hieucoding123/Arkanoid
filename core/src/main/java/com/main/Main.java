package com.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import entity.*;

import java.util.ArrayList;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    private SpriteBatch batch;
    public static ArrayList<Ball> balls;
    Paddle paddle;
    Texture bgTex;
    BricksMap bricksMap;
    boolean ballstuck = true;       //Ball stuck the paddle or not, true is stuck

    @Override
    public void create() {
        TextureManager.loadTextures();
        batch = new SpriteBatch();
        bgTex = new Texture("background.png");
        paddle = new Paddle(100, 100, TextureManager.paddleTexture);

        balls = new ArrayList<>();
        balls.add(new Ball(paddle.getX() + paddle.getWidth() / 2f - 10,
                            paddle.getY() + paddle.getHeight(),
                            TextureManager.ballTexture,
                            3.0f));

        bricksMap = new BricksMap("/map1.txt");
        SCREEN_WIDTH = Gdx.graphics.getWidth(); //Bo sung kich thuoc man hinh
        SCREEN_HEIGHT = Gdx.graphics.getHeight();
    }
    public void handleInput() {
        float paddleSpeed = 5.0f;
        //Edit Velocity of paddle
        //Press LEFT
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.LEFT)) {
            if (paddle.getX() > 0) { // Check LEFT
                paddle.setVelocity(-paddleSpeed, 0);
            } else {
                paddle.setVelocity(0, 0);
            }
        }
        //Press RIGHT
        else if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.RIGHT)) {
            if (paddle.getX() < Gdx.graphics.getWidth() - paddle.getWidth()) { //Check RIGHT
                paddle.setVelocity(paddleSpeed, 0);
            } else {
                paddle.setVelocity(0, 0);
            }
        }
        //IF NO PRESS KEEP IT STAND
        else {
            paddle.setVelocity(0, 0);
        }

        //New state of the ball
        if (ballstuck && Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.SPACE)) {
            ballstuck = false;
            balls.get(0).updateVelocity();
        }
    }

    public void checkCollision(Ball ball) {
        //collision with the wall
        if (ball.getX() <= 0 || ball.getX() + ball.getWidth() >= Gdx.graphics.getWidth()) {
            ball.reverseX();
        }
        if (ball.getY() + ball.getHeight() >= Gdx.graphics.getHeight()) {
            ball.reverseY();
        }
        if (ball.getY() <= 0) {
            ballstuck = true;
            ball.reset();
        }

        //collision with paddle
        float ballY = ball.getY();
        float paddleY = paddle.getY() + paddle.getHeight();
        if (ballY - paddleY <= 3) {
            float paddleCenter = paddle.getX() + paddle.getWidth() / 2f;
            float ballCenter = ball.getX() + ball.getWidth() / 2f;
            float hitPosition = ballCenter - paddleCenter;

            float normalizedPosition = hitPosition / (paddle.getWidth() / 2f);
            float maxBounceAngle = (float) Math.PI / 3f;
            float newAngle = (float) Math.PI / 2f - (normalizedPosition * maxBounceAngle);

            ball.setAngle(newAngle);
        }
        //collision with bricks
        for (Brick brick : bricksMap.getBricks()) {
            if (ball.checkCollision(brick)) {
                brick.takeHit();
                ball.reverseY();
                break;
            }
        }

    }

    public void update() {
        paddle.update();

        if (ballstuck) {
            balls.get(0).setX(paddle.getX() + (paddle.getWidth() / 2f) - 10);
            balls.get(0).setY(paddle.getY() + paddle.getHeight());
        } else {
            balls.get(0).update();
        }

        bricksMap.update();
        checkCollision(balls.get(0));
    }

    public void draw() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();

        // Rendering
        batch.draw(bgTex, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        for (Ball ball : balls) {
            ball.draw(batch);
        }
        paddle.draw(batch);
        bricksMap.draw(batch);
        EffectItem.drawEffectItems(batch);

        batch.end();
    }
    @Override
    public void render() {
        handleInput();
        update();
        draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
        bgTex.dispose();
        TextureManager.dispose();
    }
}
