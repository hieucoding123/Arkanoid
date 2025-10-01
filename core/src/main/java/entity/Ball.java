package entity;

import com.badlogic.gdx.graphics.Texture;


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

    public void update() {
        super.update();
    }
}
