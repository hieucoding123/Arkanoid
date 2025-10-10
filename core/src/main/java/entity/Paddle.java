package entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.main.Main;

public class Paddle extends MovableObject {
    private long expandEnd = 0;
    private float originalWidth;
    public Paddle(float x, float y, Texture texture) {
        super(x, y, texture);
        this.speed = 2.5f;
    }

    public void activateExpand(float duration) {
        this.expandEnd = System.currentTimeMillis() + (long)(duration * 1000);
        this.setScale(1.5f);
    }

    public void update() {
        super.update();

        if (expandEnd > 0 && System.currentTimeMillis() > expandEnd) {
            this.setScale(1.0f);
            this.expandEnd = 0;
        }
    }

    public void moveRight() {
        x += speed;
        if (x + getWidth() > Main.SCREEN_WIDTH - Main.padding_left_right) {
            x = Main.SCREEN_WIDTH - Main.padding_left_right - getWidth();
        }
    }

    public void moveLeft() {
        x -= speed;
        if (x < Main.padding_left_right) {
            x = Main.padding_left_right;
        }
    }
}
