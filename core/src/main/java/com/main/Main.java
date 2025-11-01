package com.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * Main entry point for the game application.
 * Manages initialization, rendering, and lifecycle updates shared across all platforms.
 */
public class Main extends ApplicationAdapter {
    private Game game;

    /** Initializes the game instance and loads initial resources. */
    @Override
    public void create() {
        game = new Game(this);
    }

    /**
     * Handles window resizing by updating the game viewport.
     * @param width the new window width
     * @param height the new window height
     */
    @Override
    public void resize(int width, int height) {
        game.resize(width, height);
    }

    /** Clears the screen and updates the game loop each frame. */
    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);
        game.handleInput();
        game.update();
        game.render();
    }

    /**
     * Changes the current game state.
     * @param newGameState the target game state to switch to
     */
    public void setGameState(GameState newGameState) {
        game.setGameState(newGameState);
    }

    /**
     * Starts a new network game session.
     * @param serverIP the IP address of the server
     * @param isHost true if this client is hosting the match, false if joining
     */
    public void startNetworkGame(String serverIP, boolean isHost) {
        game.startNetworkGame(serverIP, isHost);
    }

    /**
     * Enables level selection mode.
     * @param isCoop true for co-op selection mode, false for single-player
     */
    public void setLevelSelectionMode(boolean isCoop) {
        game.setLevelSelectionMode(isCoop);
    }

    /** Disposes all resources before the application closes. */
    @Override
    public void dispose() {
        game.dispose();
    }
}
