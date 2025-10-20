package entity.Effect;

import com.main.gamemode.GameMode;
import entity.object.Ball;
import entity.TextureManager;
import entity.object.Paddle;

import java.util.ArrayList;

public class SlowBallEffect extends EffectItem {
    private static final float EFFECT_DURATION = 20.0f;

    public SlowBallEffect(float x, float y, float dy) {
        super(x, y, dy, TextureManager.SlowBallTexture);
    }

    @Override
    public void applyEffect(Paddle paddle, ArrayList<Ball> balls) {
        // Áp dụng hiệu ứng cho TẤT CẢ các quả bóng
        for (Ball ball : balls) {
            ball.activateSlow(EFFECT_DURATION);
        }
        this.setDestroyed(true);
    }

}
