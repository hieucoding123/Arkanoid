package entity;

import com.badlogic.gdx.graphics.Texture;

public abstract class MovableObject extends GameObject {
    protected float dx;
    protected float dy;

    public MovableObject(float x, float y, Texture texture) {
        super(x, y, texture);
        this.dx = 0;
        this.dy = 0;
    }

    public MovableObject(float x, float y, float scale, Texture texture) {
        super(x, y, scale, texture);
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
