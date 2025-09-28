package entity;

import com.badlogic.gdx.graphics.Texture;

public class Brick extends GameObject {
    private final static int WIDTH = 74;
    private final static int HEIGHT = 30;

    private boolean isDestroyed;

    public Brick(int x, int y, Texture texture) {
        super(x, y, WIDTH, HEIGHT, texture);
        isDestroyed = false;
    }
    public boolean isDestroyed() {
        return isDestroyed;
    }
    public void destroy() {
        isDestroyed = true;
    }
}
