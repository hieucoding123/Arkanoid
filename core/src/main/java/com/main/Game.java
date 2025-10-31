package com.main;

import Menu.leaderboard.LeaderBoard;
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
import Menu.SinglePlayerLevelSelectionMenu;
import com.main.gamemode.GameMode;
import com.main.gamemode.InfiniteMode;
import com.main.gamemode.VsMode;
import com.main.gamemode.CoopMode;
import Menu.SettingsUI;
import Menu.MainMenu;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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

    public static Music bgm;
    public static Sound sfx_bigball;
    public static Sound sfx_bigpaddle;
    public static Sound sfx_bricked;
    public static Sound sfx_cleareffect;
    public static Sound sfx_fastball;
    public static Sound sfx_frozen;
    public static Sound sfx_random;
    public static Sound sfx_shield;
    public static Sound sfx_slowball;
    public static Sound sfx_tripleball;
    public static Sound sfx_pop;
    public static Sound sfx_explode;
    public static Sound sfx_win;
    public static Sound sfx_paused;
    public static Sound sfx_click;
    public static Sound sfx_back;
    public static Sound sfx_touchpaddle;

    public static float musicVolumePercent = 1.0f;
    public static float sfxVolumePercent = 1.0f;

    public static final float MAX_MUSIC_VOLUME = 0.3f;
    public static final float MAX_SFX_VOLUME = 1.5f;

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

        // Load and play audio
        loadAudio();
        bgm.setLooping(true);
        bgm.setVolume(MAX_MUSIC_VOLUME * musicVolumePercent);
        bgm.play();

        this.setGameState(GameState.MAIN_MENU);
    }

    private void loadAudio() {
        //BGM
        bgm = Gdx.audio.newMusic(Gdx.files.internal("sound/bgm2.mp3"));

        //SFX
        sfx_bigball = Gdx.audio.newSound(Gdx.files.internal("sound/e_bigball.mp3"));
        sfx_bigpaddle = Gdx.audio.newSound(Gdx.files.internal("sound/e_bigpaddle.mp3"));
        sfx_bricked = Gdx.audio.newSound(Gdx.files.internal("sound/e_bricked.mp3"));
        sfx_cleareffect = Gdx.audio.newSound(Gdx.files.internal("sound/e_cleareffect.mp3"));
        sfx_fastball = Gdx.audio.newSound(Gdx.files.internal("sound/e_fastball.mp3"));
        sfx_frozen = Gdx.audio.newSound(Gdx.files.internal("sound/e_frozen.mp3"));
        sfx_random = Gdx.audio.newSound(Gdx.files.internal("sound/e_random.mp3"));
        sfx_shield = Gdx.audio.newSound(Gdx.files.internal("sound/e_shield.mp3"));
        sfx_slowball = Gdx.audio.newSound(Gdx.files.internal("sound/e_slowball.mp3"));
        sfx_tripleball = Gdx.audio.newSound(Gdx.files.internal("sound/e_tripleball.mp3"));

        sfx_pop = Gdx.audio.newSound(Gdx.files.internal("sound/pop.mp3"));
        sfx_explode = Gdx.audio.newSound(Gdx.files.internal("sound/explode.mp3"));
        sfx_touchpaddle = Gdx.audio.newSound(Gdx.files.internal("sound/touchpaddle.mp3"));

        sfx_click = Gdx.audio.newSound(Gdx.files.internal("sound/click.mp3"));
        sfx_back = Gdx.audio.newSound(Gdx.files.internal("sound/back.mp3"));

        //Unimplemented
        sfx_win = Gdx.audio.newSound(Gdx.files.internal("sound/win.mp3"));
        sfx_paused = Gdx.audio.newSound(Gdx.files.internal("sound/paused.mp3"));
    }

    public static void updateMusicVolume() {
        if (bgm != null) {
            bgm.setVolume(MAX_MUSIC_VOLUME * musicVolumePercent);
        }
    }

    public static void playSfx(Sound sfx) {
        sfx.play(MAX_SFX_VOLUME * sfxVolumePercent);
    }

    public static void playSfx(Sound sfx, float relativeVolume) {
        float finalVolume = (MAX_SFX_VOLUME * sfxVolumePercent) * relativeVolume;
        sfx.play(finalVolume);
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
            if (gameServer != null) {
                gameServer.stop();
            }
            networkClient.disconnect();
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

        bgm.dispose();
        sfx_bigball.dispose();
        sfx_bigpaddle.dispose();
        sfx_bricked.dispose();
        sfx_cleareffect.dispose();
        sfx_fastball.dispose();
        sfx_frozen.dispose();
        sfx_random.dispose();
        sfx_shield.dispose();
        sfx_slowball.dispose();
        sfx_tripleball.dispose();
        sfx_pop.dispose();
        sfx_explode.dispose();
        sfx_win.dispose();
        sfx_paused.dispose();
        sfx_click.dispose();
        sfx_back.dispose();
        sfx_touchpaddle.dispose();
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
