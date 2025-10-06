package entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.main.Main;

public class Paddle extends MovableObject {
    public Paddle(float x, float y, Texture texture) {
        super(x, y, texture);
        this.speed = 2.5f;
    }

    public Paddle(float x, float y, float scale, Texture texture) {
        super(x, y, scale, texture);
        this.speed = 3.0f;
    }

    public void moveRight() {
        x += speed;
        if (x + getWidth() > Main.SCREEN_WIDTH) {
            x = Main.SCREEN_WIDTH - getWidth();
        }
    }

    public void moveLeft() {
        x -= speed;
        if (x < 0) {
            x = 0;
        }
    }
}
