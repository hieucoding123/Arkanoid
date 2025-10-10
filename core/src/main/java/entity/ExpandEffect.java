package entity;

import com.badlogic.gdx.graphics.Texture;
import entity.EffectItem;
import entity.Paddle;

public class ExpandEffect extends EffectItem {
    private static final float EFFECT_DURATION = 10000f;
    private boolean applied = false;
    private Paddle paddle;

    public ExpandEffect(float x, float y, float dy, Texture texture, Paddle paddle) {
        super(x, y, dy, texture);
        this.paddle = paddle;
    }

    public void applyEffect() {
        if (paddle == null || applied) {
            return;
        }

        applied = true;
        startTime = System.currentTimeMillis();
        paddle.setScale(1.5f);
    }

    public void update() {
        super.update();

        if (applied && (System.currentTimeMillis() - this.startTime > EFFECT_DURATION)) {
            paddle.setScale(1.0f);
            setDestroyed(true);
        }
    }
}
