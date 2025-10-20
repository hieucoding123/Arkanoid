package com.main.gamemode;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import entity.Ball;
import entity.Paddle;

import java.util.ArrayList;

public abstract class GameMode {
    public ArrayList<Ball> balls;

    public GameMode() {
        balls = new ArrayList<>();
    }

    public abstract void create();
    public abstract void update();
    public abstract void render(SpriteBatch sp);
    public abstract void handleInput();
    public abstract void draw(SpriteBatch sp);
}
