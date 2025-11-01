package entity.object;

import com.badlogic.gdx.graphics.Texture;
import com.main.Game;
import entity.Effect.ShieldEffect;
import entity.MovableObject;
import entity.object.brick.Brick;

public class Ball extends MovableObject {
    private float speed;
    private float angle;
    private long BigEnd = 0;
    private long SlowEnd = 0;
    private long FastEnd = 0;
    private float originalspeed;
    private int lastHitBy = 0;

    private float baseScale;

    // Offset
    private static final float HITBOX_SHRINK_PIXELS = 1.0f;

    private boolean in1v1 = false;
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

    public boolean isIn1v1() {
        return this.in1v1;
    }

    public void setIn1v1(boolean in1v1) {
        this.in1v1 = in1v1;
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
//        if (this.getX() <= Game.padding_left_right
//            || this.getX() + this.getWidth() >= Game.SCREEN_WIDTH - Game.padding_left_right) {
//            this.reverseX();
//            Game.playSfx(Game.sfx_touchpaddle,1.2f);
//        }
//        if (this.getY() + this.getHeight() >= Game.padding_top) {
//            if (isIn1v1()) {
//                if (ShieldEffect.isShield()) {
//                    ShieldEffect.setShield();
//                    this.reverseY();
//                    Game.playSfx(Game.sfx_touchpaddle,1.2f);
//                } else {
//                    this.setDestroyed(true);
//                }
//            } else {
//                this.reverseY();
//                Game.playSfx(Game.sfx_touchpaddle,1.2f);
//            }
//        }
//        if (this.getY() <= 0) {
//            if (ShieldEffect.isShield()) {
//                ShieldEffect.setShield();
//                this.reverseY();
//                Game.playSfx(Game.sfx_touchpaddle,1.2f);
//            } else {
//                this.setDestroyed(true); // drop out of screen
//            }
//        }
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
    public void handleWallCollision() {
        boolean collided = false;
        // Va chạm tường trái hoặc phải
        if (this.getX() <= Game.padding_left_right) {
            this.setX(Game.padding_left_right); // Đẩy bóng ra
            this.reverseX();
            collided = true;
            angleSpeedAdjustment("VERTICAL");
        } else if (this.getX() + this.getWidth() >= Game.SCREEN_WIDTH - Game.padding_left_right) {
            this.setX(Game.SCREEN_WIDTH - Game.padding_left_right - this.getWidth()); // Đẩy bóng ra
            this.reverseX();
            collided = true;
            angleSpeedAdjustment("VERTICAL");
        }
        // Va chạm tường trên
        if (this.getY() + this.getHeight() >= Game.padding_top) {
            this.setY(Game.padding_top - this.getHeight()); // Đẩy bóng ra
            this.reverseY();
            collided = true;
            angleSpeedAdjustment("HORIZONTAL");
            if (this.isIn1v1())
                this.setDestroyed(true);
        }

        // Va chạm đáy màn hình
        if (this.getY() <= 0) {
            if (ShieldEffect.isShield()) {
                ShieldEffect.setShield();
                this.setY(0); // Đẩy bóng ra
                this.reverseY();
                collided = true;
                angleSpeedAdjustment("HORIZONTAL");
            } else {
                this.setDestroyed(true); // Rớt ra ngoài
            }
            if (isIn1v1())
                this.setDestroyed(true);
        }


        if (collided) {
            Game.playSfx(Game.sfx_touchpaddle, 1.2f);
        }
    }

    public void collisionWith(Paddle paddle) {
        if (!this.checkCollision(paddle)) {
            return;
        }
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
                Game.playSfx(Game.sfx_touchpaddle,1.2f);
                angleSpeedAdjustment("HORIZONTAL");
            }
        } else {
            if (this.getDy() < 0) {
                float paddleCenter = paddle.getX() + paddle.getWidth() / 2f;
                float ballCenter = this.getX() + this.getWidth() / 2f;
                float impactPoint = (ballCenter - paddleCenter) / (paddle.getWidth() / 2f);

                impactPoint = Math.max(-1.0f, Math.min(1.0f, impactPoint));
                float newAngle = (float) (Math.PI / 2 - impactPoint * (float) Math.PI / 3f);

                this.setAngle(newAngle);
//                this.updateVelocity();
                this.setY(paddle.getY() + paddle.getHeight());
                Game.playSfx(Game.sfx_touchpaddle,1.2f);
                angleSpeedAdjustment("HORIZONTAL");
            }
        }
    }

    public boolean handleBrickCollision(Brick brick) {
        if (brick.isDestroyed() || !this.checkCollision(brick)) {
            return false;
        }

        float vecX = this.getCenterX() - brick.getCenterX();
        float vecY = this.getCenterY() - brick.getCenterY();

        float combinedHalfWidths = this.getWidth() / 2f + brick.getWidth() / 2f;
        float combinedHalfHeights = this.getHeight() / 2f + brick.getHeight() / 2f;

        float overlapX = combinedHalfWidths - Math.abs(vecX);
        float overlapY = combinedHalfHeights - Math.abs(vecY);

        if (overlapX < overlapY) {
            this.reverseX();
            if (vecX > 0) {
                this.setX(this.getX() + overlapX);
            } else {
                this.setX(this.getX() - overlapX);
            }
            angleSpeedAdjustment("VERTICAL");
        } else {
            this.reverseY();
            if (vecY > 0) {
                this.setY(this.getY() + overlapY);
            } else {
                this.setY(this.getY() - overlapY);
            }
            angleSpeedAdjustment("HORIZONTAL");
        }

        return true;
    }

    public void angleSpeedAdjustment(String surface) {
        if (SlowEnd > 0 || FastEnd > 0) {
            return;
        }

        float component = 1.0f;
        if (surface.equals("VERTICAL")) {
            component = Math.abs((float)Math.cos(this.angle));
        }else if (surface.equals("HORIZONTAL")) {
            component = Math.abs((float)Math.sin(this.angle));
        }
        float speedMultiplier = 1.0f + (1.0f - 0.5f) * (1.0f - component);

        float maxSpeed = (originalspeed * 60f) * 2.0f;
        float newSpeed = (originalspeed * 60f) * speedMultiplier;


        if (newSpeed > this.speed && newSpeed <= maxSpeed) {
            this.setSpeed(newSpeed);
        } else if (newSpeed < this.speed) {
            this.setSpeed(newSpeed);
        }
    }

    public void clearEffects() {
        this.BigEnd = 0;
        this.SlowEnd = 0;
        this.FastEnd = 0;
        this.setScale(this.baseScale, this.baseScale);
        this.setSpeed(originalspeed * 60f);
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

    public long getTimeFastEffect() {
        if (FastEnd > 0 && System.currentTimeMillis() <= FastEnd) {
            return FastEnd - System.currentTimeMillis();
        }
        return 0;
    }

    public float getOriginalSpeed() {
        return this.originalspeed;
    }

    public long getBigEnd() {
        return this.BigEnd;
    }

    public long getSlowEnd() {
        return this.SlowEnd;
    }

    public long getFastEnd() {
        return this.FastEnd;
    }

    public void setBigEnd(long bigEnd) {
        this.BigEnd = bigEnd;
    }

    public void setSlowEnd(long slowEnd) {
        this.SlowEnd = slowEnd;
    }

    public void setFastEnd(long fastEnd) {
        this.FastEnd = fastEnd;
    }

    /**
     * @return The x-coordinate of the ball's center.
     */
    public float getCenterX() {
        return getX() + getWidth() / 2f;
    }

    /**
     * @return The y-coordinate of the ball's center.
     */
    public float getCenterY() {
        return getY() + getHeight() / 2f;
    }

    /**
     * @return The radius of the ball.
     */
    public float getRadius() {
        return Math.max(1.0f, (getWidth() / 2f) - HITBOX_SHRINK_PIXELS);
    }

    /**
     * Gets the ball's current horizontal velocity.
     * @return dx
     */
    public float getDx() {
        return this.dx;
    }

    /**
     * Gets the ball's current vertical velocity.
     * @return dy
     */
    public float getDy() {
        return this.dy;
    }

    /**
     * Sets the velocity and updates the internal angle and speed fields to match.
     */
    public void setVelocityAndUpdateAngle(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
        this.angle = (float) Math.atan2(dy, dx);
        this.speed = (float) Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * @return The ball's current speed (pixels per second).
     */
    public float getSpeed() {
        return this.speed;
    }
}
