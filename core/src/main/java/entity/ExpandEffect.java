package entity;

import com.badlogic.gdx.graphics.Texture;
import entity.EffectItem;
import entity.Paddle;

public class ExpandEffect extends EffectItem {
    private static final float EFFECT_DURATION = 10000f;
    private boolean applied = false;
    private long effectStartTime;
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
        effectStartTime = System.currentTimeMillis();
        paddle.setScale(1.5f);
    }

    public void update() {
        super.update();

        if (applied && (System.currentTimeMillis() - effectStartTime > EFFECT_DURATION)) {
            setScale(1.0f);
            setDestroyed(true);
        }
    }
}
