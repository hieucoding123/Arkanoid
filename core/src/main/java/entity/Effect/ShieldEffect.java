package entity.Effect;

import entity.TextureManager;
import com.main.Main;

public class ShieldEffect extends EffectItem {
    private static boolean shield = false;
    public ShieldEffect(float x, float y, float dy) {
        super(x, y, dy, TextureManager.shieldTexture);
    }

    @Override
    public void applyEffect(Main main) {
        shield = true;
        this.setDestroyed(true);
    }

    public static boolean isShield() {
        return shield;
    }

    public static void setShield() {
        shield = false;
    }
}
