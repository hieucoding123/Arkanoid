package entity.object;
import com.badlogic.gdx.graphics.Texture;
import com.main.Game;
import entity.MovableObject;

public class Paddle extends MovableObject {
    private boolean isFlipped;
    private long expandEnd = 0;
    private long StunEnd = 0;

    private boolean shieldActive = false;
    private float shieldTimer = 0;
    private static final float SHIELD_DURATION = 10.0f;

    public void setFlipped(boolean flipped) {
        this.isFlipped = flipped;
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public void activateShield() {
        this.shieldActive = true;
        this.shieldTimer = SHIELD_DURATION;
    }

    public void setShieldActive(boolean shieldActive) {
        this.shieldActive = shieldActive;
    }

    public boolean hasShield() {
        return this.shieldActive;
    }

    public Paddle(float x, float y, Texture texture) {
        super(x, y, texture);
        this.speed = 2.5f;
    }

    public Paddle(float x, float y, Texture texture, boolean isFlipped) {
        super(x, y, texture);
        this.speed = 2.5f;
        this.isFlipped = isFlipped;
    }

    public void activateExpand(float duration) {
        this.expandEnd = System.currentTimeMillis() + (long)(duration * 1000);
        this.setScale(1.5f, 1.0f);
    }

    public void activateStun(float duration) {
        this.StunEnd = System.currentTimeMillis() + (long)(duration * 1000);
    }

    public boolean isStunned() {
        return System.currentTimeMillis() < StunEnd;
    }

    public void update(float delta) {
        super.update(delta);
        if (expandEnd > 0 && System.currentTimeMillis() > expandEnd) {
            this.setScale(1.0f, 1.0f);
            this.expandEnd = 0;
        }

        if (shieldActive) {
            shieldTimer -= delta;
            if (shieldTimer <= 0) {
                this.setShieldActive(false);
            }
        }
    }

    public long getTimeExpandEffect() {
        return expandEnd -  System.currentTimeMillis();
    }

    public void moveRight() {
        if (isStunned()) {
            return;
        }
        x += speed;
        if (x + getWidth() > Game.SCREEN_WIDTH - Game.padding_left_right) {
            x = Game.SCREEN_WIDTH - Game.padding_left_right - getWidth();
        }
    }

    public void moveLeft() {
        if (isStunned()) {
            return;
        }
        x -= speed;
        if (x < Game.padding_left_right) {
            x = Game.padding_left_right;
        }
    }


}
