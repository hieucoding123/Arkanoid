package entity.Effect;

import entity.Paddle;
import entity.TextureManager;

public class ExpandEffect extends EffectItem {
    private static final float EFFECT_DURATION = 5.0f;
    private Paddle paddle;

    public ExpandEffect(float x, float y, float dy, Paddle paddle) {
        super(x, y, dy, TextureManager.expandpaddleTexture);
        this.paddle = paddle;
    }

    public void applyEffect() {
        if (paddle != null) {
            paddle.activateExpand(EFFECT_DURATION);
        }

        this.setDestroyed(true);
    }
}

