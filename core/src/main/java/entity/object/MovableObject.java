package entity.object;

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
     * Set object's velocity
     * @param dx velocity in x-axis
     * @param dy velocity in y-axis
     */
    public void setVelocity(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Updates the object's position based on its velocity and the time delta.
     *
     * @param delta The time elapsed since the last frame, typically {@link Gdx#graphics#getDeltaTime()}.
     */
    public void update(float delta) {
        this.x += dx * delta;
        this.y += dy * delta;
    }

    /**
     * Gets the vertical velocity component.
     *
     * @return The velocity in the y-axis (dy).
     */
    public float getDy() {
        return this.dy;
    }

    /**
     * Gets the horizontal velocity component.
     *
     * @return The velocity in the x-axis (dx).
     */
    public float getDx() {
        return this.dx;
    }

    /**
     * Gets the speed of the object.
     *
     * @return The speed.
     */
    public float getSpeed() {
        return this.speed;
    }

    /**
     * Sets the vertical velocity component.
     *
     * @param dy The new velocity in the y-axis.
     */
    public void setDy(float dy) {
        this.dy = dy;
    }

    /**
     * Sets the horizontal velocity component.
     *
     * @param dx The new velocity in the x-axis.
     */
    public void setDx(float dx) {
        this.dx = dx;
    }

    /**
     * Sets the speed of the object.
     *
     * @param speed The new speed.
     */
    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
