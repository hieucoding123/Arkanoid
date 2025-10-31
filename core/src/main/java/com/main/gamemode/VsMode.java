package com.main.gamemode;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    private static final float ROUND_DURATION = 60.0f;
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
    private ArrayList<Ball> balls;
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
    private boolean isGameEnded = false;

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
        balls = new ArrayList<>();
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
        paddle2 = new Paddle(Game.SCREEN_WIDTH / 2f - 48, 800, TextureManager.flippedpaddleTexture, true);
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

        paddle1.clearEffects();
        paddle2.clearEffects();

        paddle1.setX(Game.SCREEN_WIDTH / 2f - 48);
        paddle1.setY(50);

        paddle2.setX(Game.SCREEN_WIDTH / 2f - 48);
        paddle2.setY(800);

        EffectItem.clear();
        resetBalls();
    }

    public void resetBalls() {
        balls.clear();

        flowPaddle1 = true;
        flowPaddle2 = true;

        ballP1 = new Ball(paddle1.getX() + paddle1.getWidth() / 2f - 12,
            paddle1.getY() + paddle1.getHeight(),
            TextureManager.ballTexture,
            5.0f);
        ballP1.setVelocity(0, 5.0f);
        balls.add(ballP1);

        ballP2 = new Ball(paddle2.getX() + paddle2.getWidth() / 2f - 12,
            paddle2.getY() - ballP1.getHeight(),
            TextureManager.ballTexture,
            10.0f);
        ballP2.setVelocity(0, -10.0f);
        balls.add(ballP2);
    }

    private void gameOver() {
        isPaused = true;
        isGameEnded = true;
    }

    @Override
    public boolean isEnd() {
        return isGameEnded;
    }

    public void endRound(int winner) {
        isPaused = true;
        if (winner == 1) {
            roundsWonP1++;
        } else if (winner == 2) {
            roundsWonP2++;
        } else if (winner == 0) {
            if (scoreManagerP1.getScore() > scoreManagerP2.getScore()) {
                roundsWonP1++;
            } else if (scoreManagerP1.getScore() < scoreManagerP2.getScore()) {
                roundsWonP2++;
            }
        }

        System.out.println("p1: " + roundsWonP1);
        System.out.println("p2: " + roundsWonP2);

        if (roundsWonP1 == 2 || roundsWonP2 == 2 || currentRound == MAX_ROUNDS) {
            gameOver();
        } else {
            startRound(currentRound + 1);
        }
    }
    @Override
    public void update(float delta) {
//        handleInput();

        if (flowPaddle1) {
            ballP1.setX(paddle1.getX() + (paddle1.getWidth() / 2f) - ballP1.getWidth() / 2f);
            ballP1.setY(paddle1.getY() + paddle1.getHeight());
            ballP1.setAngle((float)Math.PI / 2f);
        }

        if (flowPaddle2) {
            ballP2.setX(paddle2.getX() + (paddle2.getWidth() / 2f) - ballP2.getWidth() / 2f);
            ballP2.setY(paddle2.getY() - ballP2.getHeight());
            ballP2.setAngle(-(float)Math.PI / 2f);
        }

        if (isPaused) {
            return;
        }

        if (!flowPaddle1 || !flowPaddle2) {
            roundTimer -= delta;
        }

        if (roundTimer <= 0) {
            endRound(0);
            return;
        }

        paddle1.update(delta);
        paddle2.update(delta);

//        EffectItem.updateEffectItems(paddle1, this.balls, delta);
//        EffectItem.updateEffectItems(paddle2, this.balls, delta);
        currentMap.update(delta);
        EffectItem.moveItems(delta);
        EffectItem.effectCollision(paddle1, this.balls, currentMap);
        EffectItem.effectCollision(paddle2, this.balls, currentMap);

        for (Ball ball : balls) {
            ball.setIn1v1(true);
            ball.update(delta);
            ball.handleWallCollision();

            if (ball.getY() <= 0) {
                if (paddle1.hasShield()) {
                    ball.setY(0);
                    ball.reverseY();
                    Game.playSfx(Game.sfx_touchpaddle, 1.2f);
                    ball.angleSpeedAdjustment("HORIZONTAL");
                    paddle1.setShieldActive(false);
                } else {
                    ball.setDestroyed(true);
                }
            } else if (ball.getY() + ball.getHeight() >= Game.padding_top) {
                if (paddle2.hasShield()) {
                    ball.setY(Game.padding_top - ball.getHeight());
                    ball.reverseY();
                    Game.playSfx(Game.sfx_touchpaddle, 1.2f);
                    ball.angleSpeedAdjustment("HORIZONTAL");
                    paddle2.setShieldActive(false);
                } else {
                    ball.setDestroyed(true);
                }
            }

            if (ball == ballP1 && ball.isDestroyed()) {
                endRound(2);
                return;
            }

            if (ball == ballP2 && ball.isDestroyed()) {
                endRound(1);
                return;
            }

            if (ball.checkCollision(paddle1)) {
                ball.collisionWith(paddle1);
                ball.setLastHitBy(1);
            }
            else if (ball.checkCollision(paddle2)) {
                ball.collisionWith(paddle2);
                ball.setLastHitBy(2);
            }

            for (Brick brick : currentMap.getBricks()) {
                if (ball.checkCollision(brick)) {
                    brick.takeHit();
                    if (ball.isBig()) {
                        brick.setHitPoints(0);
                    }
                    if (brick.gethitPoints() == 0) {
                        //Paddle relevantPaddle = (ball.getLastHitBy() == 1) ? paddle1 : paddle2;
                        EffectItem newEffectItem = null;
                        if (ball.getLastHitBy() == 1) {
                            newEffectItem = effectFactory.tryCreateEffectItem(brick, paddle1, ball,
                                0, 0.5, 0, 0, 0, 0, 0, 0, 0, 0);
                            if (newEffectItem != null) {
                                newEffectItem.setVelocity(0, -60f);
                            }
                        } else if (ball.getLastHitBy() == 2){
                            newEffectItem = effectFactory.tryCreateEffectItem(brick, paddle2, ball,
                                0, 0.5, 0, 0, 0, 0, 0, 0, 0, 0);
                            if (newEffectItem != null) {
                                newEffectItem.setVelocity(0, 60f);
                            }

                        }

                        if (newEffectItem != null) {
                           EffectItem.addEffectItem(newEffectItem);
                        }

//                        if (newEffectItem instanceof ShieldEffect) {
//                            if (ball.getLastHitBy() == 1) {
//                                paddle1.activateShield(newEffectItem);
//                            } else if (ball.getLastHitBy() == 2) {
//                                paddle2.activateShield(newEffectItem);
//                            }
//                        }

                        if (ball.getLastHitBy() == 1) {
                            scoreManagerP1.comboScore(brick);
                        } else {
                            scoreManagerP2.comboScore(brick);
                        }

//                        if (brick.getExplosion()) {
//                            brick.startExplosion();
//                        } else {
                        brick.setDestroyed(true);
//                        }
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

        if (currentMap.getSize() == 0 || balls.isEmpty()) {
            endRound(0);
        }
    }

    @Override
    public void render(SpriteBatch sp, float delta) {}

    @Override
    public void handleInput() {}

    @Override
    public void draw(SpriteBatch sp) {}

    @Override
    public Paddle getPaddle1() {
        return this.paddle1;
    }

    @Override
    public Paddle getPaddle2() {
        return this.paddle2;
    }

    public void launchBall(int pNumber) {
        if (isPaused) {
            isPaused = false;
        }
        if (pNumber == 1 && flowPaddle1) {
            flowPaddle1 = false;
            if (ballP1 != null) ballP1.updateVelocity();
        }
        else if (pNumber == 2 && flowPaddle2) {
            flowPaddle2 = false;
            if (ballP2 != null) ballP2.updateVelocity();
        }
    }

    public ArrayList<Ball> balls() { return this.balls;  }
    public BricksMap getCurrentMap() { return this.currentMap; }
    public int getCurrentRound() {
        return this.currentRound;
    }

    public float getRoundTimer() {
        return this.roundTimer;
    }

    public ScoreManager getScoreManagerP1() {
        return this.scoreManagerP1;
    }

    public ScoreManager getScoreManagerP2() {
        return this.scoreManagerP2;
    }
    public boolean getIsGameOver() {
        return (roundsWonP1 == 2 || roundsWonP2 == 2 || (currentRound == MAX_ROUNDS && roundTimer <= 0));
    }
}
