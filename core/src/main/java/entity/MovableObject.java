package entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public abstract class MovableObject extends GameObject {
    protected float dx;
    protected float dy;
    public float speed;
    /**
     * Constructor for MoveableObject.
     * @param x x coordinate
     * @param y y coordinate
     * @param texture texture for object
     */
    public MovableObject(float x, float y, Texture texture) {
        super(x, y, texture);
        this.dx = 0;
        this.dy = 0;
    }

    /**
     * Constructor for MoveableObject.
     * @param x x coordinate
     * @param y y coordinate
     * @param scale object's scale
     * @param texture object texture
     */
    public MovableObject(float x, float y, float scale, Texture texture) {
        super(x, y, scale, texture);
        this.dx = 0;
        this.dy = 0;
    }

    /**
     * Set object's velocity
     * @param dx velocity in x-axis
     * @param dy velocity in y-axis
     */
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

    public float getDy() {
        return this.dy;
    }

    public float getDx() {
        return this.dx;
    }

    public float getSpeed() {
        return this.speed;
    }

    public void setDy(float dy) {
        this.dy = dy;
    }

    public void setDx(float dx) {
        this.dx = dx;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
