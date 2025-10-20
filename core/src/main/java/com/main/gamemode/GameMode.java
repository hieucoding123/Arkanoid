package com.main.gamemode;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class GameMode {

    public abstract void create();
    public abstract void update(float delta);
    public abstract void render(SpriteBatch sp, float delta);
    public abstract void handleInput();
    public abstract void draw(SpriteBatch sp);
}
