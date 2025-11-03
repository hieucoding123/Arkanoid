package com.main.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.main.gamemode.GameMode;
import com.main.gamemode.NetworkVsModeLogic;
import entity.object.Ball;
import entity.object.Paddle;
import entity.object.brick.Brick;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Game server for multiplayer Arkanoid matches.
 *
 * <p>This class implements the authoritative server for networked multiplayer games.
 * It manages all game logic, processes player inputs, and broadcasts game state updates
 * to connected clients. The server uses the Kryonet networking library for efficient
 * TCP/UDP communication.</p>
 *
 * <pre>
 * Client 1 ←──[TCP/UDP]──→ GameServer ←──[TCP/UDP]──→ Client 2
 *                              ↓
 *                         Game Logic
 *                        (VsMode logic)
 * </pre>
 *
 * @see GameClient Client-side network handler
 * @see NetworkProtocol Message format definitions
 * @see GameMode Server-side game logic interface
 */
public class GameServer {
    private Server server;
    private GameMode mode;      // Game mode
    private Map<Connection, Integer> playerConnections; // connection -> player number
    private boolean[] playersReady;         // Ready state of players
    private boolean gameStarted;
    private float updateInterval = 1/60f;
    private float timeSinceLastUpdate = 0;

    // Lobby state
    private boolean p1Connected = false;
    private boolean p2Connected = false;
    private String hostName = "Server";

    /**
     * Initialize game server.
     */
    public GameServer() {
        server = new Server(131072, 65536);
        playerConnections = new HashMap<>();
        playersReady = new boolean[2];
        NetworkProtocol.register(server);
    }

    /**
     * Start game server, listening request and response from clients.
     * @throws IOException if server can bind to port.
     */
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

    /**
     * Stop game server.
     */
    public void stop() {
        server.stop();
    }

    //
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

    /**
     * Send packages contain game state to clients.
     */
    private void broadcastGameState() {
        if (!(mode instanceof NetworkVsModeLogic))  return;
        NetworkVsModeLogic networkVsModeLogic =  (NetworkVsModeLogic)mode;

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
        for (Ball ball : networkVsModeLogic.balls()) {
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
        if (networkVsModeLogic.getCurrentMap() != null) {
            for (Brick brick : networkVsModeLogic.getCurrentMap().getBricks()) {
                NetworkProtocol.BrickState brickState = new NetworkProtocol.BrickState();
                brickState.x = brick.getX();
                brickState.y = brick.getY();
                brickState.width = brick.getWidth();
                brickState.height = brick.getHeight();
                brickState.isDestroyed = brick.isDestroyed();
                brickState.hitPoints = brick.gethitPoints();
                brickState.type = brick.getType();
                brickState.isExploding = brick.isExploding();
                update.bricks.add(brickState);
            }
        }
        // Update game status
        update.p1Score = networkVsModeLogic.getScoreManagerP1().getScore();
        update.p1Wins = networkVsModeLogic.p1Wins();
        update.p2Score = networkVsModeLogic.getScoreManagerP2().getScore();
        update.p2Wins = networkVsModeLogic.p2Wins();
        update.roundTimer = networkVsModeLogic.getRoundTimer();
        update.currentRound = networkVsModeLogic.getCurrentRound();
        update.isGameOver = networkVsModeLogic.getIsGameOver();
        // ...
        server.sendToAllTCP(update);
    }

    /**
     * Handle message from clients.
     * @param connection connection of clients
     * @param obj a protocol between clients and server
     */
    private void handleMessage(Connection connection, Object obj) {
        if (obj instanceof NetworkProtocol.DiscoverServerRequest) {
            // When server is not full
            if (!gameStarted) {
                NetworkProtocol.ServerInfoResponse response = new NetworkProtocol.ServerInfoResponse();
                response.hostName = this.hostName;
                response.currentPlayers = playerConnections.size();
                response.maxPlayers = 2;
                connection.sendUDP(response);
            }
            return;
        }
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

    /**
     * Handle client disconnect with server
     * Send message to clients and disconnect.
     * @param connection connection of clients
     */
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
                c.sendTCP("PLAYER_DISCONNECTED:" + pNumber);
                c.close();
            }
            playerConnections.clear();
            broadcastMessage("Player " + pNumber + " disconnected. Game ended.");
            broadcastLobbyUpdate();
        }
    }

    /**
     * Send package from game lobby to clients.
     */
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

    /**
     * Update ready state of client. Send update message to clients.
     * @param connection connection of client
     * @param request request from client
     */
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

    /**
     * Start game.
     * Update game started state.
     * Send game start message to clients.
     */
    private void startGame() {
        gameStarted = true;
        System.out.println("Game started");

        for (Connection conn : playerConnections.keySet()) {
            conn.sendTCP("GAME_START");
        }
    }

    /**
     * Handle keyboard input from clients.
     * @param connection connection of client
     * @param input keyboard input
     */
    private void handlePlayerInput(Connection connection, NetworkProtocol.PlayerInput input) {
        if (!gameStarted || mode == null)
            return;
        NetworkVsModeLogic networkVsModeLogic = (NetworkVsModeLogic)mode;

        int pNumber = playerConnections.get(connection);

        Paddle paddle = (pNumber == 1) ? networkVsModeLogic.getPaddle1() : networkVsModeLogic.getPaddle2();
        if (paddle == null) return;

        switch (input.inputType) {
            case MOVE_LEFT:
                paddle.moveLeft();
                break;
            case MOVE_RIGHT:
                paddle.moveRight();
                break;
            case STOP:
                paddle.setVelocity(0, 0);
                break;
            case LAUNCH_BALL:
                networkVsModeLogic.launchBall(pNumber);
                break;
        }
    }

    /**
     * Handle login request of client.
     * Check match version first.
     * Then check server limit .
     * If accept login, send message and update game lobby to clients.
     * @param connection connection request from client
     * @param request request from client
     */
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
        if (playerNumber == 1) {
            p1Connected = true;
            this.hostName = request.playerName;
        } else {
            p2Connected = true;
        }

        NetworkProtocol.LoginResponse response = new NetworkProtocol.LoginResponse(
            true, playerNumber, "Connected as Player " + playerNumber
        );
        connection.sendTCP(response);
        System.out.println("Player " +  playerNumber + "(" + request.playerName + ")" + " connected");

        broadcastLobbyUpdate();
    }

    /**
     * Send message to clients.
     * @param message message from server
     */
    private void broadcastMessage(String message) {
        for  (Connection conn : playerConnections.keySet()) {
            conn.sendTCP(message);
        }
    }

    /**
     * Set game mode the server runs in.
     * @param mode game mode
     */
    public void setGameMode(GameMode mode) {
        this.mode = mode;
    }
}
