package entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;

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


    public void reset() {
        this.x = (Gdx.graphics.getWidth() / 2f) - (this.getWidth() / 2f);
        this.y = 120;
        setRandomAngle();
        this.dx = 0;
        this.dy = 0;
    }

    public void bounceOff(GameObject other) {
        float paddleCenter = other.getX() + other.getWidth() / 2f;
        float ballCenter = this.getX() + this.getWidth() / 2f;
        float hitPosition = ballCenter - paddleCenter;

        float normalizedPosition = hitPosition / (other.getWidth() / 2f);
        float maxBounceAngle = (float) Math.PI / 3f;
        float newAngle = (float) Math.PI / 2f - (normalizedPosition * maxBounceAngle);

        this.setAngle(newAngle);
    }
}
