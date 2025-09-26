package entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MovableObject extends GameObject {
    private int speed;
    public MovableObject(int x, int y, int w, int h, SpriteBatch batch, String path) {
        super(x, y, w, h, batch, path);
    }

    public void update() {

    }
}
