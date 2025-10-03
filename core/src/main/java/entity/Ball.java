package entity;

import com.badlogic.gdx.graphics.Texture;
import com.main.Main;


public class Ball extends MovableObject {
    private float speed;
    private float directionX;
    private float directionY;

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
        this.directionX = 1;
        this.directionY = -1;
        updateVelocity();
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
        this.directionX = 1;
        this.directionY = -1;
        updateVelocity();
    }

    /**
     * Update ball velocity.
     */
    public void updateVelocity() {
        this.dx = speed * directionX;
        this.dy = speed * directionY;
    }

    /**
     * Bouncing physics with other object.
     * @param other other object
     */
    public void bounceOff(GameObject other) {
        if (!this.checkCollision(other)) {
            return;
        }

        float overlapX = (this.dx > 0) ? (this.getX() + this.getWidth() - other.getX())
            : (other.getX() + other.getWidth() - this.getX());
        float overlapY = (this.dy > 0) ? (this.getY() + this.getHeight() - other.getY())
            : (other.getY() + other.getHeight() - this.getY());

        if (overlapX < overlapY) {
            directionX *= -1;
        } else {
            directionY *= -1;
        }

        updateVelocity();
    }

    /**
     * Bouncing physics with screen.
     */
    void bounceScreen() {
        if (this.getX() <= 0 || this.getX() + this.getWidth() >= Main.SCREEN_WIDTH) {
            directionX *= -1;
            updateVelocity();
        }

        if (this.getY() + this.getHeight() >= Main.SCREEN_HEIGHT) {
            directionY *= -1;
            updateVelocity();
        }

        if (this.getY() <= 0) {
            //resetPosition();
            directionY *= -1;
            updateVelocity();
        }
    }

    /**
     * Reset ball position if fell out of screen
     */
    public void resetPosition() {
        //this.x = (paddle.getX() + paddle.getWidth()) / 2 - (this.getWidth() / 2);
        //this.y = paddle.getY() + paddle.getHeight();
        this.x = Main.SCREEN_WIDTH / 2f- this.getWidth() / 2f;
        this.y = 100; // Testing reset
    }

    /**
     * Update ball coordinate and movement.
     */
    public void update() {
        super.update();
        bounceScreen();
    }
}
