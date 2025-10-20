package com.main.gamemode;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import entity.object.Ball;

import java.util.ArrayList;

public abstract class GameMode {
    public ArrayList<Ball> balls;

    public GameMode() {
        balls = new ArrayList<>();
    }

    public abstract void create();
    public abstract void update(float delta);
    public abstract void render(SpriteBatch sp, float delta);
    public abstract void handleInput();
    public abstract void draw(SpriteBatch sp);
}
