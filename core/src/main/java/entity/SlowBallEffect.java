package entity;

public class SlowBallEffect extends EffectItem {
    private static final float EFFECT_DURATION = 20.0f;
    private Ball ball;

    public SlowBallEffect(float x, float y, float dy, Ball ball) {
        super(x, y, dy, TextureManager.SlowBallTexture);
        this.ball = ball;
    }

    public void applyEffect() {
        ball.activateSlow(EFFECT_DURATION);
        this.setDestroyed(true);
    }

}
