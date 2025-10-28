package entity.Effect;

import entity.object.Ball;
import entity.TextureManager;
import entity.object.Paddle;

import java.util.ArrayList;

public class StunPaddleEffect extends EffectItem {
    private static final float EFFECT_DURATION = 5.0f;

    public StunPaddleEffect(float x, float y, float dy) {
        super(x, y, dy, TextureManager.StunPaddleTexture);
    }

    @Override
    public void applyEffect(Paddle paddle, ArrayList<Ball> balls) {
        if (paddle != null) {
            paddle.activateStun(EFFECT_DURATION);
        }
        this.setDestroyed(true);
    }

}
