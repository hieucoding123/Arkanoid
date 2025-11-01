package entity.Effect;

import com.main.Game;
import com.main.gamemode.GameMode;
import entity.object.Ball;
import entity.TextureManager;
import entity.object.Paddle;
import entity.object.brick.Brick;
import entity.object.brick.BricksMap;

import java.util.ArrayList;

/**
 * Increases the speed of all active balls for a limited duration.
 */
public class FastBallEffect extends EffectItem {
    private static final float EFFECT_DURATION = 20.0f;

    /**
     * Constructs a {@code FastBallEffect} without a triggering ball reference.
     * @param x the x-coordinate of the effect item
     * @param y the y-coordinate of the effect item
     * @param dy the vertical velocity multiplier
     */
    public FastBallEffect(float x, float y, float dy) {
        super(x, y, dy, TextureManager.FastBallTexture);
        this.triggeringBall = null;
    }

    /**
     * Constructs a fast ball effect tied to a specific ball.
     * @param x the x-coordinate of the effect item
     * @param y the y-coordinate of the effect item
     * @param dy the vertical velocity multiplier
     * @param triggeringBall the ball that triggered this effect
     */
    public FastBallEffect(float x, float y, float dy, Ball triggeringBall) {
        super(x, y, dy, TextureManager.FastBallTexture);
        this.triggeringBall = triggeringBall;
    }

    /**
     * Applies the fast-ball effect to either the triggering ball or all balls.
     * @param paddle    the player's paddle
     * @param balls     the list of active balls
     * @param bricksMap the current brick map
     */
    @Override
    public void applyEffect(Paddle paddle, ArrayList<Ball> balls, BricksMap bricksMap) {
        Game.playSfx(Game.sfx_fastball, 0.8f);
        if (triggeringBall != null) {
            this.triggeringBall.activateFast(EFFECT_DURATION);
        } else {
            for (Ball ball : balls) {
                ball.activateFast(EFFECT_DURATION);
            }
        }
        this.setDestroyed(true);
    }
}
