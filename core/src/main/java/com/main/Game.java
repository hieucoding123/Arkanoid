package com.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import entity.TextureManager;
import ui.MainMenu;
import ui.ModeMenu;
import entity.gamemode.GameMode;
import entity.gamemode.InfiniteMode;
import ui.SettingsUI;

public class Game {
    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch spriteBatch;
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    private SettingsUI settingsUI;
    private MainMenu mainMenu;
    private ModeMenu modeMenu;

    GameMode gameMode;

    public static GameState gameState;
    private Main main;

    public Game(Main main) {
        this.main = main;
        this.init();
    }

    public void init() {
        camera = new OrthographicCamera();
        // Tạo viewport với kích thước ảo là 800x1000 và liên kết nó với camera
        viewport = new FitViewport(800, 1000, camera);
        spriteBatch = new SpriteBatch();
        SCREEN_WIDTH = Gdx.graphics.getWidth();
        SCREEN_HEIGHT = Gdx.graphics.getHeight();
        TextureManager.loadTextures();

        mainMenu = new MainMenu(this.main);
        mainMenu.create();

        modeMenu = new ModeMenu(this.main);
        modeMenu.create();

        settingsUI = new SettingsUI(this.main);
        settingsUI.create();

        this.setGameState(GameState.MAIN_MENU);
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void render() {
        spriteBatch.begin();
        switch (gameState){
            case MAIN_MENU:
                mainMenu.render();
                break;
            case SETTINGS:
                settingsUI.render();
                break;
            case SELECT_MODE:
                modeMenu.render();
                break;
            case INFI_MODE:
                gameMode.render(spriteBatch);
                break;
        }
        spriteBatch.end();
    }

    public void update() {
        switch (gameState) {
            case INFI_MODE:
                gameMode.update();
                break;
            case LEVELS_MODE:
                break;
        }
    }

    public void dispose() {
        spriteBatch.dispose();
        mainMenu.dispose();
        modeMenu.dispose();
        settingsUI.dispose();
        TextureManager.dispose();
    }

    public void setGameState(GameState newGameState) {
        gameState = newGameState;
        switch (gameState) {
            case MAIN_MENU:
                Gdx.input.setInputProcessor(mainMenu.getStage());
                break;
            case SETTINGS:
                Gdx.input.setInputProcessor(settingsUI.getStage());
                break;
            case SELECT_MODE:
                Gdx.input.setInputProcessor(modeMenu.getStage());
                break;
            default:
                playGame();
        }
    }

    private void playGame() {
        viewport.apply();
        switch (gameState) {
            case INFI_MODE:
                gameMode = new InfiniteMode();
                break;
            case LEVELS_MODE:
                break;
        }
    }
}
