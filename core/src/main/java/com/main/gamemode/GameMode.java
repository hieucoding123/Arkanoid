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
    protected Player player;
    private boolean isEnd;

    protected IngameInputHandler inputHandler;
    protected boolean start = false;

    protected ArrayList<Ball> balls;
    protected boolean followPaddle = true;

    protected boolean isWin = false;

    /**
     * Initialize a game mode.
     */
    public GameMode() {
        balls = new ArrayList<>();
    }

    /**
     * Checks if the game ended in a win.
     * @return true if the player won, false otherwise.
     */
    public boolean isWin() {
        return this.isWin;
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

    public void dispose() {
    }

    /**
     * Checks if the current game mode is Player-vs-Player (offline).
     * @return true if PvP, false otherwise.
     */
    public boolean isPvP() { return false; }

    public void launchBallP2() {}
}
