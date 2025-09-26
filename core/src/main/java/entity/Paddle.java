package entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Paddle extends GameObject {
    public Paddle(int x, int y, int w, int h, SpriteBatch batch, String path) {
        super(x, y, w, h, batch, path);
    }
}
