package entity.Effect;

import com.main.Game;
import entity.object.Ball;
import entity.object.Paddle;
import entity.TextureManager;
import entity.object.brick.Brick;
import entity.object.brick.BricksMap;
import java.util.ArrayList;
import java.util.Random;

/**
 * Removes all active effects from balls, paddle, and bricks when collected.
 */
public class RemoveEffect extends EffectItem {

    private static final Random rand = new Random();

    /**
     * Constructs a RemoveEffect instance.
     * @param x  the x-coordinate of the effect item
     * @param y  the y-coordinate of the effect item
     * @param dy the vertical velocity multiplier
     */
    public RemoveEffect(float x, float y, float dy) {
        super(x, y, dy, TextureManager.DeleteEffectTexture);
    }

    /**
     * Applies the remove effect, clearing all active effects from game objects.
     * @param paddle the player's paddle
     * @param balls the list of active balls
     * @param bricksMap the current brick map
     */
    @Override
    public void applyEffect(Paddle paddle, ArrayList<Ball> balls, BricksMap bricksMap) {
        Game.playSfx(Game.sfx_cleareffect,0.8f);
        EffectItem.clear();
        if (paddle != null) {
            paddle.clearEffects();
        }
        for (Ball ball : balls) {
            ball.clearEffects();
        }

        if (bricksMap != null) {
            for (Brick brick : bricksMap.getBricks()) { //
                brick.clearUnbreak();
            }
        }
        ShieldEffect.setShield();
        this.setDestroyed(true);
    }
}
