package entity;

public class BigballEffect extends EffectItem{
    private static final float EFFECT_DURATION = 10.0f;
    private Ball ball;

    public BigballEffect(float x, float y, float dy, Ball ball) {
        super(x, y, dy, TextureManager.BALLTexture);
        this.ball = ball;
    }
    public void applyEffect() {
        ball.activateBig(EFFECT_DURATION);
        this.setDestroyed(true);
    }
}
