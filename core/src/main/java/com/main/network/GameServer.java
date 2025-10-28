package com.main.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.main.gamemode.GameMode;
import entity.object.Paddle;

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
        NetworkProtocol.GameStateUpdate update = new NetworkProtocol.GameStateUpdate();

        // Broadcast paddle
        Paddle p1 = mode.getPaddle1();
        Paddle p2 = mode.getPaddle2();
        if (p1 != null) {
            update.paddles.add(new NetworkProtocol.PaddleState(
                1, p1.getX(), p1.getX(), p1.getWidth(), p1.getHeight()
            ));
        }
        if (p2 != null) {
            update.paddles.add(new NetworkProtocol.PaddleState(
                2, p2.getX(), p2.getX(), p2.getWidth(), p2.getHeight()
            ));
        }

        // Broadcast balls
        // Broadcast bricks
        // ...
        server.sendToAllTCP(update);
    }

    private void handleMessage(Connection connection, Object obj) {
        if (obj instanceof NetworkProtocol.LoginResponse) {
            handleLoginRequest(connection, (NetworkProtocol.LoginResponse) obj);
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
            gameStarted = false;
            broadcastMessage("Player " + pNumber + " disconnected. Game ended.");
        }
    }

    private void handleStartRequest(Connection connection, NetworkProtocol.StartGameRequest request) {
        int pNumber = playerConnections.get(connection);
        playersReady[pNumber - 1] = true;

        System.out.println("Player " + pNumber + " is ready");

        if (playersReady[0] && playersReady[1]) {
            startGame();
        }
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

        int pNumber = playerConnections.get(connection);
        Paddle paddle = (pNumber == 1) ? mode.getPaddle1() : mode.getPaddle2();
        if (paddle == null)
            return;

        switch (input.inputType) {
            case MOVE_LEFT:
                paddle.moveLeft();
                break;
            case MOVE_RIGHT:
                paddle.moveRight();
                break;
            case LAUNCH_BALL:
                mode.launchBall();
                break;
        }
    }

    private void handleLoginRequest(Connection connection, NetworkProtocol.LoginResponse obj) {
        // Check empty slot
        if (playerConnections.size() >= 2) {
            NetworkProtocol.LoginResponse response = new NetworkProtocol.LoginResponse(
                false, 0, "Sever is full"
            );
            connection.sendTCP(response);
            return;
        }

        int playerNumber = playerConnections.size() + 1;
        playerConnections.put(connection, playerNumber);

        NetworkProtocol.LoginResponse response = new NetworkProtocol.LoginResponse(
            true, playerNumber, "Connected as Player " + playerNumber
        );
        connection.sendTCP(response);
        System.out.println("Player " +  playerNumber + " connected");

        if (playerConnections.size() == 2)
            broadcastMessage("All players are connected. Ready to start!");
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
