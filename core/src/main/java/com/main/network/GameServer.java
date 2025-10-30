package com.main.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.main.gamemode.GameMode;
import com.main.gamemode.VsMode;
import entity.object.Ball;
import entity.object.Paddle;
import entity.object.brick.Brick;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameServer {
    private Server server;
    private GameMode mode;
    private Map<Connection, Integer> playerConnections; // connection -> player number
    private boolean[] playersReady;
    private boolean gameStarted;
    private float updateInterval = 1/60f;
    private float timeSinceLastUpdate = 0;

    // Lobby state
    private boolean p1Connected = false;
    private boolean p2Connected = false;

    public GameServer() {
        server = new Server(16384, 8192);
        playerConnections = new HashMap<>();
        playersReady = new boolean[2];
        NetworkProtocol.register(server);
    }

    // Start server
    public void start() throws IOException {
        server.bind(NetworkProtocol.TCP_PORT, NetworkProtocol.UDP_PORT);
        server.start();

        server.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object obj) {
                handleMessage(connection, obj);
            }
            @Override
            public void disconnected(Connection connection) {
                handleDisconnect(connection);
            }
        });
        System.out.println("Server started on port " + NetworkProtocol.TCP_PORT);
    }

    public void stop() {
        server.stop();
    }

    public void update(float delta) {
        if (!gameStarted || mode == null)
            return;
        // Update current mode
        mode.update(delta);

        // Broadcast game state with interval
        timeSinceLastUpdate += delta;
        if (timeSinceLastUpdate >= updateInterval) {
            broadcastGameState();
            timeSinceLastUpdate = 0;
        }
    }

    private void broadcastGameState() {
        if (!(mode instanceof VsMode))  return;
        VsMode vsMode =  (VsMode)mode;

        NetworkProtocol.GameStateUpdate update = new NetworkProtocol.GameStateUpdate();

        // Broadcast paddle
        Paddle p1 = mode.getPaddle1();
        Paddle p2 = mode.getPaddle2();
        if (p1 != null) {
            update.paddles.add(new NetworkProtocol.PaddleState(
                1, p1.getX(), p1.getY(), p1.getWidth(), p1.getHeight()
            ));
        }
        if (p2 != null) {
            update.paddles.add(new NetworkProtocol.PaddleState(
                2, p2.getX(), p2.getY(), p2.getWidth(), p2.getHeight()
            ));
        }

        // Update balls
        for (Ball ball : vsMode.balls()) {
            NetworkProtocol.BallState ballState = new NetworkProtocol.BallState();
            ballState.x = ball.getX();
            ballState.y = ball.getY();
            ballState.width = ball.getWidth();
            ballState.height = ball.getHeight();
            ballState.isDestroyed = ball.isDestroyed();
            ballState.lastHitBy = ball.getLastHitBy();
            update.balls.add(ballState);
        }
        // Update bricks
        if (vsMode.getCurrentMap() != null) {
            for (Brick brick : vsMode.getCurrentMap().getBricks()) {
                NetworkProtocol.BrickState brickState = new NetworkProtocol.BrickState();
                brickState.x = brick.getX();
                brickState.y = brick.getY();
                brickState.width = brick.getWidth();
                brickState.height = brick.getHeight();
                brickState.isDestroyed = brick.isDestroyed();
                update.bricks.add(brickState);
            }
        }
        // Update game status
        update.p1Score = vsMode.getScoreManagerP1().getScore();
        update.p2Score = vsMode.getScoreManagerP2().getScore();
        update.roundTimer = vsMode.getRoundTimer();
        update.currentRound = vsMode.getCurrentRound();
        update.isGameOver = vsMode.getIsGameOver();
        // ...
        server.sendToAllTCP(update);
    }

    private void handleMessage(Connection connection, Object obj) {
        if (obj instanceof NetworkProtocol.LoginRequest) {
            handleLoginRequest(connection, (NetworkProtocol.LoginRequest) obj);
        }
        else if (obj instanceof NetworkProtocol.PlayerInput) {
            handlePlayerInput(connection, (NetworkProtocol.PlayerInput) obj);
        }
        else if (obj instanceof NetworkProtocol.StartGameRequest) {
            handleStartRequest(connection, (NetworkProtocol.StartGameRequest) obj);
        }
    }

    private void handleDisconnect(Connection connection) {
        Integer pNumber = playerConnections.remove(connection);
        if (pNumber != null) {
            System.out.println("Player " + pNumber + " disconnected");
            if (pNumber == 1) {
                p1Connected = false;
                playersReady[0] = false;
            } else {
                p2Connected = false;
                playersReady[1] = false;
            }
            gameStarted = false;
            for (Connection c : playerConnections.keySet()) {
                c.sendTCP("PLAYER_DISCONNECT:" + pNumber);
                c.close();
            }
            playerConnections.clear();
            broadcastMessage("Player " + pNumber + " disconnected. Game ended.");
            broadcastLobbyUpdate();
        }
    }

    private void broadcastLobbyUpdate() {
        NetworkProtocol.LobbyUpdate update = new NetworkProtocol.LobbyUpdate(
            p1Connected,
            p2Connected,
            playersReady[0],
            playersReady[1]
        );

        for (Connection c : playerConnections.keySet()) {
            c.sendTCP(update);
        }
    }

    private void handleStartRequest(Connection connection, NetworkProtocol.StartGameRequest request) {
        int pNumber = playerConnections.get(connection);
        playersReady[pNumber - 1] = true;

        System.out.println("Player " + pNumber + " is ready");

        broadcastLobbyUpdate();

        boolean allReady = true;
        if (playerConnections.size() < 2) allReady = false;

        for (int i = 0; i < playerConnections.size(); i++) {
            if (!playersReady[i]) {
                allReady = false;
                break;
            }
        }
        if (allReady)
            startGame();
    }

    private void startGame() {
        gameStarted = true;
        System.out.println("Game started");

        for (Connection conn : playerConnections.keySet()) {
            conn.sendTCP("GAME_START");
        }
    }

    private void handlePlayerInput(Connection connection, NetworkProtocol.PlayerInput input) {
        if (!gameStarted || mode == null)
            return;
        VsMode vsMode = (VsMode)mode;

        int pNumber = playerConnections.get(connection);

        Paddle paddle = (pNumber == 1) ? vsMode.getPaddle1() : vsMode.getPaddle2();
        if (paddle == null) return;

        switch (input.inputType) {
            case MOVE_LEFT:
                paddle.moveLeft();
                break;
            case MOVE_RIGHT:
                paddle.moveRight();
                break;
            case LAUNCH_BALL:
                vsMode.launchBall(pNumber);
                break;
        }
    }

    private void handleLoginRequest(Connection connection, NetworkProtocol.LoginRequest request) {
        if (!NetworkProtocol.PROTOCOL_VERSION.equals(request.protocolVersion)) {
            NetworkProtocol.LoginResponse response = new NetworkProtocol.LoginResponse(
                false, 0, "Version mismatch! Server: " + NetworkProtocol.PROTOCOL_VERSION +
                ", Client: " + request.protocolVersion
            );
            connection.sendTCP(response);
            connection.close();
            System.err.println("Client version mismatch: " + request.protocolVersion);
            return;
        }
        // Check empty slot
        if (playerConnections.size() >= 2) {
            NetworkProtocol.LoginResponse response = new NetworkProtocol.LoginResponse(
                false, 0, "Server is full"
            );
            connection.sendTCP(response);
            return;
        }

        int playerNumber = playerConnections.size() + 1;
        playerConnections.put(connection, playerNumber);

        // Update lobby state
        if (playerNumber == 1)
            p1Connected = true;
        else
            p2Connected = true;

        NetworkProtocol.LoginResponse response = new NetworkProtocol.LoginResponse(
            true, playerNumber, "Connected as Player " + playerNumber
        );
        connection.sendTCP(response);
        System.out.println("Player " +  playerNumber + "(" + request.playerName + ")" + " connected");

        broadcastLobbyUpdate();
    }

    private void broadcastMessage(String message) {
        for  (Connection conn : playerConnections.keySet()) {
            conn.sendTCP(message);
        }
    }

    public void setGameMode(GameMode mode) {
        this.mode = mode;
    }
}
