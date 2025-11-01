package com.main.gamemode;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.main.components.IngameInputHandler;
import entity.Player;
import entity.object.Ball;
import entity.object.Paddle;
import entity.object.brick.BricksMap;

import java.util.ArrayList;

/**
 * Abstract base class representing a general game mode.
 * Handles core gameplay logic shared across different modes.
 */
public abstract class GameMode {
    private Player player;
    private boolean isEnd;

    protected IngameInputHandler inputHandler;
    protected boolean start = false;

    protected ArrayList<Ball> balls;
    protected boolean followPaddle = true;

    private float debugPrintTimer = 0f;
    private static final float DEBUG_PRINT_INTERVAL = 0.5f;

    /**
     * Initialize a game mode.
     */
    public GameMode() {
        balls = new ArrayList<>();
    }

    /**
     * Gets the current player.
     * @return player instance.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the player for this game mode.
     * @param player player to assign
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Checks if the game mode has ended.
     * @return true if ended, false otherwise
     */

    public boolean isEnd() {
        return isEnd;
    }

    /**
     * Sets the end state of the game.
     * @param end true if the game has ended, false otherwise.
     */
    public void setEnd(boolean end) {
        isEnd = end;
    }

    /**
     * Gets the first paddle used in the game.
     * @return {@link Paddle} object for player 1.
     */
    public abstract Paddle getPaddle1();

    /**
     * Gets the second paddle if available (e.g. for Vs or Coop modes).
     * @return {@link Paddle} object for player 2, or null if not used.
     */
    public Paddle getPaddle2() {
        return null;
    }

    /**
     * Launches the ball from the paddle if following it.
     */
    public void launchBall() {
        if (followPaddle) {
            followPaddle = false;
            if (!balls.isEmpty()) {
                balls.get(0).updateVelocity();
            }
        }
    }

    /**
     * Starts the game if not already started.
     * @param start true to start the game.
     */
    public void isStart(boolean start) {
        if (!this.start) {
            this.start = start;
        }
    }

    /**
     * Initializes all required game objects and resources.
     */
    public abstract void create();

    /**
     * Updates the game state.
     * @param delta time elapsed since last frame (in seconds).
     */
    public abstract void update(float delta);

    /**
     * Renders the game scene.
     * @param sp SpriteBatch used for drawing.
     * @param delta time elapsed since last frame (in seconds).
     */
    public abstract void render(SpriteBatch sp, float delta);

    /**
     * Handles input processing for the current game mode.
     */
    public abstract void handleInput();

    /**
     * Draws all game objects to the screen.
     * @param sp the {@link SpriteBatch} used for drawing.
     */
    public abstract void draw(SpriteBatch sp);

//    public void printActiveEffects(float delta) {
//        // Increment timer
//        debugPrintTimer += delta;
//
//        // Check if interval has passed
//        if (debugPrintTimer < DEBUG_PRINT_INTERVAL) {
//            return; // Not time to print yet
//        }
//
//        // Reset timer
//        debugPrintTimer -= DEBUG_PRINT_INTERVAL;
//
//        // --- The rest of the print logic ---
//        System.out.println("--- ACTIVE EFFECTS DEBUG ---");
//
//        // --- Check Paddle 1 ---
//        Paddle paddle1 = getPaddle1();
//        if (paddle1 != null) {
//            long expandTime = paddle1.getTimeExpandEffect(); //
//            long stunTime = paddle1.getTimeStunEffect();
//
//            if (expandTime > 0) {
//                System.out.println("P1: Expand Active (" + String.format("%.2f", expandTime / 1000.0) + "s)");
//            }
//            if (stunTime > 0) {
//                System.out.println("P1: Stunned Active (" + String.format("%.2f", stunTime / 1000.0) + "s)");
//            }
//        }
//
//        // --- Check Paddle 2 (for VsMode) ---
//        Paddle paddle2 = getPaddle2();
//        if (paddle2 != null) {
//            long expandTimeP2 = paddle2.getTimeExpandEffect(); //
//            long stunTimeP2 = paddle2.getTimeStunEffect();
//
//            if (expandTimeP2 > 0) {
//                System.out.println("P2: Expand Active (" + String.format("%.2f", expandTimeP2 / 1000.0) + "s)");
//            }
//            if (stunTimeP2 > 0) {
//                System.out.println("P2: Stunned Active (" + String.format("%.2f", stunTimeP2 / 1000.0) + "s)");
//            }
//        }
//
//        // --- Check All Balls ---
//        if (balls != null && !balls.isEmpty()) {
//            for (int i = 0; i < balls.size(); i++) {
//                Ball ball = balls.get(i);
//                long bigTime = ball.getTimeBigWEffect();
//                long slowTime = ball.getTimeSlowEffect();
//                long fastTime = ball.getTimeFastEffect();
//
//                if (bigTime > 0) {
//                    System.out.println("Ball " + i + ": Big Active (" + String.format("%.2f", bigTime / 1000.0) + "s)");
//                }
//                if (slowTime > 0) {
//                    System.out.println("Ball " + i + ": Slow Active (" + String.format("%.2f", slowTime / 1000.0) + "s)");
//                }
//                if (fastTime > 0) {
//                    System.out.println("Ball " + i + ": Fast Active (" + String.format("%.2f", fastTime / 1000.0) + "s)");
//                }
//            }
//        }
//        System.out.println("----------------------------");
//    }

    /**
     * Checks if the ball is currently following the paddle.
     * @return true if following, false otherwise.
     */
    public boolean isFollowPaddle() {
        return followPaddle;
    }

    /**
     * Gets all active balls in the current game mode.
     * @return list of {@link Ball} objects.
     */
    public ArrayList<Ball> getBalls() { return this.balls; }

    /**
     * Gets the current brick map if available.
     * @return the {@link BricksMap}, or null if not applicable.
     */
    public BricksMap getCurrentMap() { return null; }

    /**
     * Sets the number of remaining lives.
     * @param lives the number of lives.
     */
    public void setLives(int lives) {}

    /**
     * Gets the total time played.
     * @return time played in seconds.
     */
    public double getTimePlayed() { return 0.0; }

    /**
     * Sets the total time played.
     * @param time total time in seconds.
     */
    public void setTimePlayed(double time) {}

    /**
     * Enables or disables ball following the paddle.
     * @param follow true to follow paddle, false to release.
     */
    public void setFollowPaddle(boolean follow) { this.followPaddle = follow; }

    public void resize (int width, int height) {

    }
}
