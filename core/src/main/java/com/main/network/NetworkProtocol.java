package com.main.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import entity.object.brick.BricksMap;

import java.util.ArrayList;

public class NetworkProtocol {
    public static final int TCP_PORT = 6666;
    public static final int UDP_PORT = 6667;

    public static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();

        // register message classes
        kryo.register(BricksMap.class);
    }

    // MESSAGE CLASSES
    public static class LoginRequest {
        public String playerName;
        public GameMode gameMode;

        public LoginRequest() {}

        public LoginRequest(String pName, GameMode mode) {
            this.playerName = pName;
            this.gameMode = mode;
        }
    }
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
    // Clients send player input
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
    // Sever sends game state to all clients
    public static class GameStateUpdate {
        public ArrayList<PaddleState> paddles;
        public ArrayList<BallState> balls;
        public ArrayList<BrickState> bricks;
        public double p1Score;
        public double p2Score;
        public int p1Lives;
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
    // STATE CLASSES

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
    public static class BallState {
        public float x;
        public float y;
        public float width;
        public float height;
        public int lastHitBy;
        public boolean isDestroyed;

        public BallState() {}
    }
    public static class BrickState {
        public float x;
        public float y;
        public float width;
        public float height;
        public int hitPoints;
        public boolean isDestroyed;
        public int brickType;

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
