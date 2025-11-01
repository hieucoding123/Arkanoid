package com.main.gamemode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.main.network.GameClient;
import com.main.network.NetworkProtocol;
import entity.*;
import entity.object.Ball;
import entity.object.Paddle;
import entity.object.brick.Brick;
import entity.object.brick.BricksMap;

import java.util.ArrayList;

/**
 * Represents the online versus game mode, where two players compete in real-time over a network.
 * Handles rendering, input, and synchronization with the game server.
 */
public class NetworkVsMode extends GameMode implements GameClient.GameClientListener {
    private GameClient client;
    private VsGameScreen gameScreen;
    private ScoreManager scoreManager;
    private int mPNumber;
    private boolean isHost;
    private NetworkProtocol.GameStateUpdate currentState;
    private ArrayList<Brick> localBricks;

    /**
     * Constructs a new NetworkVsMode.
     * @param player the player instance for this client
     * @param serverIP the IP address of the game server
     * @param isHost whether this player is the host
     * @param existingClient an existing GameClient instance for network communication
     */
    public NetworkVsMode(Player player,
                         String serverIP, boolean isHost, GameClient existingClient) {
        super();
        this.setPlayer(player);
        this.gameScreen = new VsGameScreen();
        this.isHost = isHost;
        client = existingClient;
        client.setListener(this);
        localBricks = new ArrayList<>();
        create();
    }

    /** Initializes resources for the versus mode game screen. */
    @Override
    public void create() {
        gameScreen.create();
    }

    /**
     * Updates game logic every frame.
     * @param delta time since the last frame
     */
    @Override
    public void update(float delta) {
    }

    /**
     * Renders all visible game objects and UI elements.
     * @param sp the SpriteBatch used for rendering
     * @param delta time (in seconds) since the last frame
     */
    @Override
    public void render(SpriteBatch sp, float delta) {
        handleInput();
        update(delta);
        draw(sp);
        gameScreen.render();
    }

    /** Handles player keyboard input and sends corresponding network messages. */
    @Override
    public void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            client.sendInput(NetworkProtocol.InputType.MOVE_LEFT);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            client.sendInput(NetworkProtocol.InputType.MOVE_RIGHT);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            client.sendInput(NetworkProtocol.InputType.LAUNCH_BALL);
        } else {
            client.sendInput(NetworkProtocol.InputType.STOP);
        }
    }

    /**
     * Draws all visible game objects including paddles, balls, and bricks.
     * @param sp the SpriteBatch used for rendering
     */
    @Override
    public void draw(SpriteBatch sp) {
        sp.draw(TextureManager.bgTexture, 0, 0, 800, 1000);
        if (currentState == null) {
            return;
        }
        for (NetworkProtocol.PaddleState paddle : currentState.paddles) {
            if (paddle.pNumber == 1)
                sp.draw(TextureManager.paddleTexture, paddle.x, paddle.y, paddle.width, paddle.height);
            else
                sp.draw(TextureManager.flippedpaddleTexture, paddle.x, paddle.y, paddle.width, paddle.height);
        }

        for (NetworkProtocol.BallState ball : currentState.balls) {
            if (!ball.isDestroyed) {
                sp.draw(TextureManager.ballTexture, ball.x, ball.y, ball.width, ball.height);
            }
        }
        for (Brick brick : localBricks) {
            brick.draw(sp);
        }
    }

    /**
     * Called when the client successfully connects to the server.
     * @param pNumber the player number assigned by the server
     */
    @Override
    public void onConnected(int pNumber) {
        this.mPNumber = pNumber;
        System.out.println("Connected as Player " + this.mPNumber);
    }

    /**
     * Called when a game state update is received from the server.
     * @param state the new game state update
     */
    @Override
    public void onGameStateUpdated(NetworkProtocol.GameStateUpdate state) {
        Gdx.app.postRunnable(() -> {
            this.currentState = state;
            localBricks.clear();
            for (NetworkProtocol.BrickState brickState : currentState.bricks) {
                Brick brick = new Brick(
                    brickState.x, brickState.y,
                    brickState.hitPoints, brickState.isExploding,
                    0, 0, 0, TextureManager.brick1HIT
                );
                localBricks.add(brick);
            }
            gameScreen.setTime(state.roundTimer);
            gameScreen.setScores(state.p1Score, state.p1Wins, state.p2Score, state.p2Wins);

            if (state.isGameOver) {
                this.setEnd(true);
            }
        });
    }

    /** Called when the game officially starts. */
    @Override
    public void onGameStarted() {
        System.out.println("GameStarted!");
        System.out.println("VS Mode is started");
        this.start = true;
    }

    /**
     * Called when the client disconnects from the server.
     * @param reason a string describing the disconnection reason
     */
    @Override
    public void onDisconnected(String reason) {
        System.out.println("Disconnected: " + reason);
        this.setEnd(true);
    }

    /**
     * Called when a chat or debug message is received from the server.
     * @param message the message content
     */
    @Override
    public void onMessage(String message) {
        System.out.println("Message: " + message);
    }

    /**
     * Called when the lobby state changes (e.g., players joining or leaving).
     * @param update the latest lobby update
     */
    @Override
    public void onLobbyUpdate(NetworkProtocol.LobbyUpdate update) {
        // Optional: handle lobby updates
    }

    /**
     * Gets paddle 1 instance. Not used in network mode.
     * @return always {@code null}
     */
    @Override
    public Paddle getPaddle1() {
        return null;
    }

    /**
     * Gets paddle 2 instance. Not used in network mode.
     * @return always {@code null}
     */
    @Override
    public Paddle getPaddle2() {
        return null;
    }

    /** Disconnects from the server and releases network resources. */
    public void dispose() {
        if (client != null) {
            client.disconnect();
        }
    }
}
