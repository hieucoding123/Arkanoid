package com.main.network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

/**
 * Game client for connecting to multiplayer Arkanoid servers.
 *
 * <p>This class handles all client-side networking, including connecting to servers,
 * sending player inputs, and receiving game state updates. It acts as the communication
 * layer between the local game and the remote server.</p>
 *
 * <h2>Client Responsibilities:</h2>
 * <ul>
 *   <li><b>Connection:</b> Establish TCP/UDP connections to game servers</li>
 *   <li><b>Authentication:</b> Send login credentials and receive player assignment</li>
 *   <li><b>Input Transmission:</b> Send player inputs (movement, actions) to server</li>
 *   <li><b>State Reception:</b> Receive and forward game state updates to listeners</li>
 *   <li><b>Event Notification:</b> Notify game logic of connection events via callbacks</li>
 * </ul>
 *
 * <h2>Event-Driven Architecture:</h2>
 * <p>The client uses the observer pattern through {@link GameClientListener}.
 * Game logic implements this interface to receive network events:</p>
 * <pre>
 * class MyGameMode implements GameClientListener {
 *     public void onConnected(int playerNumber) {
 *         // Server assigned us a player number
 *     }
 *
 *     public void onGameStateUpdated(GameStateUpdate state) {
 *         // Server sent new game state - update rendering
 *     }
 * }
 * </pre>
 *
 * <h2>Connection Flow:</h2>
 * <ol>
 *   <li><b>Create:</b> {@code new GameClient(listener)} - Initialize with event listener</li>
 *   <li><b>Connect:</b> {@code client.connect(serverIP, playerName, gameMode)} - Establish connection</li>
 *   <li><b>Login:</b> Client sends LoginRequest → Server assigns player number</li>
 *   <li><b>Lobby:</b> Client receives LobbyUpdates → Shows lobby UI</li>
 *   <li><b>Ready:</b> {@code client.sendReady()} - Signal ready to play</li>
 *   <li><b>Game:</b> Client sends inputs, receives state updates</li>
 *   <li><b>Disconnect:</b> {@code client.disconnect()} - Clean disconnect</li>
 * </ol>
 *
 * <h2>Usage Example:</h2>
 * <pre>
 * GameClient client = new GameClient(new GameClientListener() {
 *     public void onConnected(int playerNumber) {
 *         System.out.println("Connected as Player " + playerNumber);
 *     }
 *
 *     public void onGameStateUpdated(GameStateUpdate state) {
 *         // Update local game rendering
 *         updatePaddles(state.paddles);
 *         updateBalls(state.balls);
 *     }
 * });
 *
 * // Connect to server
 * client.connect("192.168.1.100", "PlayerName", GameMode.VS);
 *
 * // Send input (e.g., in game loop)
 * if (keyPressed(LEFT)) {
 *     client.sendInput(InputType.MOVE_LEFT);
 * }
 *
 * // Disconnect when done
 * client.disconnect();
 * </pre>
 *
 * <h2>Thread Safety:</h2>
 * <p>Network events are received on Kryonet's network thread. Listener callbacks
 * should update game state thread-safely (e.g., using {@code Gdx.app.postRunnable()}
 * for libGDX rendering updates).</p>
 *
 * @see GameClientListener Event callback interface
 * @see GameServer Server-side network handler
 * @see NetworkProtocol Message format definitions
 */

public class GameClient {
    private Client client;
    private int myPNumber;
    private boolean connected = false;
    private NetworkProtocol.GameStateUpdate lastGameState;
    private GameClientListener listener;

    /**
     * A type of client can receive packages from {@link GameServer}.
     * And handle them.
     */
    public interface GameClientListener {
        /**
         * Receive connected information.
         * @param pNumber connected player's number
         */
        void onConnected(int pNumber);

        /**
         * Receive game update information.
         * @param state game update state.
         */
        void onGameStateUpdated(NetworkProtocol.GameStateUpdate state);

        /**
         * Receive game started information.
         */
        void onGameStarted();

        /**
         * Get disconnected information.
         * @param reason reason for diconnected
         */
        void onDisconnected(String reason);

        /**
         * Get message from {@link GameServer}.
         * @param message message from server
         */
        void onMessage(String message);

        /**
         * Handle update from {@link Menu.NetworkLobby}.
         * @param update update from lobby
         */
        void onLobbyUpdate(NetworkProtocol.LobbyUpdate update);
    }

    public GameClient(GameClientListener listener) {
        this.listener = listener;
        client = new Client(131072, 65536);
        NetworkProtocol.register(client);
    }

    /**
     * Connect client to server.
     * Send type of game mode request to connect.
     * @param host IP of server request to connect
     * @param pName name of {@link entity.Player}
     * @param mode type of game mode request to connect
     * @throws IOException if can't connect to server.
     */
    public void connect(String host, String pName, NetworkProtocol.GameMode mode) throws IOException {
        client.start();
        client.connect(5000, host, NetworkProtocol.TCP_PORT, NetworkProtocol.UDP_PORT);

        // Add listener to handle message from server
        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                handleMessage(object);
            }
            @Override
            public void disconnected(Connection connection) {
                handleDisconnect();
            }
        });

        NetworkProtocol.LoginRequest loginRequest = new NetworkProtocol.LoginRequest(
            pName, mode
        );
        client.sendTCP(loginRequest);
    }

    /**
     * Send input signal to {@link GameServer}
     * @param inputType instance of {@link NetworkProtocol.InputType}
     */
    public void sendInput(NetworkProtocol.InputType inputType) {
        if (!connected) return;

        NetworkProtocol.PlayerInput input = new NetworkProtocol.PlayerInput(
            myPNumber, inputType
        );
        client.sendUDP(input);
    }

    /**
     * Send ready to game request to {@link GameServer}.
     */
    public void sendReady() {
        if (!connected) return;

        NetworkProtocol.StartGameRequest request = new NetworkProtocol.StartGameRequest();
        client.sendTCP(request);
    }

    /**
     * Handler message from {@link GameServer} send back.
     * @param obj instance of {@link NetworkProtocol} or message
     */
    private void handleMessage(Object obj) {
        if (obj instanceof NetworkProtocol.LoginResponse) {
            handleLoginResponse((NetworkProtocol.LoginResponse) obj);
        }
        else if (obj instanceof NetworkProtocol.GameStateUpdate) {
            handleGameStateUpdate((NetworkProtocol.GameStateUpdate) obj);
        }
        else if (obj instanceof NetworkProtocol.LobbyUpdate) {
            handleLobbyUpdate((NetworkProtocol.LobbyUpdate) obj);
        }
        else if (obj instanceof String) {
            String message = (String) obj;
            if (message.equals("GAME_START")) {
                listener.onGameStarted();
            } else {
                listener.onMessage(message);
            }
        }
    }

    /**
     * Handle update of game from {@link GameServer}.
     * @param state instance of {@link NetworkProtocol.GameStateUpdate},
     *              contains current state of {@link entity.object.GameObject} in game.
     */
    private void handleGameStateUpdate(NetworkProtocol.GameStateUpdate state) {
        lastGameState = state;
        listener.onGameStateUpdated(state);
    }

    /**
     * Handle login response from {@link GameServer}.
     * If login success, connect to {@link GameServer}
     * @param loginResponse instance of {@link NetworkProtocol.LoginResponse}
     */
    private void handleLoginResponse(NetworkProtocol.LoginResponse loginResponse) {
        if (loginResponse.success) {
            connected = true;
            myPNumber = loginResponse.pNumber;
            listener.onConnected(myPNumber);
            System.out.println("Connected as Player " + myPNumber);
        } else {
            System.out.println("Connection failed: " + loginResponse.message);
            listener.onDisconnected(loginResponse.message);
        }
    }

    /**
     * Handle {@link Menu.NetworkLobby} update.
     * Client listen and handle it.
     * @param update instance of {@link NetworkProtocol.LobbyUpdate}
     */
    private void handleLobbyUpdate(NetworkProtocol.LobbyUpdate update) {
        listener.onLobbyUpdate(update);
    }

    /**
     * Handle disconnect.
     */
    private void handleDisconnect() {
        connected = false;
        listener.onDisconnected("Connection lost");
    }

    public boolean isConnected() {
        return connected;
    }
    public void disconnect() {
        if (connected) {
            client.close();
            connected = false;
        }
    }
    public int getMyPNumber() {
        return myPNumber;
    }
    public NetworkProtocol.GameStateUpdate getLastGameState() {
        return lastGameState;
    }

    public void setListener(GameClientListener listener) {
        this.listener = listener;
    }
}
