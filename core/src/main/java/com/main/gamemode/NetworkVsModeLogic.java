package com.main.gamemode;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.main.Game;
import entity.ScoreManager;
import entity.TextureManager;
import entity.object.Ball;
import entity.object.Paddle;
import entity.object.brick.Brick;
import entity.object.brick.BricksMap;

import java.util.ArrayList;

/**
 * Network Versus Mode game logic for 1v1 multiplayer Arkanoid battles.
 *
 * <p>This class implements a competitive two-player game mode where players compete
 * head-to-head to break bricks and score points. Each player controls their own paddle
 * and ball in a shared arena.</p>
 *
 * <h2>Game Rules:</h2>
 * <ul>
 *   <li><b>Players:</b> 2 players competing against each other</li>
 *   <li><b>Setup:</b> Each player has their own paddle and ball on opposite sides of the screen</li>
 *   <li><b>Ball Respawn:</b> When a player's ball falls off their side, it respawns at their paddle
 *       (no lives lost - continuous play)</li>
 *   <li><b>Scoring:</b> Players earn points by breaking bricks. Bricks broken by your ball add to your score</li>
 *   <li><b>Round End Conditions:</b>
 *     <ul>
 *       <li>Time runs out (60 seconds per round)</li>
 *       <li>All bricks are destroyed</li>
 *     </ul>
 *   </li>
 *   <li><b>Round Winner:</b> Player with the highest score at the end of the round wins that round</li>
 * </ul>
 *
 * <h2>Match Structure:</h2>
 * <ul>
 *   <li><b>Total Rounds:</b> Best of 3 (maximum 3 rounds)</li>
 *   <li><b>Victory Condition:</b> First player to win 2 rounds wins the match</li>
 *   <li><b>Tie-breaker:</b> If tied 1-1, the third round determines the winner</li>
 * </ul>
 *
 * <h2>Network Architecture:</h2>
 * <p>This class handles the server-side game logic. It runs on the host machine and:</p>
 * <ul>
 *   <li>Processes input from both players (received via network)</li>
 *   <li>Updates game physics and collision detection</li>
 *   <li>Manages round progression and scoring</li>
 *   <li>Broadcasts game state to all connected clients for rendering</li>
 * </ul>
 *
 * <h2>Example Flow:</h2>
 * <pre>
 * Round 1: Player 1 scores 1500, Player 2 scores 1200 → Player 1 wins (1-0)
 * Round 2: Player 1 scores 1000, Player 2 scores 1800 → Player 2 wins (1-1)
 * Round 3: Player 1 scores 2000, Player 2 scores 1500 → Player 1 wins match (2-1)
 * </pre>
 *
 * @see NetworkVsMode Client-side rendering counterpart
 * @see com.main.network.GameServer Network server that uses this logic
 * @see VsMode Local version of this game mode
 */

public class NetworkVsModeLogic extends GameMode {
    private static final float ROUND_DURATION = 60.0f;
    private static final int MAX_ROUNDS = 3;

    private Paddle paddle1;
    private Paddle paddle2;
    private final ArrayList<BricksMap> brickMap;
    private BricksMap currentMap;
    private final ArrayList<Ball> balls;
    private Ball ballP1;
    private Ball ballP2;
    private int currentRound;
    private boolean isPaused;
    private float roundTimer;
    private boolean flowPaddle1;
    private boolean flowPaddle2;
    private int roundsWonP1;
    private int roundsWonP2;
    private final ScoreManager scoreManagerP1;
    private final ScoreManager scoreManagerP2;
    private boolean isGameEnded = false;

    public NetworkVsModeLogic(ScoreManager scoreManagerP1, ScoreManager scoreManagerP2) {
        super();
        this.roundsWonP1 = 0;
        this.roundsWonP2 = 0;

        this.scoreManagerP1 = scoreManagerP1;
        this.scoreManagerP2 = scoreManagerP2;
        balls = new ArrayList<>();
        brickMap = new ArrayList<>();
        create();
    }

    @Override
    public void create() {

        // Create brickMaps
        for (int i = 1; i <= MAX_ROUNDS; i++) {
            String mapPath = "/maps/map_vs" + i + ".txt";
            brickMap.add(new BricksMap(mapPath));
        }

        paddle1 = new Paddle(Game.SCREEN_WIDTH / 2f - 48, 50, TextureManager.paddleTexture, false);
        paddle2 = new Paddle(Game.SCREEN_WIDTH / 2f - 48, 800, TextureManager.flippedpaddleTexture, true);

        startRound(1);
    }

    /**
     * Start new round.
     * @param roundNum round want to start
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
            5.0f);
        ballP2.setVelocity(0, -5.0f);
        balls.add(ballP2);
    }

    /**
     * Pause game, set game ended.
     */
    private void gameOver() {
        isPaused = true;
        isGameEnded = true;
    }

    @Override
    public boolean isEnd() {
        return isGameEnded;
    }

    public void endRound() {
        isPaused = true;
        if (scoreManagerP1.getScore() > scoreManagerP2.getScore()) {
            roundsWonP1++;
        } else if (scoreManagerP1.getScore() < scoreManagerP2.getScore()) {
            roundsWonP2++;
        }

        System.out.println("p1: " + scoreManagerP1.getScore());
        System.out.println("p2: " + scoreManagerP2.getScore());

        if (roundsWonP1 == 2 || roundsWonP2 == 2 || currentRound == MAX_ROUNDS) {
            gameOver();
        } else {
            startRound(currentRound + 1);
        }
    }
    @Override
    public void update(float delta) {
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
            endRound();
            return;
        }

        paddle1.update(delta);
        paddle2.update(delta);

        currentMap.update(delta);


        for (Ball ball : balls) {
            ball.setIn1v1Online(true);
            ball.update(delta);
            ball.handleWallCollision();

            // Ball is ballP1
            if (ball == ballP1 && ball.isDestroyed()) {
                ball.setDestroyed(false);
                ball.setX(paddle1.getX() + paddle1.getWidth() / 2f - 12);
                ball.setY(paddle1.getY() + paddle1.getHeight());
                paddle1.setX(Game.SCREEN_WIDTH / 2f - paddle1.getWidth() / 2f);
                paddle1.setY(50);
                flowPaddle1 = true;
                return;
            }

            if (ball == ballP2 && ball.isDestroyed()) {
                ball.setDestroyed(false);
                ball.setX(paddle2.getX() + paddle2.getWidth() / 2f - 12);
                ball.setY(paddle2.getY() - ball.getHeight());
                paddle2.setX(Game.SCREEN_WIDTH / 2f - paddle2.getWidth() / 2f);
                paddle2.setY(800);
                flowPaddle2 = true;
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
                if (!brick.isDestroyed() && ball.handleBrickCollision(brick)) {
                    brick.takeHit();
                    if (ball.isBig()) {
                        brick.setHitPoints(0);
                    }
                    if (brick.gethitPoints() == 0) {
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

        if (currentMap.getSize() == 0) {
            endRound();
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
    public int p1Wins() {
        return roundsWonP1;
    }
    public int p2Wins() {
        return roundsWonP2;
    }
}
