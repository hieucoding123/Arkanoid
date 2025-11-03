package entity.Effect;

import com.main.Game;
import entity.object.Ball;
import entity.TextureManager;
import entity.object.Paddle;
import entity.object.brick.BricksMap;

import java.util.ArrayList;

/**
 * Represents an effect that slows down one or more balls for a limited duration.
 */
public class SlowBallEffect extends EffectItem {
    private static final float EFFECT_DURATION = 5.0f;

    /**
     * Constructs a SlowBallEffect instance without a specific triggering ball.
     * @param x the x-coordinate of the effect
     * @param y the y-coordinate of the effect
     * @param dy the vertical velocity of the effect
     */
    public SlowBallEffect(float x, float y, float dy) {
        super(x, y, dy, TextureManager.SlowBallTexture);
        this.triggeringBall = null;
    }

    /**
     * Constructs a SlowBallEffect instance linked to a specific triggering ball.
     * @param x the x-coordinate of the effect
     * @param y he y-coordinate of the effect
     * @param dy the vertical velocity of the effect
     * @param triggeringBall the ball that triggered this effect
     */
    public SlowBallEffect(float x, float y, float dy, Ball triggeringBall) {
        super(x, y, dy, TextureManager.SlowBallTexture);
        this.triggeringBall = triggeringBall;
    }

    @Override
    public void applyEffect(Paddle paddle, ArrayList<Ball> balls, BricksMap bricksMap) {
        Game.playSfx(Game.sfx_slowball,0.8f);
        if (this.check1vs1 && this.triggeringBall != null) {
            this.triggeringBall.activateSlow(EFFECT_DURATION);
        } else {
            for (Ball ball : balls) {
                ball.activateSlow(EFFECT_DURATION);
            }
        }
        this.setDestroyed(true);
    }

}
