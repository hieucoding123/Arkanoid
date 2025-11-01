package com.main.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

import java.util.ArrayList;

/**
 * Client-Server protocol.
 * Contains packages exchanged between client and server.
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
