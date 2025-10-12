package entity;

public class ShieldEffect extends EffectItem {
    private static boolean shield = false;
    public ShieldEffect(float x, float y, float dy) {
        super(x, y, dy, TextureManager.shieldTexture);
    }
    @Override
    public void applyEffect() {
        shield = true;
    }

    public static boolean isShield() {
        return shield;
    }

    public static void setShield() {
        shield = false;
    }
}
