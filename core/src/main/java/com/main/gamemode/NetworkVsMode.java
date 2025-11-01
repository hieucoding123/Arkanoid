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
import java.util.HashMap;

public class NetworkVsMode extends GameMode implements GameClient.GameClientListener {
    private GameClient client;
    private VsGameScreen gameScreen;
    private ScoreManager scoreManager;
    private int mPNumber;
    private boolean isHost;
    private NetworkProtocol.GameStateUpdate currentState;
    private ArrayList<Brick> localBricks;

    public NetworkVsMode(Player player,
                         String serverIP, boolean isHost, GameClient existingClient) {
        super();
        this.setPlayer(player);
        this.gameScreen = new VsGameScreen();
        this.isHost = isHost;
        client = existingClient;
        client.setListener(this);
        localBricks =  new ArrayList<>();
        create();
    }

    @Override
    public void create() {
        gameScreen.create();
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void render(SpriteBatch sp, float delta) {
        handleInput();
        update(delta);
        draw(sp);
        gameScreen.render();
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
        if (currentState == null) {
            return;
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
        for (Brick brick : localBricks) {
            brick.draw(sp);
        }
    }

    @Override
    public void onConnected(int pNumber) {
        this.mPNumber = pNumber;
        System.out.println("Connected as Player " + this.mPNumber);
    }

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
