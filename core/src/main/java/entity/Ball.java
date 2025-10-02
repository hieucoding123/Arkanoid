package entity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.main.Main;


public class Ball extends MovableObject {
    private float speed;
    private float directionX;
    private float directionY;

    public Ball(float x, float y, Texture texture, float speed) {
        super(x, y, texture);
        this.speed = speed;
        this.directionX = 1;
        this.directionY = -1;
        updateVelocity();
    }

    public Ball(float x, float y, float scale, Texture texture, float speed) {
        super(x, y, scale, texture);
        this.speed = speed;
        this.directionX = 1;
        this.directionY = -1;
        updateVelocity();
    }

    public void updateVelocity() {
        this.dx = speed * directionX;
        this.dy = speed * directionY;
    }

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

    public void resetPosition() {
        //this.x = (paddle.getX() + paddle.getWidth()) / 2 - (this.getWidth() / 2);
        //this.y = paddle.getY() + paddle.getHeight();
        this.x = Main.SCREEN_WIDTH / 2f- this.getWidth() / 2f;
        this.y = 100; // Testing reset
    }

    public void update() {
        super.update();
        bounceScreen();
    }
}
