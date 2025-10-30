package com.main;

import Menu.*;
import Menu.leaderboard.LeaderBoard;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.main.gamemode.*;
import com.main.network.GameClient;
import com.main.network.GameServer;
import com.main.network.NetworkProtocol;
import entity.GameScreen;
import entity.Player;
import entity.ScoreManager;
import entity.object.brick.BricksMap;
import entity.TextureManager;
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
    private ScoreManager scoreManagerP2;
    private GameScreen gameScreen;

    GameMode gameMode;

    public static GameState gameState;
    private Main main;
    private Player player;
    private Player player2;
    private int selectedLevelNumber;

    private boolean isCoopSelection = false;

    private GameServer gameServer;
    private String networkServerIP;
    private boolean isNetworkHost;
    private GameClient networkClient;

    public Game(Main main) {
        this.main = main;
        this.init();
    }

    public void init() {
        player = new Player();
        player2 = new Player();
        scoreManager = new ScoreManager();
        scoreManagerP2 = new ScoreManager();
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
            case NETWORK_CONNECTION_MENU:
            case NETWORK_LOBBY:
            case SELECT_MODE:
                ui.render();
                break;
            case LEVELS_SELECTION:
                if (ui != null) {
                    ui.render();
                }
                break;
            case INFI_MODE:
            case VS_MODE:
            case NETWORK_VS:
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
        //Debug
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.R)) {
            int x = Gdx.input.getX();
            int y_from_bottom = Gdx.graphics.getHeight() - Gdx.input.getY();
            System.out.println("Mouse Location: x = " + x + ", y = " + y_from_bottom);
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
            case VS_MODE:
                gameMode.update(this.delta);
                if (gameMode.isEnd()) {
                    setGameState(GameState.SELECT_MODE);
                }
            case LEADER_BOARD:
                ui.update();
                break;
            case NETWORK_VS:
                if (gameMode != null) {
                    gameMode.update(this.delta);
                }
                if (isNetworkHost && gameServer != null) {
                    gameServer.update(this.delta);
                }
                if (gameMode != null && gameMode.isEnd()) {
                    stopNetworkGame();
                    setGameState(GameState.NETWORK_CONNECTION_MENU);
                }
                break;
            case NETWORK_LOBBY:
                break;
        }
    }

    private void stopNetworkGame() {
        if (gameServer != null) {
            gameServer.stop();
            gameServer = null;
        }
        if (gameMode instanceof NetworkVsMode) {
            ((NetworkVsMode) gameMode).dispose();
        }
    }

    public void dispose() {
        stopNetworkGame();
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
                ui =new LeaderBoard(main, this.player);
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
            case NETWORK_CONNECTION_MENU:
                ui = new NetworkConnectionMenu(main, this.player);
                ui.create();
                Gdx.input.setInputProcessor(ui.getStage());
                break;
            case NETWORK_LOBBY:
                ui = new NetworkLobby(main, this.player, networkClient, isNetworkHost);
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
            case VS_MODE:
                gameMode = new VsMode(this.player, this.player2, gameScreen, this.scoreManager, this.scoreManagerP2);
                break;
            case NETWORK_VS:
                playNetworkGame();
                break;
        }
    }

    private void playNetworkGame() {
        gameMode = new NetworkVsMode(
            player, scoreManager, gameScreen,
            networkServerIP, isNetworkHost, networkClient
        );
        if (isNetworkHost && gameServer != null) {
            VsMode severGameMode = new  VsMode(player, player2, gameScreen,
                scoreManager, scoreManagerP2);
            gameServer.setGameMode(severGameMode);
        }

    }

    public void startNetworkGame(String severIP, boolean isHost) {
        this.networkServerIP = severIP;
        this.isNetworkHost = isHost;

        if (isHost) {
            startGameServer();
        }

        try {
            networkClient = new GameClient(new GameClient.GameClientListener() {
                @Override
                public void onConnected(int pNumber) {
                    System.out.println("Connected to server as Player " + pNumber);
                }

                @Override
                public void onGameStateUpdated(NetworkProtocol.GameStateUpdate state) {

                }

                @Override
                public void onGameStarted() {

                }

                @Override
                public void onDisconnected(String reason) {

                }

                @Override
                public void onMessage(String message) {

                }

                @Override
                public void onLobbyUpdate(NetworkProtocol.LobbyUpdate update) {
                    System.out.println("Lobby update: P1=" + update.p1Connected +
                    " P2=" + update.p2Connected +
                    " P1Ready=" + update.p1Ready +
                    " P2Ready=" + update.p2Ready);
                }
            });
            networkClient.connect(severIP, player.getName(), NetworkProtocol.GameMode.VS);
            new Thread(() -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Gdx.app.postRunnable(() -> {
                    setGameState(GameState.NETWORK_LOBBY);
                });
            }).start();
        } catch (Exception e) {
            System.err.println("Failed to connect: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void startGameServer() {
        try {
            gameServer = new GameServer();
            gameServer.start();

            new Thread(() -> {
                while (gameServer != null) {
                    float delta = 1/60f;
                    gameServer.update(delta);

                    try {
                        Thread.sleep(16);   // ~ 60 fps
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }).start();
            System.out.println("Game server started successfully");
        } catch (Exception e) {
            System.err.println("Failed to connect: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
