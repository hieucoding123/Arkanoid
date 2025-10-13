package entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;

public class Ball extends MovableObject {
    private float speed;
    private float angle;
    private static long BigEnd = 0;
    private static long SlowEnd = 0;
    private float originalWidth;

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

//    public float getdy() {
//        return this.dy;
//    }

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
        this.setSpeed(1.0f);
    }

    public void update() {
        super.update();
        if (BigEnd > 0 && System.currentTimeMillis() > BigEnd) {
            this.setScale(1.0f, 1.0f);
            BigEnd = 0;
        }
        if (SlowEnd > 0 && System.currentTimeMillis() > SlowEnd) {
            this.setSpeed(2.0f);
            SlowEnd = 0;
        }
    }

    public static boolean isBig() {
        if (System.currentTimeMillis() <= BigEnd) {
            return true;
        } else {
            return false;
        }
    }
}
