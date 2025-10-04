package entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;

public class Ball extends MovableObject {
    private float speed;
    private float angle;

    public Ball(float x, float y, float w, float h, Texture texture, float speed) {
        super(x, y, w, h, texture);
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
        // Để phản xạ qua trục Y (tường trái/phải), góc mới = PI - góc cũ
        this.angle = (float)Math.PI - this.angle;
        updateVelocity();
    }


    public void reverseY() {
        // Để phản xạ qua trục X (tường trên/dưới), góc mới = -góc cũ
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

    }
}
