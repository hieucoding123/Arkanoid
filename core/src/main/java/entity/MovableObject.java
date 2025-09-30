package entity;

import com.badlogic.gdx.graphics.Texture;

public class MovableObject extends GameObject {
    protected float dx;
    protected float dy;

    public MovableObject(float x, float y, float w, float h, Texture texture) {
        super(x, y, w, h, texture);
        this.dx = 0;
        this.dy = 0;
    }

    public void setVelocity(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
    }
    /**
     * Update on object.
     */
    public void update() {
        this.x += dx;
        this.y += dy;
    }
}
