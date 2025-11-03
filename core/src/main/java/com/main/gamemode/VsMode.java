package com.main.gamemode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.main.Game;
import com.main.components.CollisionManager;
import com.main.components.IngameInputHandler;
import com.main.gamescreen.VsGameScreen;
import entity.Player;
import entity.object.effect.EffectFactory;
import entity.object.effect.EffectItem;
import com.main.components.ScoreManager;
import com.main.components.TextureManager;
import entity.object.Ball;
import entity.object.Paddle;
import entity.object.brick.Brick;
import entity.object.brick.BricksMap;
import java.util.ArrayList;

/**
 * Represents the versus (PvP) game mode where two players compete
 * against each other in multiple rounds.
 */
public class VsMode extends GameMode {
    private static final float ROUND_DURATION = 60.0f;
    private static final int MAX_ROUNDS = 3;

    private final Player player2;
    private Paddle paddle1;
    private Paddle paddle2;
    private final EffectFactory effectFactory;
    private final ArrayList<BricksMap> brickMap;
    private BricksMap currentMap;
    private final ArrayList<Ball> balls;
    private final VsGameScreen gameScreen;
    private Ball ballP1;
    private Ball ballP2;
    private int currentRound;
    private boolean isPaused;
    private float roundTimer;
    private boolean followPaddle1;
    private boolean followPaddle2;
    private int roundsWonP1;
    private int roundsWonP2;
    private final ScoreManager scoreManagerP1;
    private final ScoreManager scoreManagerP2;
    private boolean isGameEnded = false;

    /**
     * Constructs a new versus mode with two score managers.
     *
     * @param scoreManagerP1 score manager for player 1
     * @param scoreManagerP2 score manager for player 2
     */
    public VsMode(Player player1, Player player2, ScoreManager scoreManagerP1, ScoreManager scoreManagerP2) {
        super();
        this.setPlayer(player1);
        this.player2 = player2;
        this.roundsWonP1 = 0;
        this.roundsWonP2 = 0;
        this.scoreManagerP1 = scoreManagerP1;
        this.scoreManagerP2 = scoreManagerP2;
        this.effectFactory = new EffectFactory();
        this.gameScreen = new VsGameScreen();
        balls = new ArrayList<>();
        brickMap = new ArrayList<>();
        create();
    }

    /**
     * Initializes game resources, input handlers, paddles, and maps.
     */
    @Override
    public void create() {
        gameScreen.create();
        for (int i = 1; i <= 3; i++) {
            String mapPath = "/maps/map_vs" + i + ".txt";
            brickMap.add(new BricksMap(mapPath));
        }
        paddle1 = new Paddle(Game.SCREEN_WIDTH / 2f - 48, 50, TextureManager.paddleTexture, false);
        paddle2 = new Paddle(Game.SCREEN_WIDTH / 2f - 48, 800, TextureManager.flippedpaddleTexture, true);
        startRound(1);

        this.inputHandler = new IngameInputHandler(this);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(gameScreen.getStage());
        multiplexer.addProcessor(this.inputHandler);
        Gdx.input.setInputProcessor(multiplexer);
    }

    /**
     * Starts a specific round of the versus game.
     *
     * @param roundNum the round number to start
     */
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

    /**
     * Resets both players' balls at the start of a round.
     */
    public void resetBalls() {
        balls.clear();
        followPaddle1 = true;
        followPaddle2 = true;

        ballP1 = new Ball(paddle1.getX() + paddle1.getWidth() / 2f - 12,
            paddle1.getY() + paddle1.getHeight(),
            TextureManager.ballTexture,
            2.5f);
        balls.add(ballP1);

        ballP2 = new Ball(paddle2.getX() + paddle2.getWidth() / 2f - 12,
            paddle2.getY() - ballP1.getHeight(),
            TextureManager.ballTexture,
            2.5f);
        balls.add(ballP2);
    }

    /**
     * Ends the game and pauses gameplay.
     */
    private void gameOver() {
        isPaused = true;
        isGameEnded = true;
    }

    /**
     * Checks whether the versus game has ended.
     *
     * @return true if the game has ended, false otherwise
     */
    @Override
    public boolean isEnd() {
        return isGameEnded;
    }

    /**
     * Ends the current round and determines the winner.
     */
    public void endRound() {
        isPaused = true;
        if (scoreManagerP1.getScore() > scoreManagerP2.getScore()) {
            roundsWonP1++;
        } else if (scoreManagerP1.getScore() < scoreManagerP2.getScore()) {
            roundsWonP2++;
        }
        System.out.println("p1: " + roundsWonP1);
        System.out.println("p2: " + roundsWonP2);

        if (roundsWonP1 == 2 || roundsWonP2 == 2 || currentRound == MAX_ROUNDS) {
            gameOver();
        } else {
            startRound(currentRound + 1);
        }
    }

    /**
     * Updates the game logic for versus mode.
     *
     * @param delta time elapsed since the last frame
     */
    @Override
    public void update(float delta) {
        if (followPaddle1) {
            ballP1.setX(paddle1.getX() + (paddle1.getWidth() / 2f) - ballP1.getWidth() / 2f);
            ballP1.setY(paddle1.getY() + paddle1.getHeight());
            ballP1.setAngle((float) Math.PI / 2f);
        }
        if (followPaddle2) {
            ballP2.setX(paddle2.getX() + (paddle2.getWidth() / 2f) - ballP2.getWidth() / 2f);
            ballP2.setY(paddle2.getY() - ballP2.getHeight());
            ballP2.setAngle(-(float) Math.PI / 2f);
        }
        if (isPaused) return;
        if (!followPaddle1 || !followPaddle2) {
            roundTimer -= delta;
        }
        if (roundTimer <= 0) {
            endRound();
            return;
        }
        paddle1.update(delta);
        paddle2.update(delta);
        currentMap.update(delta);
        EffectItem.moveItems(delta);
        EffectItem.effectCollision(paddle1, this.balls, currentMap);
        EffectItem.effectCollision(paddle2, this.balls, currentMap);

        final int SOLVER_ITERATIONS = 5;
        for (int k = 0; k < SOLVER_ITERATIONS; k++) {
            for (int i = 0; i < balls.size(); i++) {
                Ball ball1 = balls.get(i);
                for (int j = i + 1; j < balls.size(); j++) {
                    Ball ball2 = balls.get(j);
                    CollisionManager.handleBallBallCollision(ball1, ball2);
                }
            }
        }

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
            if (ball.checkCollision(paddle1)) {
                ball.collisionWith(paddle1);
                ball.setLastHitBy(1);
            } else if (ball.checkCollision(paddle2)) {
                ball.collisionWith(paddle2);
                ball.setLastHitBy(2);
            }
            for (Brick brick : currentMap.getBricks()) {
                if (!brick.isDestroyed() && ball.handleBrickCollision(brick)) {
                    brick.takeHit();
                    if (ball.isBig()) {
                        brick.setHitPoints(0);
                    }
                    if (brick.gethitPoints() == 0) {
                        EffectItem newEffectItem = null;
                        if (ball.getLastHitBy() == 1) {
                            newEffectItem = effectFactory.tryCreateEffectItem(true, brick, paddle1, ball,
                                0.02, 0.03, 0.03, 0.03, 0.04, 0.05, 0.06, 0.07, 0.08, 0);
                            if (newEffectItem != null) {
                                newEffectItem.setVelocity(0, -60f);
                            }
                        } else if (ball.getLastHitBy() == 2) {
                            newEffectItem = effectFactory.tryCreateEffectItem(true, brick, paddle2, ball,
                                0.02, 0.03, 0.03, 0.03, 0.04, 0.05, 0.06, 0.07, 0.08, 0);
                            if (newEffectItem != null) {
                                newEffectItem.setVelocity(0, 60f);
                            }
                        }
                        if (newEffectItem != null) {
                            EffectItem.addEffectItem(newEffectItem);
                        }
                        if (ball.getLastHitBy() == 1) {
                            scoreManagerP1.comboScore(brick);
                        } else {
                            scoreManagerP2.comboScore(brick);
                        }
                        brick.setDestroyed(true);
                    }
                    break;
                }
            }
        }
        balls.removeIf(Ball::isDestroyed);
        if (currentMap.getSize() == 0 || balls.isEmpty()) {
            endRound();
        }
    }

    /**
     * Renders the versus game mode.
     *
     * @param sp the SpriteBatch for rendering
     * @param delta time elapsed since the last frame
     */
    @Override
    public void render(SpriteBatch sp, float delta) {
        this.update(delta);
        gameScreen.setTime(roundTimer);
        gameScreen.setScores(scoreManagerP1.getScore(), roundsWonP1,
            scoreManagerP2.getScore(), roundsWonP2);
        this.draw(sp);
        gameScreen.render();
    }

    /**
     * Processes user input for both players.
     */
    @Override
    public void handleInput() {
        if (inputHandler != null) {
            inputHandler.processMovement();
        }
    }

    /**
     * Draws all entities for the versus mode.
     *
     * @param sp the SpriteBatch used for drawing
     */
    @Override
    public void draw(SpriteBatch sp) {
        sp.draw(TextureManager.bgTexture, 0, 0, 800, 1000);
        currentMap.draw(sp);
        EffectItem.drawEffectItems(sp);
        if (paddle1.hasShield()) {
            sp.draw(TextureManager.lineTexture,
                Game.padding_left_right, 0,
                Game.SCREEN_WIDTH - 2 * Game.padding_left_right,
                5);
        }
        if (paddle2.hasShield()) {
            sp.draw(TextureManager.lineTexture,
                Game.padding_left_right, Game.padding_top - 5,
                Game.SCREEN_WIDTH - 2 * Game.padding_left_right,
                5);
        }
        if (followPaddle1) {
            ballP1.draw(sp);
        }
        if (followPaddle2) {
            ballP2.draw(sp);
        }
        for (Ball ball : balls) {
            ball.draw(sp);
        }
        paddle1.draw(sp);
        paddle2.draw(sp);
    }

    /**
     * Indicates that this mode is a PvP (versus) mode.
     *
     * @return true always
     */
    @Override
    public boolean isPvP() {
        return true;
    }

    /**
     * Gets player one's paddle.
     *
     * @return paddle of player 1
     */
    @Override
    public Paddle getPaddle1() {
        return this.paddle1;
    }

    /**
     * Gets player two's paddle.
     *
     * @return paddle of player 2
     */
    @Override
    public Paddle getPaddle2() {
        return this.paddle2;
    }

    /**
     * Launches player one's ball.
     */
    @Override
    public void launchBall() {
        if (followPaddle1) {
            followPaddle1 = false;
            isPaused = false;
        }
    }

    /**
     * Launches player two's ball.
     */
    @Override
    public void launchBallP2() {
        if (followPaddle2) {
            followPaddle2 = false;
            isPaused = false;
        }
    }

    /**
     * Getter for player1's round won.
     * @return player1's round won
     */
    public int getRoundsWonP1() {
        return this.roundsWonP1;
    }

    /**
     * Getter for player2's round won.
     * @return player2's round won
     */
    public int getRoundsWonP2() {
        return this.roundsWonP2;
    }

    /**
     * Getter for player1's scoreManager.
     * @return player1's scoreManager
     */
    public ScoreManager getScoreManagerP1() {
        return this.scoreManagerP1;
    }

    /**
     * Getter for player2's scoreManager.
     * @return player2's scoreManager
     */
    public ScoreManager getScoreManagerP2() {
        return this.scoreManagerP2;
    }
}
