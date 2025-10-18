package entity.Effect;

import com.main.Main;
import entity.object.Ball;
import entity.TextureManager;

public class BigballEffect extends EffectItem {
    private static final float EFFECT_DURATION = 10.0f;

    public BigballEffect(float x, float y, float dy) {
        super(x, y, dy, TextureManager.BALLTexture);
    }

    @Override
    public void applyEffect(Main main) {
        for (Ball ball : main.balls) {
            ball.activateBig(EFFECT_DURATION);
            //Left wall
            if (ball.getX() < Main.padding_left_right) {
                ball.setX(Main.padding_left_right);
            }
            //Right wall
            if (ball.getX() + ball.getWidth() > Main.SCREEN_WIDTH - Main.padding_left_right) {
                ball.setX(Main.SCREEN_WIDTH - Main.padding_left_right - ball.getWidth());
            }
            //Top wall
            if (ball.getY() + ball.getHeight() > Main.padding_top) {
                ball.setY(Main.padding_top - ball.getHeight());
            }
        }
        this.setDestroyed(true);
    }
}
