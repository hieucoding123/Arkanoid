package entity.gamemode;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import entity.Ball;
import entity.Paddle;

import java.util.ArrayList;

public abstract class GameMode {
    public ArrayList<Ball> balls;
    public ArrayList<Paddle> paddles;

    public GameMode() {
        balls = new ArrayList<>();
        paddles = new ArrayList<>();
    }

    public abstract void create();
    public abstract void update();
    public abstract void render(SpriteBatch sp);
    public abstract void handleInput();
    public abstract void draw(SpriteBatch sp);
}
