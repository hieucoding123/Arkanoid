package com.main.gamemode;

import com.main.Game;
import entity.Effect.EffectFactory;
import entity.Effect.EffectItem;
import entity.GameScreen;
import entity.Player;
import entity.ScoreManager;
import entity.TextureManager;
import entity.object.Ball;
import entity.object.Paddle;
import entity.object.brick.BricksMap;

import java.util.ArrayList;

public class CoopMode extends GameMode{
    private final ArrayList<BricksMap> bricksMaps;
    private final ArrayList<Ball> balls;
    boolean followPaddle = true;      // Ball follow paddle
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

    @Override
    public void create() {
        gameScreen.create();

        for (int i = 1; i <= 5; i++) {
            String mapPath = "/maps_for_levelmode/map" + i + ".txt";
            bricksMaps.add(new BricksMap(mapPath));
        }
        mapIndex = this.levelNumber;

        if (mapIndex <= 0 || mapIndex > bricksMaps.size()) {
            mapIndex = 1;
        }
        currentMap = bricksMaps.get(mapIndex - 1);

        paddle1 = new Paddle(Game.SCREEN_WIDTH / 2f - 48, 50, TextureManager.paddleTexture);
        paddle2 = new Paddle(Game.SCREEN_WIDTH / 2f - 48, 40, TextureManager.paddleTexture);

        balls.add(new Ball(paddle1.getX() + paddle1.getWidth() / 2f - 12,
            paddle1.getY() + paddle1.getHeight(),
            TextureManager.ballTexture,
            5.0f)
        );
    }

    @Override
    public void update(float delta) {
        if (this.isEnd()) {
            return;
        }

        if (start) {
            this.timePlayed += delta;
        }

        currentMap.update(delta, this.scoreManager);
        paddle1.update(delta);
        paddle2.update(delta);
        EffectItem.updateEffectItems(paddle1, this.balls, delta);
        EffectItem.updateEffectItems(paddle2, this.balls, delta);

        if (balls.isEmpty()) {
            scoreManager.deduction();
            this.lives--;
            //this.reset();
        }

        if(followPaddle) {
            balls.get(0).setX(paddle1.getX() + paddle1.getWidth() / 2f);
            balls.get(0).setY(paddle1.getY() + paddle1.getHeight());
            balls.get(0).setAngle((float)Math.PI / 2f);
        }


    }
}
