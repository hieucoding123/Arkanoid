package com.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import entity.TextureManager;
import entity.Ball;
import entity.BricksMap;
import entity.Paddle;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    Ball ball;
    Paddle paddle;
    Texture bgTex;
    BricksMap bricksMap;

    @Override
    public void create() {
        TextureManager.loadTextures();
        batch = new SpriteBatch();
        bgTex = new Texture("background.png");
        paddle = new Paddle(100, 100, 96, 16, TextureManager.paddleTexture);
        ball = new Ball(24, 0, 20, 20, TextureManager.ballTexture, 0);
        bricksMap = new BricksMap("/map1.txt");
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
    }
    public void update() {
        //Apply the change of paddle velocity
        paddle.update();

        ball.update();
        bricksMap.update();
    }

    public void draw() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();

        // Rendering
        batch.draw(bgTex, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        ball.draw(batch);
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
