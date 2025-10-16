package entity.GameMode;

import entity.Ball;
import entity.BricksMap;
import entity.Paddle;

import java.util.ArrayList;

public abstract class GameMode {
    public BricksMap bricksMap;
    public ArrayList<Ball> balls;
    public Paddle paddle;

    public abstract void create();
    public abstract void update();
    public abstract void render();
    public abstract void draw();
}
