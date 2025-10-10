package entity;

import com.badlogic.gdx.graphics.Texture;
import entity.EffectItem;
import entity.Paddle;

public class ExpandEffect extends EffectItem {
    private static final float EFFECT_DURATION = 5.0f;
    private Paddle paddle;

    public ExpandEffect(float x, float y, float dy, Texture texture, Paddle paddle) {
        super(x, y, dy, texture);
        this.paddle = paddle;
    }

    public void applyEffect() {
        if (paddle != null) {
            paddle.activateExpand(EFFECT_DURATION);
        }

        this.setDestroyed(true);
    }
}
