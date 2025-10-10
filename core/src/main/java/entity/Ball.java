package entity;

import com.badlogic.gdx.graphics.Texture;

public class Ball extends MovableObject {
    private float speed;
    private float angle;

    /**
     * Constructor for ball.
     * @param x x coordinate of ball
     * @param y y coordinate of ball
     * @param texture ball texture
     * @param speed ball speed
     */
    public Ball(float x, float y, Texture texture, float speed) {
        super(x, y, texture);
        this.speed = speed;
        setRandomAngle();
    }

    /**
     * Constructor for ball.
     * @param x x coordinate of ball
     * @param y y coordinate of ball
     * @param scale ball scale
     * @param texture ball texture
     * @param speed ball speed
     */
    public Ball(float x, float y, float scale, Texture texture, float speed) {
        super(x, y, scale, texture);
        this.speed = speed;
        setRandomAngle();
    }

    private void setRandomAngle() {
        this.angle = (float) (Math.random() * (Math.PI / 2) + (Math.PI / 4));
    }

    public void updateVelocity() {
        this.dx = speed * (float)Math.cos(angle);
        this.dy = speed * (float)Math.sin(angle);
    }

    public void reverseX() {
        this.angle = (float)Math.PI - this.angle;
        updateVelocity();
    }

    public void reverseY() {
        this.angle = -this.angle;
        updateVelocity();
    }

    public void setAngle(float angleInRadians) {
        this.angle = angleInRadians;
        updateVelocity();
    }

    public float getAngle() {
        return this.angle;
    }

    public void setSpeed(float newSpeed) {
        this.speed = newSpeed;
        updateVelocity();
    }

}
