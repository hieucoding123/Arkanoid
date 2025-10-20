package entity.Effect;

import com.main.gamemode.GameMode;
import entity.object.Ball;
import entity.TextureManager;

public class SlowBallEffect extends EffectItem {
    private static final float EFFECT_DURATION = 20.0f;

    public SlowBallEffect(float x, float y, float dy) {
        super(x, y, dy, TextureManager.SlowBallTexture);
    }

    @Override
    public void applyEffect(GameMode gameMode) {
        // Áp dụng hiệu ứng cho TẤT CẢ các quả bóng
        for (Ball ball : gameMode.balls) {
            ball.activateSlow(EFFECT_DURATION);
        }
        this.setDestroyed(true);
    }

}
