package entity.object;

import com.badlogic.gdx.graphics.Texture;
import entity.MovableObject;

public class Ball extends MovableObject {
    private float speed;
    private float angle;
    private long BigEnd = 0;
    private long SlowEnd = 0;
    private float originalspeed;

    /**
     * Constructor for ball.
     * @param x x coordinate of ball
     * @param y y coordinate of ball
     * @param texture ball texture
     * @param speed ball speed
     */
    public Ball(float x, float y, Texture texture, float speed) {
        super(x, y, texture);
        this.originalspeed = speed;
        this.speed = speed * 60f;
        setRandomAngle();
    }

    public Ball(Ball other) {
        super(other.x, other.y, other.texture);
        this.originalspeed = other.originalspeed;
        this.speed = other.speed;
        this.angle = other.angle - 0.3f;
        this.scaleWidth = other.scaleWidth;
        this.scaleHeight = other.scaleHeight;


        if (other.BigEnd > System.currentTimeMillis()) {
            this.BigEnd = other.BigEnd;
        }
        if (other.SlowEnd > System.currentTimeMillis()) {
            this.SlowEnd = other.SlowEnd;
        }
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

    public float getAngle() { return this.angle; }

    public void setSpeed(float newSpeed) {
        this.speed = newSpeed;
        updateVelocity();
    }

    public void activateBig(float duration) {
        BigEnd = System.currentTimeMillis() + (long)(duration * 1000);
        this.setScale(1.5f, 1.5f);
    }

    public void activateSlow(float duration) {
        SlowEnd = System.currentTimeMillis() + (long)(duration * 1000);
        this.setSpeed(180f);
    }

    public void update(float delta) {
        super.update(delta);
        if (BigEnd > 0 && System.currentTimeMillis() > BigEnd) {
            this.setScale(1.0f, 1.0f);
            BigEnd = 0;
        }
        if (SlowEnd > 0 && System.currentTimeMillis() > SlowEnd) {
            this.setSpeed(originalspeed * 60f);
            SlowEnd = 0;
        }
    }

    public boolean isBig() {
        if (System.currentTimeMillis() <= BigEnd) {
            return true;
        } else {
            return false;
        }
    }
}
