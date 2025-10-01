package entity;

import com.badlogic.gdx.graphics.Texture;

public class Brick extends GameObject {
    private boolean isDestroyed;

    public Brick(float x, float y, Texture texture) {
        super(x, y, texture);
        isDestroyed = false;
    }

    public Brick(float x, float y, float scale, Texture texture) {
        super(x, y, scale, texture);
        isDestroyed = false;
    }
    public boolean isDestroyed() {
        return isDestroyed;
    }
    public void destroy() {
        isDestroyed = true;
    }
}
