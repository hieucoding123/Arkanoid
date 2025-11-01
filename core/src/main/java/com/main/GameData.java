package com.main;

import java.util.ArrayList;

/**
 * Represents the complete game state including score, player data,
 * paddles, balls, bricks, and active effects.
 */
public class GameData {
    public double score;
    public int lives;
    public double timePlayed;
    public boolean followPaddle;
    public boolean isCoop;
    public int cnt_combo;
    public long TimeLastHit;
    public PaddleData paddle1;
    public PaddleData paddle2;
    public ArrayList<BallData> balls;
    public ArrayList<BrickData> bricks;
    public ArrayList<EffectItemData> effectItems;
    public Object levelNumber;

    /** Default constructor initializing empty lists for game objects. */
    public GameData() {
        balls = new ArrayList<>();
        bricks = new ArrayList<>();
        effectItems = new ArrayList<>();
    }

    /** Holds paddle state data used for saving or synchronization. */
    public static class PaddleData {
        public float x;
        public float y;
        public long expandEnd;
        public long StunEnd;
        public boolean shieldActive;
        public PaddleData() {}
    }

    /** Holds ball state data used for saving or synchronization. */
    public static class BallData {
        public float x;
        public float y;
        public float dx;
        public float dy;
        public float angle;
        public float speed;
        public float originalspeed;
        public long BigEnd;
        public long SlowEnd;
        public long FastEnd;
        public BallData() {}
    }

    /** Holds brick state data used for saving or synchronization. */
    public static class BrickData {
        public float x;
        public float y;
        public int hitPoints;
        public boolean isDestroyed;
        public boolean unbreak;
        public int row;
        public int col;
        public int color;
        public float dx;
        public float dy;
        public BrickData() {}
    }

    /** Holds effect item data used for saving or synchronization. */
    public static class EffectItemData {
        public float x;
        public float y;
        public float dx;
        public float dy;
        public String EffectType;
        public EffectItemData() {}
    }
}
