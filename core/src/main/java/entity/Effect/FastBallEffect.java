package entity.Effect;

import com.main.Game;
import com.main.gamemode.GameMode;
import entity.object.Ball;
import entity.TextureManager;
import entity.object.Paddle;
import entity.object.brick.Brick;
import entity.object.brick.BricksMap;

import java.util.ArrayList;

public class FastBallEffect extends EffectItem {
    private static final float EFFECT_DURATION = 20.0f;

    public FastBallEffect(float x, float y, float dy) {
        super(x, y, dy, TextureManager.FastBallTexture);
        this.triggeringBall = null;
    }

    public FastBallEffect(float x, float y, float dy, Ball triggeringBall) {
        super(x, y, dy, TextureManager.FastBallTexture);
        this.triggeringBall = triggeringBall;
    }
    @Override
    public void applyEffect(Paddle paddle, ArrayList<Ball> balls, BricksMap bricksMap) {
        Game.playSfx(Game.sfx_fastball,0.8f);
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
