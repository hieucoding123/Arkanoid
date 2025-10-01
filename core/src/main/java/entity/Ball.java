package entity;

import com.badlogic.gdx.graphics.Texture;


public class Ball extends MovableObject {
    private float speed;
    private float directionX;
    private float directionY;

    public Ball(float x, float y, float w, float h, Texture texture, float speed) {
        super(x, y, w, h, texture);
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

    }
}
