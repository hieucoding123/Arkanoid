package entity.Effect;

import com.main.Game;
import entity.object.Ball;
import entity.TextureManager;
import entity.object.Paddle;
import entity.object.brick.BricksMap;

import java.util.ArrayList;

public class StunPaddleEffect extends EffectItem {
    private static final float EFFECT_DURATION = 5.0f;

    public StunPaddleEffect(float x, float y, float dy) {
        super(x, y, dy, TextureManager.StunPaddleTexture);
    }

    @Override
    public void applyEffect(Paddle paddle, ArrayList<Ball> balls, BricksMap bricksMap) {
        Game.playSfx(Game.sfx_frozen,0.8f);
        if (paddle != null) {
            paddle.activateStun(EFFECT_DURATION);
        }
        this.setDestroyed(true);
    }

}
