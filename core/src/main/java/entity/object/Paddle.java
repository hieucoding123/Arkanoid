package entity.object;
import com.badlogic.gdx.graphics.Texture;
import com.main.Game;
import entity.MovableObject;

public class Paddle extends MovableObject {
    private long expandEnd = 0;

    public Paddle(float x, float y, Texture texture) {
        super(x, y, texture);
        this.speed = 2.5f;
    }

    public void activateExpand(float duration) {
        this.expandEnd = System.currentTimeMillis() + (long)(duration * 1000);
        this.setScale(1.5f, 1.0f);
    }

    public void update(float delta) {
        super.update(delta);
        if (expandEnd > 0 && System.currentTimeMillis() > expandEnd) {
            this.setScale(1.0f, 1.0f);
            this.expandEnd = 0;
        }
    }

    public long getTimeExpandEffect() {
        return expandEnd -  System.currentTimeMillis();
    }

    public void moveRight() {
        x += speed;
        if (x + getWidth() > Game.SCREEN_WIDTH - Game.padding_left_right) {
            x = Game.SCREEN_WIDTH - Game.padding_left_right - getWidth();
        }
    }

    public void moveLeft() {
        x -= speed;
        if (x < Game.padding_left_right) {
            x = Game.padding_left_right;
        }
    }
}
