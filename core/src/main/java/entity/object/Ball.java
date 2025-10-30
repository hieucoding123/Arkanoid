package entity.object;

import com.badlogic.gdx.graphics.Texture;
import com.main.Game;
import entity.Effect.ShieldEffect;
import entity.MovableObject;

public class Ball extends MovableObject {
    private float speed;
    private float angle;
    private long BigEnd = 0;
    private long SlowEnd = 0;
    private long FastEnd = 0;
    private float originalspeed;
    private int lastHitBy = 0;

    private float baseScale;

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

        //Rescale
        this.baseScale = 36.0f / this.orgWidth;
        this.setScale(this.baseScale, this.baseScale);
    }

    public Ball(Ball other) {
        super(other.x, other.y, other.texture);
        this.originalspeed = other.originalspeed;
        this.speed = other.speed;
        this.angle = other.angle - 0.3f;
        this.scaleWidth = other.scaleWidth;
        this.scaleHeight = other.scaleHeight;

        this.baseScale = other.baseScale;

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

    public void setSpeed(float newSpeed) {
        this.speed = newSpeed;
        updateVelocity();
    }

    public void activateBig(float duration) {
        BigEnd = System.currentTimeMillis() + (long)(duration * 1000);
//        this.setScale(1.5f, 1.5f);
        this.setScale(this.baseScale * 1.5f, this.baseScale * 1.5f);
    }

    public void activateSlow(float duration) {
        if (FastEnd > 0) {
            this.setSpeed(originalspeed * 60f);
            FastEnd = 0;
        }
        SlowEnd = System.currentTimeMillis() + (long)(duration * 1000);
        this.setSpeed(180f);
    }

    public void activateFast(float duration) {
        if (SlowEnd > 0) {
            this.setSpeed(originalspeed * 60f);
            SlowEnd = 0;
        }
        FastEnd = System.currentTimeMillis() + (long)(duration * 1000);
        this.setSpeed(1000f);
    }

    public void update(float delta) {
        super.update(delta);
        if (this.getX() <= Game.padding_left_right
            || this.getX() + this.getWidth() >= Game.SCREEN_WIDTH - Game.padding_left_right) {
            this.reverseX();
        }
        if (this.getY() + this.getHeight() >= Game.padding_top) {
            this.reverseY();
        }
        if (this.getY() <= 0) {
            if (ShieldEffect.isShield()) {
                ShieldEffect.setShield();
                this.reverseY();
            } else {
                this.setDestroyed(true); // drop out of screen
            }
        }
        if (BigEnd > 0 && System.currentTimeMillis() > BigEnd) {
//            this.setScale(1.0f, 1.0f);
            this.setScale(this.baseScale, this.baseScale);
            BigEnd = 0;
        }
        if (SlowEnd > 0 && System.currentTimeMillis() > SlowEnd) {
            this.setSpeed(originalspeed * 60f);
            SlowEnd = 0;
        }
        if (FastEnd > 0 && System.currentTimeMillis() > FastEnd) {
            this.setSpeed(originalspeed * 60f);
            FastEnd = 0;
        }
    }

    public void collisionWith(Paddle paddle) {
        if (paddle.isFlipped()) {
            if (this.getDy() > 0) {
                float paddleCenter = paddle.getX() + paddle.getWidth() / 2f;
                float ballCenter = this.getX() + this.getWidth() / 2f;
                float impactPoint = (ballCenter - paddleCenter) / (paddle.getWidth() / 2f);
                impactPoint = Math.max(-1.0f, Math.min(1.0f, impactPoint));

                float newAngle = -((float)Math.PI / 2 - impactPoint * (float)Math.PI / 3f);

                this.setAngle(newAngle);
                this.updateVelocity();

                this.setY(paddle.getY() - this.getHeight());
            }
        } else {
            if (this.getDy() < 0 &&
                this.getX() < paddle.getX() + paddle.getWidth() &&
                this.getX() + this.getWidth() > paddle.getX() &&
                this.getY() <= paddle.getY() + paddle.getHeight() &&
                this.getY() + this.getHeight() >= paddle.getY()) {

                float paddleCenter = paddle.getX() + paddle.getWidth() / 2f;
                float ballCenter = this.getX() + this.getWidth() / 2f;
                float impactPoint = (ballCenter - paddleCenter) / (paddle.getWidth() / 2f);

                impactPoint = Math.max(-1.0f, Math.min(1.0f, impactPoint));
                float newAngle = (float) (Math.PI / 2 - impactPoint * (float) Math.PI / 3f);

                this.setAngle(newAngle);
                this.updateVelocity();
                this.setY(paddle.getY() + paddle.getHeight());
            }
        }
    }

    public boolean isBig() {
        if (System.currentTimeMillis() <= BigEnd) {
            return true;
        } else {
            return false;
        }
    }

    public long getTimeBigWEffect() {
        return BigEnd - System.currentTimeMillis();
    }

    public long getTimeSlowEffect() {
        return SlowEnd - System.currentTimeMillis();
    }

    public float getAngle() { return this.angle; }

    public void setLastHitBy(int playerNumber) {
        this.lastHitBy = playerNumber;
    }

    public int getLastHitBy() {
        return this.lastHitBy;
    }
}
