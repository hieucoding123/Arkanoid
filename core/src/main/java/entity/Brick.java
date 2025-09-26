package entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Brick extends MovableObject {
    public Brick(int x, int y, int w, int h, SpriteBatch batch, String path) {
        super(x, y, w, h, batch, path);
    }
}
