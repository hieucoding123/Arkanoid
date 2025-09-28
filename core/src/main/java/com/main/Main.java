package com.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import entity.Ball;
import entity.BricksMap;
import entity.Paddle;

import java.util.ArrayList;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    Ball ball;
    Paddle paddle;
    Texture bgTex;
    BricksMap bricksMap;

    @Override
    public void create() {
        batch = new SpriteBatch();
        bgTex = new Texture("background.png");
        paddle = new Paddle(100, 100, 96, 16, "paddle.png");
        ball = new Ball(24, 0, 20, 20, "ball2.png");
        bricksMap = new BricksMap("/map1.txt");
    }
    public void handleInput() {

    }
    public void update() {

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
        ball.dispose();
        paddle.dispose();
        bricksMap.dispose();
    }
}
