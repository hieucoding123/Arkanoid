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
import entity.object.brick.Brick;
import entity.object.brick.BricksMap;

import java.util.ArrayList;

public class NetworkVsMode extends GameMode implements GameClient.GameClientListener {
    private GameClient client;
    private GameScreen gameScreen;
    private ScoreManager scoreManager;
//    private Paddle paddle1;
//    private Paddle paddle2;
    private int mPNumber;
    private boolean isHost;
    private NetworkProtocol.GameStateUpdate currentState;

//    private ArrayList<Ball> renderballs;
//    private Paddle renderPaddle1;
//    private Paddle renderPaddle2;

    private ArrayList<BricksMap> brickMaps;
    private BricksMap currentMap;

    public NetworkVsMode(Player player, ScoreManager scoreManager, GameScreen gameScreen,
                         String serverIP, boolean isHost, GameClient existingClient) {
        super();
        this.setPlayer(player);
        this.scoreManager = scoreManager;
        this.gameScreen = gameScreen;
        this.isHost = isHost;

        client = existingClient;
        client.setListener(this);

        create();
    }

    @Override
    public void create() {
        gameScreen.create();

//        renderPaddle1 = new Paddle(100, 100, TextureManager.paddleTexture);
//        renderPaddle2 = new Paddle(500, 100, TextureManager.paddleTexture);
        brickMaps = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            String mapPath = "/maps/map_vs" + i + ".txt";
            brickMaps.add(new BricksMap(mapPath));
        }
        currentMap = brickMaps.get(0);
    }

    @Override
    public void update(float delta) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sp, float delta) {
//        handleInput();
        update(delta);
        draw(sp);
    }

    @Override
    public void handleInput() {
//        Paddle myPaddle = (mPNumber == 1) ? paddle1 : paddle2;

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            client.sendInput(NetworkProtocol.InputType.MOVE_LEFT);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            client.sendInput(NetworkProtocol.InputType.MOVE_RIGHT);
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            client.sendInput(NetworkProtocol.InputType.LAUNCH_BALL);
        }
        else {
            client.sendInput(NetworkProtocol.InputType.STOP);
        }
    }

    @Override
    public void draw(SpriteBatch sp) {
        sp.draw(TextureManager.bgTexture, 0, 0, 800, 1000);
        if (currentState == null)
            return;

        if (currentState.currentRound > 0 && currentState.currentRound <= brickMaps.size()) {
            currentMap = brickMaps.get(currentState.currentRound - 1);
        }

        for (NetworkProtocol.PaddleState paddle : currentState.paddles) {
            if (paddle.pNumber == 1)
                sp.draw(TextureManager.paddleTexture, paddle.x,  paddle.y, paddle.width, paddle.height);
            else
                sp.draw(TextureManager.flippedpaddleTexture, paddle.x,  paddle.y, paddle.width, paddle.height);
        }

        for (NetworkProtocol.BallState ball : currentState.balls) {
            if (!ball.isDestroyed) {
                sp.draw(TextureManager.ballTexture, ball.x, ball.y, ball.width, ball.height);
            }
        }

        if (currentMap != null) {
            currentMap.draw(sp);
        }
    }

    @Override
    public void onConnected(int pNumber) {
        this.mPNumber = pNumber;
        System.out.println("Connected as Player " + this.mPNumber);
    }

    @Override
    public void onGameStateUpdated(NetworkProtocol.GameStateUpdate state) {
        this.currentState = state;
        if (currentMap != null && state.bricks != null) {
            for (NetworkProtocol.BrickState brickState : state.bricks) {
                for (int i = 0; i < state.bricks.size(); i++) {
                    currentMap.getBricks().get(i).setDestroyed(state.bricks.get(i).isDestroyed);
                    currentMap.getBricks().get(i).setHitPoints(state.bricks.get(i).hitPoints);
                }
            }
        }

        if (state.isGameOver) {
            this.setEnd(true);
        }
    }

    @Override
    public void onGameStarted() {
        System.out.println("GameStarted!");
        System.out.println("VS Mode is started");
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
    public void onLobbyUpdate(NetworkProtocol.LobbyUpdate update) {
        //
    }

    @Override
    public Paddle getPaddle1() {
        return null;        // Client don't manage paddle
    }

    @Override
    public Paddle getPaddle2() {
        return null;
    }

    public void dispose() {
        if (client != null) {
            client.disconnect();
        }
    }
}
