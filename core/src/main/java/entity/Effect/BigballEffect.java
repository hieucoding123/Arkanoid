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
        // Áp dụng hiệu ứng cho TẤT CẢ các quả bóng
        for (Ball ball : main.balls) {
            ball.activateBig(EFFECT_DURATION);
        }
        this.setDestroyed(true);
    }
}
