package com.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import entity.Ball;
import entity.Brick;
import entity.Paddle;

import java.util.ArrayList;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    Ball ball;
    Paddle paddle;
    ArrayList<Brick> bricks;

    @Override
    public void create() {
        batch = new SpriteBatch();
        paddle = new Paddle(100, 100, 96, 16, batch, "paddle.png");
        ball = new Ball(0, 0, 20, 20, batch, "ball2.png");
        bricks = new ArrayList<>();
        bricks.add(new Brick(300, 300, 45, 21, batch, "white_brick.png"));
        bricks.add(new Brick(385, 300, 45, 21, batch, "green_brick.png"));
    }
    public void handleInput() {

    }
    public void update() {

    }

    public void draw() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();

        // Rendering
        ball.draw();
        paddle.draw();
        for (Brick brick : bricks) {
            brick.draw();
        }

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
        ball.dispose();
        paddle.dispose();
        for (Brick brick : bricks) {
            brick.dispose();
        }
    }
}
