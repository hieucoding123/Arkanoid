package com.main.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import entity.object.brick.BrickType;

import java.util.ArrayList;

/**
 * Network protocol definitions for Arkanoid multiplayer.
 *
 * <p>This class defines the message format, data structures, and serialization
 * configuration for client-server communication. It uses the Kryo serialization
 * library (part of Kryonet) for efficient binary encoding.</p>
 *
 * <h2>Protocol Components:</h2>
 * <ul>
 *   <li><b>Message Classes:</b> Request/response objects for game actions</li>
 *   <li><b>State Classes:</b> Data structures for game entity states</li>
 *   <li><b>Enums:</b> Type-safe constants for game modes and input types</li>
 *   <li><b>Serialization:</b> Kryo registration for network transmission</li>
 * </ul>
 *
 * <h2>Message Types:</h2>
 * <table border="1">
 *   <tr>
 *     <th>Message</th>
 *     <th>Direction</th>
 *     <th>Purpose</th>
 *   </tr>
 *   <tr>
 *     <td>LoginRequest</td>
 *     <td>Client → Server</td>
 *     <td>Join game with player name</td>
 *   </tr>
 *   <tr>
 *     <td>LoginResponse</td>
 *     <td>Server → Client</td>
 *     <td>Connection result + player number</td>
 *   </tr>
 *   <tr>
 *     <td>PlayerInput</td>
 *     <td>Client → Server</td>
 *     <td>Send movement/action input</td>
 *   </tr>
 *   <tr>
 *     <td>GameStateUpdate</td>
 *     <td>Server → Clients</td>
 *     <td>Current game state (60 FPS)</td>
 *   </tr>
 *   <tr>
 *     <td>LobbyUpdate</td>
 *     <td>Server → Clients</td>
 *     <td>Lobby status (connections, ready)</td>
 *   </tr>
 * </table>
 *
 * <h2>Serialization Strategy:</h2>
 * <p>All message and state classes are registered with fixed IDs to ensure
 * compatibility between client and server:</p>
 * <ul>
 *   <li><b>Enums:</b> IDs 50-59</li>
 *   <li><b>Messages:</b> IDs 60-69</li>
 *   <li><b>State Objects:</b> IDs 70-79</li>
 * </ul>
 *
 * <h2>Version Compatibility:</h2>
 * <p>The protocol includes a version string ({@code PROTOCOL_VERSION = "1.0.0"})
 * to prevent mismatched clients from connecting. Server rejects clients with
 * different versions.</p>
 *
 * <h2>Usage Example:</h2>
 * <pre>
 * // Server-side registration
 * Server server = new Server();
 * NetworkProtocol.register(server);  // Register all types
 *
 * // Client-side registration
 * Client client = new Client();
 * NetworkProtocol.register(client);  // Must match server
 *
 * // Sending a message
 * LoginRequest request = new LoginRequest("PlayerName", GameMode.VS);
 * connection.sendTCP(request);
 *
 * // Receiving a message
 * if (object instanceof LoginResponse) {
 *     LoginResponse response = (LoginResponse) object;
 *     System.out.println("Joined as Player " + response.pNumber);
 * }
 * </pre>
 *
 * <h2>Adding New Message Types:</h2>
 * <ol>
 *   <li>Define the message class with public fields (Kryo requirement)</li>
 *   <li>Add no-arg constructor (required by Kryo)</li>
 *   <li>Register in {@link #register(EndPoint)} with a unique ID</li>
 *   <li>Update both client and server with matching registrations</li>
 *   <li>Increment {@code PROTOCOL_VERSION} to prevent mismatches</li>
 * </ol>
 *
 * @see GameServer Server using this protocol
 * @see GameClient Client using this protocol
 * @see <a href="https://github.com/EsotericSoftware/kryo">Kryo Documentation</a>
 */

public class NetworkProtocol {
    public static final int TCP_PORT = 54555;
    public static final int UDP_PORT = 54777;

    public static final String PROTOCOL_VERSION = "1.0.0";

    /**
     * Register type of packages.
     * @param endPoint local end point.
     */
    public static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();

        kryo.setRegistrationRequired(true);

        // register message classes
        // Register with ID from 50
        // Enum: 50 - 59
        kryo.register(GameMode.class, 50);
        kryo.register(InputType.class, 51);
        // Message classes: 60 - 69
        kryo.register(LoginRequest.class, 60);
        kryo.register(LoginResponse.class, 61);
        kryo.register(GameStateUpdate.class, 62);
        kryo.register(PlayerInput.class, 63);
        kryo.register(StartGameRequest.class, 64);
        kryo.register(LobbyUpdate.class, 65);
        kryo.register(DiscoverServerRequest.class, 66);
        kryo.register(ServerInfoResponse.class, 67);
        // State classes: 70 -79
        kryo.register(BallState.class, 70);
        kryo.register(PaddleState.class, 71);
        kryo.register(BrickState.class, 72);
        kryo.register(BrickType.class, 73);
        // Java collections: 80-89
        kryo.register(ArrayList.class, 80);
        kryo.register(java.util.HashMap.class, 81);
    }

    // MESSAGE CLASSES

    /**
     * Login request contains protocolVersion.
     * And login information.
     */
    public static class LoginRequest {
        public String protocolVersion;
        public String playerName;
        public GameMode gameMode;

        public LoginRequest() {
        }

        public LoginRequest(String pName, GameMode mode) {
            protocolVersion = PROTOCOL_VERSION;
            this.playerName = pName;
            this.gameMode = mode;
        }
    }

    /**
     * Response package to client from a login request.
     */
    public static class LoginResponse {
        public boolean success;
        public int pNumber;
        public String  message;

        public LoginResponse() {}
        public LoginResponse(boolean success, int pName, String message) {
            this.success = success;
            this.pNumber = pName;
            this.message = message;

        }
    }

    /**
     * Keyboard input to control object in game.
     */
    public static class PlayerInput {
        public int pNumber;
        public InputType inputType;
        public long timestamp;

        public PlayerInput() {}
        public PlayerInput(int pNumber, InputType inputType) {
            this.pNumber = pNumber;
            this.inputType = inputType;
            this.timestamp = System.currentTimeMillis();
        }
    }

    /**
     * Contains all game current state update.
     */
    public static class GameStateUpdate {
        public ArrayList<PaddleState> paddles;
        public ArrayList<BallState> balls;
        public ArrayList<BrickState> bricks;
        public double p1Score;
        public double p2Score;
        public int p1Wins;
        public int p2Wins;
        public int p2Lives;
        public float roundTimer;
        public int currentRound;
        public boolean isGameOver;
        public long timestamp;

        public GameStateUpdate() {
            paddles = new ArrayList<>();
            balls = new ArrayList<>();
            bricks = new ArrayList<>();
            timestamp = System.currentTimeMillis();
        }
    }
    public static class StartGameRequest {
        public int pNumber;

        public StartGameRequest() {}
        public StartGameRequest(int pNumber) {
            this.pNumber = pNumber;
        }
    }
    public static class LobbyUpdate {
        public boolean p1Connected;
        public boolean p2Connected;
        public boolean p1Ready;
        public boolean p2Ready;

        public LobbyUpdate() {}
        public LobbyUpdate (
            boolean p1Connected, boolean p2Connected,
            boolean p1Ready, boolean p2Ready
        ) {
            this.p1Connected = p1Connected;
            this.p2Connected = p2Connected;
            this.p1Ready = p1Ready;
            this.p2Ready = p2Ready;
        }
    }

    public static class DiscoverServerRequest {
        public DiscoverServerRequest() {}
    }

    /**
     * To server response information of server.
     */
    public static class ServerInfoResponse {
        public String hostName;
        public int currentPlayers;
        public int maxPlayers;
        public ServerInfoResponse() {}
    }
    // STATE CLASSES

    /**
     * State of game paddle.
     */
    public static class PaddleState {
        public int pNumber;
        public float x;
        public float y;
        public float width;
        public float height;

        public PaddleState() {}
        public PaddleState(int pNumber, float x, float y, float w, float h) {
            this.pNumber = pNumber;
            this.x = x;
            this.y = y;
            this.width = w;
            this.height = h;
        }
    }

    /**
     * State of ball.
     */
    public static class BallState {
        public float x;
        public float y;
        public float width;
        public float height;
        public int lastHitBy;
        public boolean isDestroyed;

        public BallState() {}
    }

    /**
     * State of brick.
     */
    public static class BrickState {
        public float x;
        public float y;
        public float width;
        public float height;
        public int hitPoints;
        public boolean isDestroyed;
        public boolean isExploding;
        public BrickType type;

        public BrickState() {}
    }

    // ENUMS
    public enum GameMode {
        COOP,
        VS
    }
    public enum InputType {
        MOVE_LEFT,
        MOVE_RIGHT,
        STOP,
        LAUNCH_BALL,
        PAUSE
    }
}
