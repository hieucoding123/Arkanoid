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
     * @param scaleWidth object's scaleWidth
     * @param scaleHeight object's scaleHeight
     * @param texture object texture
     */
    public MovableObject(float x, float y, float scaleWidth, float scaleHeight, Texture texture) {
        super(x, y, scaleWidth, scaleHeight, texture);
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
}
