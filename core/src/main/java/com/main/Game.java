package com.main;

import Menu.UserInterface;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import entity.GameScreen;
import entity.ScoreManager;
import entity.object.brick.BricksMap;
import entity.TextureManager;
import Menu.ModeMenu;
import com.main.gamemode.GameMode;
import com.main.gamemode.InfiniteMode;
import ui.SettingsUI;
import ui.MainMenu;

public class Game {
    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch spriteBatch;
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    public static int padding_left_right;
    public static int padding_top;
    private UserInterface ui;
    float delta;
    private ScoreManager scoreManager;
    private GameScreen gameScreen;

    GameMode gameMode;

    public static GameState gameState;
    private Main main;

    public Game(Main main) {
        this.main = main;
        this.init();
    }

    public void init() {
        scoreManager = new ScoreManager();
        gameScreen = new GameScreen(scoreManager);
        gameScreen.create();

        camera = new OrthographicCamera();
        // Tạo viewport với kích thước ảo là 800x1000 và liên kết nó với camera
        viewport = new FitViewport(800, 1000, camera);
        spriteBatch = new SpriteBatch();
        SCREEN_WIDTH = Gdx.graphics.getWidth();
        SCREEN_HEIGHT = Gdx.graphics.getHeight();
        padding_left_right = BricksMap.xBeginCoord;
        padding_top = BricksMap.yBeginCoord + BricksMap.brickH;
        delta = Gdx.graphics.getDeltaTime();

        TextureManager.loadTextures();

        this.setGameState(GameState.MAIN_MENU);
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
        gameScreen.resize(width, height);
    }

    public void render() {
        this.delta = Gdx.graphics.getDeltaTime();
        spriteBatch.begin();
        switch (gameState){
            case MAIN_MENU:
            case SETTINGS:
            case SELECT_MODE:
                ui.render();
                break;
            case INFI_MODE:
                gameMode.render(spriteBatch, this.delta);
                break;
        }
        spriteBatch.end();
    }

    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.F11)) {
            if (Gdx.graphics.isFullscreen()) {
                // Nếu đang toàn màn hình, chuyển về chế độ cửa sổ (800x1000)
                Gdx.graphics.setWindowedMode(800, 1000);
            } else {
                // Nếu đang ở cửa sổ, chuyển sang toàn màn hình
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            }
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
            setGameState(GameState.MAIN_MENU);
        }
    }

    public void update() {
        switch (gameState) {
            case INFI_MODE:
                gameMode.update(this.delta);
                break;
            case LEVELS_MODE:
                break;
        }
    }

    public void dispose() {
        spriteBatch.dispose();
        ui.dispose();
        gameScreen.dispose();
        TextureManager.dispose();
    }

    public void setGameState(GameState newGameState) {
        gameState = newGameState;
        switch (gameState) {
            case MAIN_MENU:
                ui = new MainMenu(main);
                ui.create();
                Gdx.input.setInputProcessor(ui.getStage());
                break;
            case SETTINGS:
                ui = new SettingsUI(main);
                ui.create();
                Gdx.input.setInputProcessor(ui.getStage());
                break;
            case SELECT_MODE:
                ui = new ModeMenu(main);
                ui.create();
                Gdx.input.setInputProcessor(ui.getStage());
                break;
            default:
                playGame();
        }
    }

    private void playGame() {
        viewport.apply();
        switch (gameState) {
            case INFI_MODE:
                gameMode = new InfiniteMode(scoreManager, gameScreen);
                break;
            case LEVELS_MODE:
                break;
        }
    }
}
