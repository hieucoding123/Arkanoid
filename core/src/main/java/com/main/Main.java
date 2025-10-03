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

    @Override
    public void create() {
        TextureManager.loadTextures();
        batch = new SpriteBatch();
        bgTex = new Texture("background.png");
        paddle = new Paddle(100, 100, TextureManager.paddleTexture);

        balls = new ArrayList<>();
        balls.add(new Ball(24, 6, TextureManager.ballTexture, 2));

        bricksMap = new BricksMap("/map1.txt");
        SCREEN_WIDTH = Gdx.graphics.getWidth(); //Bo sung kich thuoc man hinh
        SCREEN_HEIGHT = Gdx.graphics.getHeight();
    }
    public void handleInput() {

    }
    public void update() {
        bricksMap.update();
        EffectItem.updateEffectItems(paddle);
        balls.removeIf(Ball::isDestroyed);

        for (Ball ball : balls) {
            ball.update();
            for (Brick brick : bricksMap.bricks) {
                if (ball.checkCollision(brick)) {
                    if (Math.random() < 0.3) {      // create new effect with probability
                        EffectItem.addEffectItem(
                            new ThreeBallsEffect(
                                brick.getX(),
                                brick.getY(),
                                -1)
                        );
                    }
                    ball.bounceOff(brick);
                    brick.takeHit();
                    break;
                }
            }
        }
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
