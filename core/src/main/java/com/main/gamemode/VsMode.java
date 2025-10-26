package com.main.gamemode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Input.Keys;
import com.main.Game;
import entity.Effect.EffectFactory;
import entity.Effect.EffectItem;
import entity.GameScreen;
import entity.Player;
import entity.ScoreManager;
import entity.TextureManager;
import entity.object.Ball;
import entity.object.Paddle;
import entity.object.brick.Brick;
import entity.object.brick.BricksMap;

import java.util.ArrayList;

public class VsMode extends GameMode {
    private static final float ROUND_DURATION = 30.0f;
    private static final int MAX_ROUNDS = 3;
    //private static final double VS_POINT_VALUE = 100.0;

    private Player player1;
    private Player player2;
    private Paddle paddle1;
    private Paddle paddle2;
    private EffectFactory effectFactory;
    private GameScreen gameScreen;
    private ArrayList<BricksMap> brickMap;
    private BricksMap currentMap;
    private Ball ballP1;
    private Ball ballP2;
    private int currentRound;
    private boolean isPaused;
    private float roundTimer;
    private boolean flowPaddle1;
    private boolean flowPaddle2;
    private int roundsWonP1;
    private int roundsWonP2;
    private ScoreManager scoreManagerP1;
    private ScoreManager scoreManagerP2;

    public VsMode(Player p1, Player p2, GameScreen gameScreen,
                  ScoreManager scoreManagerP1, ScoreManager scoreManagerP2) {
        super();
        this.player1 = p1;
        this.player2 = p2;
        this.roundsWonP1 = 0;
        this.roundsWonP2 = 0;

        this.gameScreen = gameScreen;
        this.scoreManagerP1 = scoreManagerP1;
        this.scoreManagerP2 = scoreManagerP2;
        this.effectFactory  = new EffectFactory();
        brickMap = new ArrayList<>();
        create();
    }

    @Override
    public void create() {
        gameScreen.create();

        for (int i = 1; i <= 3; i++) {
            String mapPath = "/maps/map_vs" + i + ".txt";
            brickMap.add(new BricksMap(mapPath));
        }
//        currentMap = brickMap.get(0);
//        currentRound = 1;
        paddle1 = new Paddle(Game.SCREEN_WIDTH / 2f - 48, 50, TextureManager.paddleTexture, false);
        paddle2 = new Paddle(Game.SCREEN_WIDTH / 2f - 48, 550, TextureManager.flippedpaddleTexture, true);
//        balls.add(new Ball(paddle1.getX() + paddle1.getWidth() / 2f - 12,
//            paddle1.getY() + paddle1.getHeight(),
//            TextureManager.ballTexture,
//            10.0f)
//        );
//        balls.add(new Ball(paddle2.getX() + paddle2.getWidth() / 2f - 12,
//            paddle2.getY() + paddle2.getHeight(),
//            TextureManager.ballTexture,
//            10.0f)
//        );

        startRound(1);
    }

    public void startRound(int roundNum) {
        this.currentRound = roundNum;
        this.currentMap = brickMap.get(currentRound - 1);

        this.roundTimer = ROUND_DURATION;
        this.isPaused = true;

        this.scoreManagerP1.setScore(0.0d);
        this.scoreManagerP2.setScore(0.0d);

        resetBalls();
    }

    public void resetBalls() {
        balls.clear();

        flowPaddle1 = true;
        flowPaddle2 = true;

        ballP1 = new Ball(paddle1.getX() + paddle1.getWidth() / 2f - 12,
            paddle1.getY() + paddle1.getHeight(),
            TextureManager.ballTexture,
            10.0f);

        ballP2 = new Ball(paddle2.getX() + paddle2.getWidth() / 2f - 12,
            paddle2.getY() - ballP2.getHeight(),
            TextureManager.ballTexture,
            10.0f);
        ballP2.setVelocity(0, -10.0f);
    }

    private void gameOver() {
        isPaused = true;
    }

    public void endRound() {
        isPaused = true;
        if (scoreManagerP1.getScore() > scoreManagerP2.getScore()) {
            roundsWonP1++;
        } else if (scoreManagerP1.getScore() < scoreManagerP2.getScore()) {
            roundsWonP2++;
        }

        if (roundsWonP1 == 2 || roundsWonP2 == 2 || currentRound == MAX_ROUNDS) {
            gameOver();
        } else {
            startRound(currentRound + 1);
        }
    }
    @Override
    public void update(float delta) {
        handleInput();

        if (isPaused) {
            return;
        }

        roundTimer -= delta;
        if (roundTimer <= 0) {
            endRound();
            return;
        }

        paddle1.update(delta);
        paddle2.update(delta);

//        EffectItem.updateEffectItems(paddle1, this.balls, delta);
//        EffectItem.updateEffectItems(paddle2, this.balls, delta);

        if (flowPaddle1) {
            ballP1.setX(paddle1.getX() + (paddle1.getWidth() / 2f) - ballP1.getWidth() / 2f);
            ballP1.setY(paddle1.getY() + paddle1.getHeight());
            ballP1.setAngle((float)Math.PI / 2f);
        }

        if (flowPaddle2) {
            ballP2.setX(paddle2.getX() + (paddle2.getWidth() / 2f) - ballP2.getWidth() / 2f);
            ballP2.setY(paddle2.getY() - paddle2.getHeight());
            ballP2.setAngle(-(float)Math.PI / 2f);
        }

        for (Ball ball : balls) {
            ball.update(delta);


            ball.collisionWith(paddle1);
            ball.collisionWith(paddle2);

            if (ball.checkCollision(paddle1)) {
                ball.setLastHitBy(1);
            }
            if (ball.checkCollision(paddle2)) {
                ball.setLastHitBy(2);
            }

            for (Brick brick : currentMap.getBricks()) {
                if (ball.checkCollision(brick)) {
                    brick.takeHit();
                    if (ball.isBig()) {
                        brick.setHitPoints(0);
                    }
                    if (brick.gethitPoints() == 0) {
                        Paddle relevantPaddle = (ball.getLastHitBy() == 1) ? paddle1 : paddle2;
//                        EffectItem newEffectItem = effectFactory.tryCreateEffectItem(brick, relevantPaddle, ball);
//                        if (newEffectItem != null) {
//                            EffectItem.addEffectItem(newEffectItem);
//                        }

                        if (ball.getLastHitBy() == 1) {
                            scoreManagerP1.comboScore(brick);
                        } else {
                            scoreManagerP2.comboScore(brick);
                        }

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

        if (currentMap.getSize() == 0) {
            endRound();
        }
    }

    @Override
    public void render(SpriteBatch sp, float delta) {
        this.handleInput();
        this.update(delta);
        this.draw(sp);
    }

    @Override
    public void handleInput() {
        if (isPaused && Gdx.input.isKeyJustPressed(Keys.SPACE)) {
            isPaused = false;
            return;
        }

        //Press LEFT paddle1
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.A)) {
            paddle1.moveLeft();
        }

        //Press RIGHT paddle1
        else if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.D)) {
            paddle1.moveRight();
        }

        //IF NO PRESS KEEP IT STAND
        else {
            paddle1.setVelocity(0, 0);
        }

        //Press LEFT paddle 2
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.LEFT)) {
            paddle2.moveLeft();
        }
        //Press RIGHT paddle 2
        else if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.RIGHT)) {
            paddle2.moveRight();
        }
        //IF NO PRESS KEEP IT STAND
        else {
            paddle2.setVelocity(0, 0);
        }

        if (flowPaddle1 && Gdx.input.isKeyJustPressed(Keys.W)) {
            flowPaddle1 = false;
            ballP1.setVelocity(0, 10.0f);
            balls.add(ballP1);
        }

        if (flowPaddle2 && Gdx.input.isKeyJustPressed(Keys.UP)) {
            flowPaddle2 = false;
            balls.add(ballP2);
        }
    }

    @Override
    public void draw(SpriteBatch sp) {
        sp.draw(TextureManager.bgTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        currentMap.draw(sp);
//        EffectItem.drawEffectItems(sp);

        if (flowPaddle1) {
            ballP1.draw(sp);
        }

        if (flowPaddle2) {
            ballP2.draw(sp);
        }

        for (Ball ball : balls) {
            ball.draw(sp);
        }
        paddle1.draw(sp);
        paddle2.draw(sp);
    }

    @Override
    public Paddle getPaddle1() {
        return this.paddle1;
    }

    @Override
    public void launchBall() {
        // DO NOTHING.
    }
}
