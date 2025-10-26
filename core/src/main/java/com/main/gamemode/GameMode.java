package com.main.gamemode;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.main.input.IngameInputHandler;
import entity.Player;
import entity.object.Paddle;

public abstract class GameMode {
    private Player player;
    private boolean isEnd;

    protected IngameInputHandler inputHandler;

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
    public abstract Paddle getPaddle2();
    public abstract void launchBall();
    public abstract void isStart(boolean start);

    public abstract void create();
    public abstract void update(float delta);
    public abstract void render(SpriteBatch sp, float delta);
    public abstract void handleInput();
    public abstract void draw(SpriteBatch sp);
}
