package com.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import entity.*;
import ui.UI;

import java.util.ArrayList;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    public static int padding_left_right;
    public static int padding_top;
    private SpriteBatch batch;
    public static ArrayList<Ball> balls;
    Paddle paddle;
    Texture bgTex;
    BricksMap bricksMap;
    boolean flowPaddle = true;      // Ball follow paddle
    boolean Press_M = false;

    private UI ui;
    public static GameState gameState;

    @Override
    public void create() {
        TextureManager.loadTextures();
        SCREEN_WIDTH = Gdx.graphics.getWidth(); //Add screen size
        SCREEN_HEIGHT = Gdx.graphics.getHeight();
        batch = new SpriteBatch();
        bgTex = new Texture("background.png");
        paddle = new Paddle(SCREEN_WIDTH / 2f - 48, 70, TextureManager.paddleTexture);

        balls = new ArrayList<>();
        balls.add(new Ball(paddle.getX() + paddle.getWidth() / 2f - 10,
                            paddle.getY() + paddle.getHeight(),
                            TextureManager.ballTexture,
                            2.0f));
        Level_game.loadLevels();
        bricksMap = Level_game.getCurrentLevel();
        padding_left_right = bricksMap.xBeginCoord;
        padding_top = bricksMap.yBeginCoord + bricksMap.brickH;

        ui = new UI(this);
        ui.create();

        gameState = GameState.MAIN_MENU;
    }

    public void handleInput() {
        //Press M to change map (it for test)
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.M)) {
            Press_M = true;
        }
        //Press LEFT
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.LEFT) || (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.A))) {
            paddle.moveLeft();
        }
        //Press RIGHT
        else if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.RIGHT) || (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.D))) {
            paddle.moveRight();
        }
        //IF NO PRESS KEEP IT STAND
        else {
            paddle.setVelocity(0, 0);
        }

        //New state of the ball
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.SPACE)) {
            flowPaddle = false;             // pulled ball up
            balls.get(0).updateVelocity();
        }
    }

    public void checkCollision(Ball ball) {
        //collision with the wall
        if (ball.getX() <= padding_left_right || ball.getX() + ball.getWidth() >= SCREEN_WIDTH - padding_left_right) {
            ball.reverseX();
        }
        if (ball.getY() + ball.getHeight() >= padding_top) {
            ball.reverseY();
        }
        if (ball.getY() <= 0) {
            ball.setDestroyed(true);    // drop out of screen
        }
        //collision with paddle
        if (ball.getdy() < 0 &&
            ball.getX() < paddle.getX() + paddle.getWidth() &&
            ball.getX() + ball.getWidth() > paddle.getX() &&
            ball.getY() <= paddle.getY() + paddle.getHeight() &&
            ball.getY() >= paddle.getY()) {
            float paddleCenter = paddle.getX() + paddle.getWidth() / 2f;
            float ballCenter = ball.getX() + ball.getWidth() / 2f;
            float hitPosition = ballCenter - paddleCenter;

            float normalizedPosition = hitPosition / (paddle.getWidth() / 2f);
            float maxBounceAngle = (float)Math.PI / 3f;
            float newAngle = (float)Math.PI / 2f - (normalizedPosition * maxBounceAngle);

            ball.setAngle(newAngle);
        }
        //collision with bricks
        for (Brick brick : bricksMap.getBricks()) {
            if (ball.checkCollision(brick)) {
                brick.takeHit();
//                if (Math.random() < 0.5) {
//                    EffectItem.addEffectItem(new ThreeBallsEffect(
//                        brick.getX(), brick.getY(), -1
//                    ));
//                }
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

    public void reset() {
        balls.clear();
        balls.add(new Ball(paddle.getX() + (paddle.getWidth() / 2f) - 10,
            paddle.getY() + paddle.getHeight(),
            TextureManager.ballTexture, 3.0f));
        paddle.setX(SCREEN_WIDTH / 2f - 48);
        paddle.setY(70);
        flowPaddle = true;
    }

    public void update() {
        paddle.update();
//        EffectItem.updateEffectItems(paddle);
        if (Press_M || bricksMap.getsize() == 0) {
            Level_game.nextLevel();
            bricksMap = Level_game.getCurrentLevel();
            reset();
            Press_M = false;
        }
        bricksMap.update();
        // Create and reset ball if no ball exists
        if (balls.isEmpty()) {
            reset();
        }
        if (flowPaddle) {       // follow paddle
            balls.get(0).setX(paddle.getX() + (paddle.getWidth() / 2f) - 10);
            balls.get(0).setY(paddle.getY() + paddle.getHeight());
            balls.get(0).setAngle((float)Math.PI / 2f);
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
        batch.draw(bgTex, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        for (Ball ball : balls) {
            ball.draw(batch);
        }
        paddle.draw(batch);
        bricksMap.draw(batch);
//        EffectItem.drawEffectItems(batch);
        batch.end();
    }
    @Override
    public void render() {
        switch (gameState){
            case MAIN_MENU:
                ui.render();
                break;
            case PLAYING:
                handleInput();
                update();
                draw();
                break;
        }
    }

    public void setGameState(GameState newGameState) {
        gameState = newGameState;
        if (gameState == GameState.PLAYING) {
            Gdx.input.setInputProcessor(null); // Set input processor to null for gameplay
        } else {
            Gdx.input.setInputProcessor(ui.getStage()); // Set input processor to the UI stage
        }
    }


    @Override
    public void dispose() {
        batch.dispose();
        bgTex.dispose();
        TextureManager.dispose();
        ui.dispose();
    }
}

