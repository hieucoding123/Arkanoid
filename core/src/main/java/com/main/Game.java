package com.main;

import Menu.*;
import Menu.leaderboard.LeaderBoard;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.main.gamemode.*;
import com.main.network.GameClient;
import com.main.network.GameServer;
import com.main.network.NetworkProtocol;
import Menu.CoopPlayerLevelSelectionMenu;
import entity.Effect.EffectItem;
import entity.Player;
import entity.ScoreManager;
import entity.object.brick.BricksMap;
import entity.TextureManager;
import Menu.ModeMenu;
import Menu.SinglePlayerLevelSelectionMenu;
import com.main.gamemode.GameMode;
import com.main.gamemode.InfiniteMode;
import com.main.gamemode.NetworkVsModeLogic;
import com.main.gamemode.CoopMode;
import Menu.PauseUI;
import Menu.SettingsUI;
import Menu.MainMenu;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

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
//    private GameScreen gameScreen;
    private PauseUI pauseUI;
    private ShapeRenderer shapeRenderer;
    private com.badlogic.gdx.InputProcessor previous;
    private boolean isPaused = false;
    private boolean isResumingFromSave = false;
    private int currentLives = 3;
    private double currentTimePlayed = 0.0;

    GameMode gameMode;

    public static GameState gameState;
    private final Main main;
    private Player player;
    private Player player2;

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

    public static final float MAX_MUSIC_VOLUME = 0.05f;
    public static final float MAX_SFX_VOLUME = 1.5f;

    private GameServer gameServer;
    private String networkServerIP;
    private boolean isNetworkHost;
    private GameClient networkClient;

    private String gameSummaryTitle;
    private String gameSummaryScore;
    private GameState gameSummaryNextState;
    private boolean isWin;

    public Game(Main main) {
        this.main = main;
        this.init();
    }

    public void init() {
        player = new Player();
        player2 = new Player();
        scoreManager = new ScoreManager();
        scoreManagerP2 = new ScoreManager();
//        gameScreen = new GameScreen(scoreManager);
//        gameScreen.create();

        camera = new OrthographicCamera();
        // Tạo viewport với kích thước ảo là 800x1000 và liên kết nó với camera
        viewport = new FitViewport(800, 1000, camera);
        spriteBatch = new SpriteBatch();
        SCREEN_WIDTH = Gdx.graphics.getWidth();
        SCREEN_HEIGHT = Gdx.graphics.getHeight();
        padding_left_right = BricksMap.xBeginCoord;
        padding_top = BricksMap.yBeginCoord + BricksMap.brickH;
        delta = Gdx.graphics.getDeltaTime();
        shapeRenderer = new ShapeRenderer();
        pauseUI = new PauseUI(spriteBatch, this);
        TextureManager.loadTextures();

        // Load and play audio
        loadAudio();
        bgm.setLooping(true);
        bgm.setVolume(MAX_MUSIC_VOLUME * musicVolumePercent);
        bgm.play();

        this.setGameState(GameState.MAIN_MENU);
    }

    /**
     * Loads audio assets.
     */
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

        //Game state sfx
        sfx_win = Gdx.audio.newSound(Gdx.files.internal("sound/win.mp3"));
        sfx_paused = Gdx.audio.newSound(Gdx.files.internal("sound/paused.mp3"));
    }

    public static void updateMusicVolume() {
        if (bgm != null) {
            bgm.setVolume(MAX_MUSIC_VOLUME * musicVolumePercent);
        }
    }

    /**
     * Plays a sound effect at the default global volume.
     * @param sfx The Sound object to be played.
     */
    public static void playSfx(Sound sfx) {
        sfx.play(MAX_SFX_VOLUME * sfxVolumePercent);
    }

    /**
     * Plays a sound effect with a volume relative to the default global volume.
     * @param sfx The Sound object to be played.
     * @param relativeVolume A multiplier for the global volume
     */
    public static void playSfx(Sound sfx, float relativeVolume) {
        float finalVolume = (MAX_SFX_VOLUME * sfxVolumePercent) * relativeVolume;
        sfx.play(finalVolume);
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
//        gameScreen.resize(width, height);
        if (ui != null) {
            ui.resize(width, height);
        }

        if (pauseUI != null) {
            pauseUI.resize(width, height);
        }

        if (gameMode != null) {
            gameMode.resize(width, height);
        }
    }

    private boolean isCurrentModeSaveable() {
        if (gameMode == null) return false;
        return (gameMode instanceof LevelMode) || (gameMode instanceof CoopMode || gameMode instanceof InfiniteMode);
    }

    private void updateTimeLive() {
        if (gameMode == null) return;

        if (gameMode instanceof LevelMode) {
            currentLives = ((LevelMode) gameMode).getLives();
            currentTimePlayed = gameMode.getTimePlayed();
        } else if (gameMode instanceof CoopMode) {
            currentLives = ((CoopMode) gameMode).getLives();
            currentTimePlayed = gameMode.getTimePlayed();
        } else if (gameMode instanceof InfiniteMode) {
            currentLives = ((InfiniteMode) gameMode).getLives();
            currentTimePlayed = gameMode.getTimePlayed();
        }
    }

    public void ContinueGame() {
        isPaused = !isPaused;
        if (isPaused) {
            playSfx(sfx_paused);
            if (isCurrentModeSaveable() && !isResumingFromSave) {
                updateTimeLive();
                GameSaveManager.saveGame(player, gameMode, gameState, scoreManager, currentLives, currentTimePlayed, isCoopSelection);
            }
            previous = Gdx.input.getInputProcessor();
            Gdx.input.setInputProcessor(pauseUI.getStage());
        } else {
            Gdx.input.setInputProcessor(previous);
            previous= null;
            if (isResumingFromSave) {
                GameSaveManager.deleteSave(player, gameState, isCoopSelection);
                isResumingFromSave = false;
            }
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
            case GAME_OVER:
            case VS_MENU:
            case SELECT_MODE:
                ui.render();
                break;
            case GAME_SUMMARY:
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
                    if (isPaused) {
                        Gdx.gl.glEnable(GL20.GL_BLEND);
                        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

                        shapeRenderer.setProjectionMatrix(camera.combined);
                        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                        shapeRenderer.setColor(0, 0, 0, 0.5f);
                        shapeRenderer.rect(0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
                        shapeRenderer.end();

                        Gdx.gl.glDisable(GL20.GL_BLEND);
                        pauseUI.render();
                    }
                }
                break;
        }
    }

    public void handleInput() {
        if (gameMode != null && !isPaused) {
            gameMode.handleInput();
        }
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.F11)) {
            if (Gdx.graphics.isFullscreen()) {
                // Nếu đang toàn màn hình, chuyển về chế độ cửa sổ (800x1000)
                Gdx.graphics.setWindowedMode(800, 1000);
            } else {
                // Nếu đang ở cửa sổ, chuyển sang toàn màn hình
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            }
        }
        switch (gameState) {
            case INFI_MODE:
            case VS_MODE:
            case LEVEL1:
            case LEVEL2:
            case LEVEL3:
            case LEVEL4:
            case LEVEL5:
                if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                    ContinueGame();
                }
                break;
            default:
                break;
        }
        //Debug
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.R)) {
            int x = Gdx.input.getX();
            int y_from_bottom = Gdx.graphics.getHeight() - Gdx.input.getY();
            System.out.println("Mouse Location: x = " + x + ", y = " + y_from_bottom);
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.P)) {
            if (gameServer != null) {
                gameServer.stop();
            }
            networkClient.disconnect();
            setGameState(GameState.MAIN_MENU);
        }
    }

    public void update() {
        if (isPaused) {
            pauseUI.act(this.delta);
            return;
        }
        switch (gameState) {
            case INFI_MODE:
                gameMode.update(this.delta);
                updateTimeLive();
                if (gameMode.isEnd()) {
                    this.gameSummaryTitle = "Game Over";
                    this.gameSummaryScore = "Final Score: " + (int)scoreManager.getScore();
                    this.gameSummaryNextState = GameState.SELECT_MODE;
                    this.isWin = false;
                    setGameState(GameState.GAME_SUMMARY);
                }
                break;
            case LEVEL1:
            case LEVEL2:
            case LEVEL3:
            case LEVEL4:
            case LEVEL5:
                gameMode.update(this.delta);
                updateTimeLive();
                if (gameMode.isEnd()) {
                    GameSaveManager.deleteSave(player, gameState, isCoopSelection);
                    GameSaveManager.deleteSave(player, gameState, isCoopSelection);

                    // --- MODIFIED LOGIC ---
                    boolean playerWon = gameMode.isWin();

                    this.isWin = playerWon;
                    this.gameSummaryTitle = playerWon ? "Level Complete!" : "Game Over";
                    this.gameSummaryScore = "Final Score: " + (int)scoreManager.getScore();
                    this.gameSummaryNextState = GameState.LEVELS_SELECTION;

                    setGameState(GameState.GAME_SUMMARY);
                }
                break;
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
                    setGameState(GameState.GAME_OVER);
                }
                break;
            case VS_MODE:
                gameMode.update(this.delta);
                break;
            case GAME_OVER:
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
            gameMode.dispose();
        }
    }

    public void dispose() {
        if (gameMode != null && !gameMode.isEnd() && !isPaused && isCurrentModeSaveable()) {
            updateTimeLive();
            GameSaveManager.saveGame(player, gameMode, gameState, scoreManager, currentLives, currentTimePlayed, isCoopSelection);
        }
        stopNetworkGame();
        spriteBatch.dispose();
        ui.dispose();
//        gameScreen.dispose();
        TextureManager.dispose();
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
        if (pauseUI != null) {
            pauseUI.dispose();
        }
        EffectItem.clear();
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
        if (this.gameMode != null) {
            this.gameMode.dispose();
            this.gameMode = null;
        }
        isPaused = false;
        isResumingFromSave = false;
        scoreManager.resetScore();
        currentLives = 3;
        currentTimePlayed = 0.0;
        if (pauseUI != null) {
            pauseUI.dispose();
        }
        pauseUI = new PauseUI(spriteBatch, this);
        if (ui != null) {
            ui.dispose();
            ui = null;
        }
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
                if (gameServer != null) stopNetworkGame();
                ui = new ModeMenu(main, this.player);
                ui.create();
                Gdx.input.setInputProcessor(ui.getStage());
                isCoopSelection = false;
                break;
            case LEVELS_SELECTION:
                if (isCoopSelection) {
                    ui = new CoopPlayerLevelSelectionMenu(main, this.player);
                } else {
                    // Nếu là Single, dùng menu cũ
                    ui = new SinglePlayerLevelSelectionMenu(main, this.player);
                }
                ui.create();
                Gdx.input.setInputProcessor(ui.getStage());
                break;
            case VS_MENU:
                ui = new VsMenu(main, this.player);
                ui.create();
                Gdx.input.setInputProcessor(ui.getStage());
                break;
            case NETWORK_CONNECTION_MENU:
                if (gameServer != null) stopNetworkGame();
                ui = new NetworkConnectionMenu(main, this.player);
                ui.create();
                Gdx.input.setInputProcessor(ui.getStage());
                break;
            case NETWORK_LOBBY:
                ui = new NetworkLobby(main, this.player, networkClient, isNetworkHost);
                ui.create();
                Gdx.input.setInputProcessor(ui.getStage());
                break;
            case GAME_OVER:
                ui = new GameOver(
                    main, player, (int)player.getScore(), (int)player2.getScore(),
                    networkClient, isNetworkHost);
                ui.create();
                Gdx.input.setInputProcessor(ui.getStage());
                break;
            case GAME_SUMMARY:
                if (isWin) {
                    playSfx(sfx_win);
                }
                ui = new GameSummaryScreen(main, this.player,
                    gameSummaryTitle,
                    gameSummaryScore,
                    gameSummaryNextState);
                ui.create();
                Gdx.input.setInputProcessor(ui.getStage());
                break;
            default:
                playGame();
        }
    }

    private void playGame() {
        EffectItem.clear();
        viewport.apply();
        if (GameSaveManager.isSaveableGameMode(gameState)) {
            isResumingFromSave = GameSaveManager.HaveToSave(this.player, gameState, isCoopSelection);
        } else {
            isResumingFromSave = false;
        }
        switch (gameState) {
            case INFI_MODE:
                gameMode = new InfiniteMode(this.player, scoreManager);
                break;
            case LEVEL1:
                if (isCoopSelection) {
                    gameMode = new CoopMode(this.player, scoreManager, 1);
                } else {
                    gameMode = new LevelMode(this.player, scoreManager,  1);
                }
                break;
            case LEVEL2:
                if (isCoopSelection) {
                    gameMode = new CoopMode(this.player, scoreManager, 2);
                } else {
                    gameMode = new LevelMode(this.player, scoreManager,2);
                }
                break;
            case LEVEL3:
                if (isCoopSelection) {
                    gameMode = new CoopMode(this.player, scoreManager, 3);
                } else {
                    gameMode = new LevelMode(this.player, scoreManager, 3);
                }
                break;
            case LEVEL4:
                if (isCoopSelection) {
                    gameMode = new CoopMode(this.player, scoreManager, 4);
                } else {
                    gameMode = new LevelMode(this.player, scoreManager, 4);
                }
                break;
            case LEVEL5:
                if (isCoopSelection) {
                    gameMode = new CoopMode(this.player, scoreManager, 5);
                } else {
                    gameMode = new LevelMode(this.player, scoreManager, 5);
                }
                break;
            case VS_MODE:
                gameMode = new VsMode(scoreManager, scoreManagerP2);
                break;
            case NETWORK_VS:
                playNetworkGame();
                break;

        }
        if (isResumingFromSave) {
            GameSaveManager.loadGame(this.player,gameMode, gameState, scoreManager, isCoopSelection);
            updateTimeLive();
            isPaused = true;
            if (pauseUI != null) pauseUI.dispose();
            pauseUI = new PauseUI(spriteBatch, this);
            previous = Gdx.input.getInputProcessor();
            Gdx.input.setInputProcessor(pauseUI.getStage());
        }
    }

    private void playNetworkGame() {
        gameMode = new NetworkVsMode(
            player, player2, networkServerIP, isNetworkHost, networkClient
        );
        if (isNetworkHost && gameServer != null) {
            NetworkVsModeLogic severGameMode = new NetworkVsModeLogic(scoreManager, scoreManagerP2);
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
                    Gdx.app.error("Network", e.getMessage(), e);
                }
                Gdx.app.postRunnable(() -> {
                    setGameState(GameState.NETWORK_LOBBY);
                });
            }).start();
        } catch (Exception e) {
            Gdx.app.error("Network", "Failed to connect: " + e.getMessage(), e);
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
            Gdx.app.error("Network", e.getMessage(), e);
        }
    }

    public void NewGame() {
        if (isCurrentModeSaveable()) {
            GameSaveManager.deleteSave(player, gameState, isCoopSelection);
        }
        if (gameMode != null) {
            entity.Effect.EffectItem.clear();
        }
        gameMode = null;
        scoreManager.resetScore();
        playGame();
        isPaused = false;
        previous = null;
    }

    public void ReturnMenu() {
        isPaused = false;
        if (previous != null) {
            Gdx.input.setInputProcessor(previous);
            previous = null;
        } else if (ui != null) {
            Gdx.input.setInputProcessor(ui.getStage());
        }
        if (isCurrentModeSaveable()) {
            isResumingFromSave = false;
        }
        EffectItem.clear();
        GameState state;
        if (gameState == GameState.INFI_MODE) {
            state = GameState.SELECT_MODE;
        } else if (GameSaveManager.isSaveableGameMode(gameState)) {
            state = GameState.LEVELS_SELECTION;
        } else {
            state = GameState.SELECT_MODE;
        }
        setGameState(state);
    }

    /**
     * Main getter.
     * @return main
     */
    public Main getMain() {
        return this.main;
    }

    /**
     * Player getter.
     * @return player
     */
    public Player getPlayer() {
        return this.player;
    }
}
