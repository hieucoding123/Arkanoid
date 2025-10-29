package com.main.gamemode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.main.network.GameClient;
import com.main.network.NetworkProtocol;
import entity.GameScreen;
import entity.Player;
import entity.ScoreManager;
import entity.TextureManager;
import entity.object.Ball;
import entity.object.Paddle;

import java.util.ArrayList;

public class NetworkVsMode extends GameMode implements GameClient.GameClientListener {
    private GameClient client;
    private GameScreen gameScreen;
    private ScoreManager scoreManager;
    private Paddle paddle1;
    private Paddle paddle2;
    private int mPNumber;
    private boolean isHost;
    private NetworkProtocol.GameStateUpdate currentState;

    private ArrayList<Ball> renderballs;
    private Paddle renderPaddle1;
    private Paddle renderPaddle2;

    public NetworkVsMode(Player player, ScoreManager scoreManager, GameScreen gameScreen,
                         String serverIP, boolean isHost) {
        super();
        this.setPlayer(player);
        this.scoreManager = scoreManager;
        this.gameScreen = gameScreen;
        this.isHost = isHost;

        client = new GameClient(this);

        try {
            client.connect(serverIP, player.getName(), NetworkProtocol.GameMode.VS);
        } catch (Exception e) {
            System.err.println("Failed to connect: " + e.getMessage());
            this.setEnd(true);
        }

        create();
    }

    @Override
    public void create() {
        gameScreen.create();

        renderPaddle1 = new Paddle(100, 100, TextureManager.paddleTexture);
        renderPaddle2 = new Paddle(100, 100, TextureManager.paddleTexture);
    }

    @Override
    public void update(float delta) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sp, float delta) {
        handleInput();
        update(delta);
        draw(sp);
    }

    @Override
    public void handleInput() {
        Paddle myPaddle = (mPNumber == 1) ? paddle1 : paddle2;

        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            client.sendInput(NetworkProtocol.InputType.MOVE_LEFT);
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            client.sendInput(NetworkProtocol.InputType.MOVE_RIGHT);
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            client.sendInput(NetworkProtocol.InputType.LAUNCH_BALL);
        }
    }

    @Override
    public void draw(SpriteBatch sp) {
        sp.draw(TextureManager.bgTexture, 0, 0, 800, 1000);
        if (currentState != null) {
            for (NetworkProtocol.PaddleState paddle : currentState.paddles) {
                if (paddle.pNumber == 1) {
                    sp.draw(TextureManager.paddleTexture, paddle.x, paddle.y, paddle.width, paddle.height);
                }else {
                    sp.draw(TextureManager.paddleTexture, paddle.x, paddle.y, paddle.width, paddle.height);
                }
            }
        }
    }

    @Override
    public void onConnected(int pNumber) {
        this.mPNumber = pNumber;
        System.out.println("Connected as Player " + this.mPNumber);

        if (this.isHost) {
            client.sendReady();
        }
    }

    @Override
    public void onGameStateUpdated(NetworkProtocol.GameStateUpdate state) {
        this.currentState = state;
        if (state.paddles.size() >= 2) {
            NetworkProtocol.PaddleState p1 = state.paddles.get(0);
            NetworkProtocol.PaddleState p2 = state.paddles.get(1);

            renderPaddle1.setX(p1.x);
            renderPaddle1.setY(p1.y);
            renderPaddle2.setX(p2.x);
            renderPaddle2.setY(p2.y);
        }

        if (state.isGameOver) {
            this.setEnd(true);
        }
    }

    @Override
    public void onGameStarted() {
        System.out.println("GameStarted!");
        this.start = true;
    }

    @Override
    public void onDisconnected(String reason) {
        System.out.println("Disconnected: " + reason);
        this.setEnd(true);
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Message: " + message);
    }

    @Override
    public Paddle getPaddle1() {
        return renderPaddle1;
    }

    @Override
    public Paddle getPaddle2() {
        return renderPaddle2;
    }

    public void dispose() {
        if (client != null) {
            client.disconnect();
        }
    }
}
