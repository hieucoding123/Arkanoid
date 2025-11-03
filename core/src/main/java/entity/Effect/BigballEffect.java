package entity.Effect;

import com.main.Game;
import entity.object.Ball;
import entity.TextureManager;
import entity.object.Paddle;
import entity.object.brick.BricksMap;

import java.util.ArrayList;

/**
 * Represents big ball effect.
 */
public class BigballEffect extends EffectItem {
    private static final float EFFECT_DURATION = 10.0f;

    /**
     * Creates a BigballEffect falling item with no specific triggering ball.
     *
     * @param x  the x-coordinate of the effect
     * @param y  the y-coordinate of the effect
     * @param dy the vertical velocity
     */
    public BigballEffect(float x, float y, float dy) {
        super(x, y, dy, TextureManager.BALLTexture);
        this.triggeringBall = null;
    }

    /**
     * Creates a BigballEffect associated with a specific ball.
     *
     * @param x the x-coordinate of the effect
     * @param y the y-coordinate of the effect
     * @param dy the vertical velocity
     * @param triggeringBall the ball that triggers the effect
     */
    public BigballEffect(float x, float y, float dy, Ball triggeringBall) {
        super(x, y, dy, TextureManager.BALLTexture);
        this.triggeringBall = triggeringBall;
    }

    /**
     * Applies the Big Ball effect by enlarging the triggering ball or all active balls.
     * Adjusts ball positions to ensure they remain within screen boundaries.
     *
     * @param paddle the player's paddle
     * @param balls the list of active balls
     * @param bricksMap the current map of bricks
     */
    @Override
    public void applyEffect(Paddle paddle, ArrayList<Ball> balls, BricksMap bricksMap) {
        Game.playSfx(Game.sfx_bigball, 0.8f);
//        if (this.triggeringBall != null) {
//            this.triggeringBall.activateBig(EFFECT_DURATION);
//        } else
        if (!balls.isEmpty()) {
            for (Ball ball : balls) {
                ball.activateBig(EFFECT_DURATION);
                // Left wall
                if (ball.getX() < Game.padding_left_right) {
                    ball.setX(Game.padding_left_right);
                }
                // Right wall
                if (ball.getX() + ball.getWidth() > Game.SCREEN_WIDTH - Game.padding_left_right) {
                    ball.setX(Game.SCREEN_WIDTH - Game.padding_left_right - ball.getWidth());
                }
                // Top wall
                if (ball.getY() + ball.getHeight() > Game.padding_top) {
                    ball.setY(Game.padding_top - ball.getHeight());
                }
            }
        }
        this.setDestroyed(true);
    }
}
