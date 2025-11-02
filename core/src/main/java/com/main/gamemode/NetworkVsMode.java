package com.main.gamemode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.main.gamescreen.VsGameScreen;
import com.main.network.GameClient;
import com.main.network.NetworkProtocol;
import entity.*;
import entity.object.Paddle;
import entity.object.brick.Brick;

import java.util.ArrayList;

public class NetworkVsMode extends GameMode implements GameClient.GameClientListener {
    private final GameClient client;
    private final VsGameScreen gameScreen;
    private int mPNumber;
    private NetworkProtocol.GameStateUpdate currentState;
    private final ArrayList<Brick> localBricks;
    private final Player player2;

    public NetworkVsMode(Player player1, Player player2,
                         String serverIP, boolean isHost, GameClient existingClient) {
        super();
        this.setPlayer(player1);
        this.player2 = player2;
        this.gameScreen = new VsGameScreen();
        client = existingClient;
        client.setListener(this);
        mPNumber = client.getMyPNumber();
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
    public void resize(int width, int height) {
        if (gameScreen != null) {
            gameScreen.resize(width, height);
        }
    }

    @Override
    public void handleInput() {

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

    /**
     * Draw game object in current state.
     * @param sp the {@link SpriteBatch} used for drawing.
     */
    @Override
    public void draw(SpriteBatch sp) {
        sp.draw(TextureManager.bgTexture, 0, 0, 800, 1000);
        if (currentState == null) {
            return;
        }
        // Draw paddles in current state
        for (NetworkProtocol.PaddleState paddle : currentState.paddles) {
            // Flip texture if paddle is not controlled by this player
            if (mPNumber == 1) {
                Texture paddleTexture = paddle.pNumber == 1 ? TextureManager.paddleTexture : TextureManager.flippedpaddleTexture;
                sp.draw(paddleTexture, paddle.x, paddle.y, paddle.width, paddle.height);
            } else if (mPNumber == 2) {
                Texture paddleTexture = paddle.pNumber == 1 ? TextureManager.flippedpaddleTexture : TextureManager.paddleTexture;
                sp.draw(paddleTexture, paddle.x, paddle.y, paddle.width, paddle.height);
            }
        }

        // Draw balls in current state
        for (NetworkProtocol.BallState ball : currentState.balls) {
            if (!ball.isDestroyed) {
                sp.draw(TextureManager.ballTexture, ball.x, ball.y, ball.width, ball.height);
            }
        }
        // Draw bricks in current state.
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
        if (mPNumber == 2) flip(state);
        Gdx.app.postRunnable(() -> {
            this.currentState = state;
            updateLocalBrickmap();

            // Update scores and times.
            gameScreen.setTime(state.roundTimer);
            gameScreen.setScores(state.p1Score, state.p1Wins, state.p2Score, state.p2Wins);
            getPlayer().setScore(state.p1Score);
            player2.setScore(state.p2Score);

            if (state.isGameOver) {
                this.setEnd(true);
            }
        });
    }

    /**
     * Update brickMap in current state.
     */
    private void updateLocalBrickmap() {
        localBricks.clear();
        for (NetworkProtocol.BrickState brickState : currentState.bricks) {
            Brick brick = new Brick(
                brickState.x, brickState.y,
                brickState.hitPoints, brickState.isExploding,
                0, 0, 0, brickState.type
            );
            localBricks.add(brick);
        }
    }

    /**
     * Flip position in y-axis of game object in state.
     * @param state state of game mode
     */
    private void flip(NetworkProtocol.GameStateUpdate state) {
        for (NetworkProtocol.BrickState b : state.bricks)
            b.y = 890 - b.y - b.height;
        for (NetworkProtocol.PaddleState p : state.paddles)
            p.y = 890 - p.y - p.height;
        for (NetworkProtocol.BallState b : state.balls)
            b.y = 890 - b.y - b.height;
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

    public void dispose() {
    }
}
