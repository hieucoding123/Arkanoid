package entity.Effect;

import com.main.gamemode.GameMode;
import entity.object.Ball;
import entity.TextureManager;
import entity.object.Paddle;
import entity.object.brick.BricksMap;

import java.util.ArrayList;

public class SlowBallEffect extends EffectItem {
    private static final float EFFECT_DURATION = 20.0f;

    public SlowBallEffect(float x, float y, float dy) {
        super(x, y, dy, TextureManager.SlowBallTexture);
        this.triggeringBall = null;
    }

    public SlowBallEffect(float x, float y, float dy, Ball triggeringBall) {
        super(x, y, dy, TextureManager.SlowBallTexture);
        this.triggeringBall = triggeringBall;
    }

    @Override
    public void applyEffect(Paddle paddle, ArrayList<Ball> balls, BricksMap bricksMap) {
        if (paddle.isFlipped()) {
            this.setVelocity(0, -this.getDy());
        }

        if (this.triggeringBall != null) {
            this.triggeringBall.activateSlow(EFFECT_DURATION);
        } else {
            for (Ball ball : balls) {
                ball.activateSlow(EFFECT_DURATION);
            }
        }

        this.setDestroyed(true);
    }

}
