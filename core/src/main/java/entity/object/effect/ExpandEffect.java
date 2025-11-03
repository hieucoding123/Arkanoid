package entity.object.effect;

import com.main.Game;
import entity.object.Ball;
import entity.object.Paddle;
import com.main.components.TextureManager;
import entity.object.brick.BricksMap;
import com.badlogic.gdx.Gdx;

import java.util.ArrayList;

/**
 * Expands the paddle size temporarily when collected.
 * The effect lasts for a fixed duration and ensures the paddle
 * remains within screen boundaries.
 */
public class ExpandEffect extends EffectItem {
    private static final float EFFECT_DURATION = 5.0f;

    /**
     * Constructs an {@code ExpandEffect} instance.
     * @param x  the x-coordinate of the effect item
     * @param y  the y-coordinate of the effect item
     * @param dy the vertical velocity multiplier
     */
    public ExpandEffect(float x, float y, float dy) {
        super(x, y, dy, TextureManager.expandpaddleTexture);
    }

    /**
     * Applies the expand effect to the paddle.
     * @param paddle    the player's paddle
     * @param balls     the list of active balls
     * @param bricksMap the current brick map
     */
    @Override
    public void applyEffect(Paddle paddle, ArrayList<Ball> balls, BricksMap bricksMap) {
        Game.playSfx(Game.sfx_bigpaddle, 0.8f);
        if (paddle != null) {
            paddle.activateExpand(EFFECT_DURATION);

            float screenWidth = Gdx.graphics.getWidth();
            if (paddle.getX() + paddle.getWidth() > screenWidth - BricksMap.xBeginCoord) {
                float newX = screenWidth - BricksMap.xBeginCoord - paddle.getWidth();
                paddle.setX(newX);
            }

            if (paddle.getX() < BricksMap.xBeginCoord) {
                paddle.setX(BricksMap.xBeginCoord);
            }
        }
        this.setDestroyed(true);
    }
}

