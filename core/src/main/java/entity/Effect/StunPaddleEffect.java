package entity.Effect;

import com.main.Game;
import entity.object.Ball;
import entity.TextureManager;
import entity.object.Paddle;
import entity.object.brick.BricksMap;

import java.util.ArrayList;

/**
 * Represents an effect that temporarily stuns the paddle, preventing it from moving.
 * When activated, it disables paddle control for a short duration.
 */
public class StunPaddleEffect extends EffectItem {
    private static final float EFFECT_DURATION = 1.0f;

    /**
     * Constructs a StunPaddleEffect instance.
     * @param x  the x-coordinate of the effect
     * @param y  the y-coordinate of the effect
     * @param dy the vertical velocity of the effect
     */
    public StunPaddleEffect(float x, float y, float dy) {
        super(x, y, dy, TextureManager.StunPaddleTexture);
    }

    /**
     * Applies the stun effect to the paddle, preventing movement for a limited time.
     * Plays a frozen sound effect and marks this effect as destroyed.
     * @param paddle    the paddle to apply the effect to
     * @param balls     the list of active balls (unused)
     * @param bricksMap the current bricks map (unused)
     */
    @Override
    public void applyEffect(Paddle paddle, ArrayList<Ball> balls, BricksMap bricksMap) {
        Game.playSfx(Game.sfx_frozen, 0.8f);
        if (paddle != null) {
            paddle.activateStun(EFFECT_DURATION);
        }
        this.setDestroyed(true);
    }
}
