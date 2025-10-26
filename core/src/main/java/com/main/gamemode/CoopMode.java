package com.main.gamemode;

import entity.Effect.EffectFactory;
import entity.GameScreen;
import entity.Player;
import entity.ScoreManager;
import entity.object.Ball;
import entity.object.Paddle;
import entity.object.brick.BricksMap;

import java.util.ArrayList;

public class CoopMode extends GameMode{
    private final ArrayList<BricksMap> bricksMaps;
    private final ArrayList<Ball> balls;
    boolean flowPaddle = true;      // Ball follow paddle
    private Paddle paddle1;
    private Paddle paddle2;
    private BricksMap currentMap;
    private ScoreManager scoreManager;
    GameScreen gameScreen;
    private EffectFactory effectFactory;
    private int levelNumber;
    private int mapIndex;
    private int lives;
    private double timePlayed;
    private boolean start = false;

    public CoopMode(Player player, ScoreManager scoreManager, GameScreen gameScreen, int levelNumber) {
        super();
        balls = new ArrayList<>();
        bricksMaps = new ArrayList<>();

        this.setPlayer(player);
        this.setEnd(false);
        this.scoreManager = scoreManager;
        this.gameScreen = gameScreen;
        this.effectFactory = new EffectFactory();
        this.levelNumber = levelNumber;
        this.timePlayed = 0.0f;
        create();
    }
}
