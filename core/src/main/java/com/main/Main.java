package com.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private Game game;
    @Override
    public void create() {
        game = new Game(this);
    }

    @Override
    public void resize(int width, int height) {
        // Cập nhật viewport với kích thước cửa sổ mới
        game.resize(width, height);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);
        game.handleInput();
        game.update();
        game.render();
    }

    public void setGameState(GameState newGameState) {
        game.setGameState(newGameState);
    }

    public void startNetworkGame(String severIP, boolean isHost) {
        game.startNetworkGame(severIP, isHost);
    }

    public void setLevelSelectionMode(boolean isCoop) {
        game.setLevelSelectionMode(isCoop);
    }

    @Override
    public void dispose() {
        game.dispose();
    }

}
