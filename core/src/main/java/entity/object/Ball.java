package entity.object;

import com.badlogic.gdx.graphics.Texture;
import com.main.Game;
import entity.Effect.ShieldEffect;
import entity.MovableObject;
import entity.ScoreManager;
import entity.object.brick.Brick;
import entity.object.brick.BricksMap;

public class Ball extends MovableObject {
    private float speed;
    private float angle;
    private static long BigEnd = 0;
    private static long SlowEnd = 0;
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
            this.setScale(1.0f, 1.0f);
            BigEnd = 0;
        }
        if (SlowEnd > 0 && System.currentTimeMillis() > SlowEnd) {
            this.setSpeed(2.0f);
            SlowEnd = 0;
        }
    }

    public void collisionWith(Paddle paddle) {
        if (this.getDy() < 0 &&
            this.getX() < paddle.getX() + paddle.getWidth() &&
            this.getX() + this.getWidth() > paddle.getX() &&
            this.getY() <= paddle.getY() + paddle.getHeight() &&
            this.getY() + this.getHeight() >= paddle.getY()) {

            float paddleCenter = paddle.getX() + paddle.getWidth() / 2f;
            float ballCenter = this.getX() + this.getWidth() / 2f;
            float impactPoint = (ballCenter - paddleCenter) / (paddle.getWidth() / 2f);

            impactPoint = Math.max(-1.0f, Math.min(1.0f, impactPoint));
            float newAngle = (float)(Math.PI / 2 - impactPoint * (float)Math.PI / 3f);

            this.setAngle(newAngle);
            this.updateVelocity();
            this.setY(paddle.getY() + paddle.getHeight());
        }
    }

    public void collisionWith(BricksMap bricksMap, ScoreManager scoreMng) {
        for (Brick brick : bricksMap.getBricks()) {
//            int hp = brick.gethitPoints();
            if (this.checkCollision(brick)) {
                brick.takeHit();
                if (this.isBig()) brick.setHitPoints(0);
                if (brick.gethitPoints() == 0) {
//                    callEffect(brick);
                    scoreMng.addScore(brick);
                    if (brick.getExplosion()) {
                        brick.startExplosion();
                    } else {
                        brick.setDestroyed(true);
                    }
                }
                float ballCenterX = this.getX() + this.getWidth() / 2f;
                float ballCenterY = this.getY() + this.getHeight() / 2f;
                //Bottom and top collision
                if (ballCenterX > brick.getX() && ballCenterX < brick.getX() + brick.getWidth()) {
                    this.reverseY();
                }
                //Left and right collision
                else if (ballCenterY > brick.getY() && ballCenterY < brick.getY() + brick.getHeight()) {
                    this.reverseX();
                }
                //Corner collision
                else {
                    this.reverseY();
                    this.reverseX();
                }
                break;
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

    public static long getTimeBigWEffect() {
        return BigEnd - System.currentTimeMillis();
    }

    public static long getTimeSlowEffect() {
        return SlowEnd - System.currentTimeMillis();
    }

    public float getAngle() { return this.angle; }
}
