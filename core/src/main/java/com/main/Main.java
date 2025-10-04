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
    boolean flowPaddle = true;      // Ball follow paddle

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
        paddle.handleInput();

        //New state of the ball
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.SPACE)) {
            flowPaddle = false;             // pulled ball up
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
            ball.setDestroyed(true);    // drop out of screen
        }
        // collision with paddle
        if (ball.checkCollision(paddle))
            ball.bounceOff(paddle);

        //collision with bricks
        for (Brick brick : bricksMap.getBricks()) {
            if (ball.checkCollision(brick)) {
                ball.bounceOff(brick);
                brick.takeHit();
                break;
            }
        }
    }

    public void update() {
        paddle.update();
        bricksMap.update();
        // Create and reset ball if no ball exists
        if (balls.isEmpty()) {
            balls.add(new Ball(paddle.getX() + (paddle.getWidth() / 2f) - 10,
                                paddle.getY() + paddle.getHeight(),
                                TextureManager.ballTexture, 3.0f));
            flowPaddle = true;
        }
        if (flowPaddle) {       // follow paddle
            balls.get(0).setX(paddle.getX() + (paddle.getWidth() / 2f) - 10);
            balls.get(0).setY(paddle.getY() + paddle.getHeight());
        }
        for (Ball ball : balls) {
            ball.update();
            checkCollision(ball);
        }
        balls.removeIf(Ball::isDestroyed);
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
