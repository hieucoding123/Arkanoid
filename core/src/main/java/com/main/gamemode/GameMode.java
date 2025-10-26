package com.main.gamemode;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.main.input.IngameInputHandler;
import entity.Player;
import entity.object.Ball;
import entity.object.Paddle;

import java.util.ArrayList;

public abstract class GameMode {
    private Player player;
    private boolean isEnd;

    protected IngameInputHandler inputHandler;
    protected boolean start = false;

    protected ArrayList<Ball> balls;
    protected boolean followPaddle = true;

    public GameMode() {
        balls = new ArrayList<>();
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    public abstract Paddle getPaddle1();
    public Paddle getPaddle2() {
        return null;
    }

    public void launchBall() {
        if (followPaddle) {
            followPaddle = false;
            if (!balls.isEmpty()) {
                balls.get(0).updateVelocity();
            }
        }
    }

    public void isStart(boolean start) {
        if (!this.start) {
            this.start = start;
        }
    }

    public abstract void create();
    public abstract void update(float delta);
    public abstract void render(SpriteBatch sp, float delta);
    public abstract void handleInput();
    public abstract void draw(SpriteBatch sp);
}
