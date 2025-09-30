package entity;

import com.badlogic.gdx.graphics.Texture;

public class Brick extends GameObject {
    public final static int width = 74;
    public final static int height = 30;
    private boolean isDestroyed;

    public Brick(int x, int y, Texture texture) {
        super(x, y, width, height, texture);
        isDestroyed = false;
    }
    public boolean isDestroyed() {
        return isDestroyed;
    }
    public void destroy() {
        isDestroyed = true;
    }

    public void takeHit() {
        this.destroy();
    }
}
