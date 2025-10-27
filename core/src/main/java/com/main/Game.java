package com.main;

import Menu.LeaderBoard;
import Menu.UserInterface;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.main.gamemode.LevelMode;
import entity.GameScreen;
import entity.Player;
import entity.ScoreManager;
import entity.object.brick.BricksMap;
import entity.TextureManager;
import Menu.ModeMenu;
import Menu.LevelSelectionMenu;
import Menu.SinglePlayerLevelSelectionMenu;
import com.main.gamemode.GameMode;
import com.main.gamemode.InfiniteMode;
import com.main.gamemode.VsMode;
import com.main.gamemode.CoopMode;
import table.InfiDataHandler;
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
    private Player player;

    private boolean isCoopSelection = false;

    public Game(Main main) {
        this.main = main;
        this.init();
    }

    public void init() {
        player = new Player();
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
        if (ui != null) {
            ui.resize(width, height);
        }
    }

    public void render() {
        this.delta = Gdx.graphics.getDeltaTime();

        switch (gameState){
            case MAIN_MENU:
            case LEADER_BOARD:
            case SETTINGS:
            case SELECT_MODE:
            case LEVELS_SELECTION:
                if (ui != null) {
                    ui.render();
                }
                break;
            case INFI_MODE:
            case LEVEL1:
            case LEVEL2:
            case LEVEL3:
            case LEVEL4:
            case LEVEL5:
                if (gameMode != null) {
                    viewport.apply();
                    spriteBatch.setProjectionMatrix(camera.combined);
                    spriteBatch.begin();
                    gameMode.render(spriteBatch, this.delta);
                    spriteBatch.end();
                    gameScreen.render();
                }
                break;
        }
    }

    public void handleInput() {
        if (gameMode != null)
            gameMode.handleInput();
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
                if (gameMode.isEnd()) {
                    setGameState(GameState.SELECT_MODE);
                }
                break;
            case LEVEL1:
            case LEVEL2:
            case LEVEL3:
            case LEVEL4:
            case LEVEL5:
                gameMode.update(this.delta);
                if (gameMode.isEnd()) {
                    setGameState(GameState.LEVELS_SELECTION);
                }
                break;
        }
    }

    public void dispose() {
        spriteBatch.dispose();
        ui.dispose();
        gameScreen.dispose();
        TextureManager.dispose();
    }

    public void setLevelSelectionMode(boolean isCoop) {
        this.isCoopSelection = isCoop;
        setGameState(GameState.LEVELS_SELECTION);
    }

    public void setGameState(GameState newGameState) {
        gameState = newGameState;

        switch (gameState) {
            case MAIN_MENU:
                ui = new MainMenu(main, this.player);
                ui.create();
                ui.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                Gdx.input.setInputProcessor(ui.getStage());
                break;
            case LEADER_BOARD:
                ui =new LeaderBoard(main, this.player, InfiDataHandler.getLeaderboardData());
                ui.create();
                Gdx.input.setInputProcessor(ui.getStage());
                break;
            case SETTINGS:
                ui = new SettingsUI(main, this.player);
                ui.create();
                Gdx.input.setInputProcessor(ui.getStage());
                break;
            case SELECT_MODE:
                ui = new ModeMenu(main, this.player);
                ui.create();
                Gdx.input.setInputProcessor(ui.getStage());
                isCoopSelection = false;
                break;
            case LEVELS_SELECTION:
                ui = new SinglePlayerLevelSelectionMenu(main, this.player);
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
                gameMode = new InfiniteMode(this.player, scoreManager, gameScreen);
                break;
            case LEVEL1:
                if (isCoopSelection) {
                    gameMode = new CoopMode(this.player, scoreManager, gameScreen, 1);
                } else {
                    gameMode = new LevelMode(this.player, scoreManager, gameScreen, 1);
                }
                break;
            case LEVEL2:
                if (isCoopSelection) {
                    gameMode = new CoopMode(this.player, scoreManager, gameScreen, 2);
                } else {
                    gameMode = new LevelMode(this.player, scoreManager, gameScreen, 2);
                }
                break;
            case LEVEL3:
                if (isCoopSelection) {
                    gameMode = new CoopMode(this.player, scoreManager, gameScreen, 3);
                } else {
                    gameMode = new LevelMode(this.player, scoreManager, gameScreen, 3);
                }
                break;
            case LEVEL4:
                if (isCoopSelection) {
                    gameMode = new CoopMode(this.player, scoreManager, gameScreen, 4);
                } else {
                    gameMode = new LevelMode(this.player, scoreManager, gameScreen, 4);
                }
                break;
            case LEVEL5:
                if (isCoopSelection) {
                    gameMode = new CoopMode(this.player, scoreManager, gameScreen, 5);
                } else {
                    gameMode = new LevelMode(this.player, scoreManager, gameScreen, 5);
                }
                break;

        }
    }
}
