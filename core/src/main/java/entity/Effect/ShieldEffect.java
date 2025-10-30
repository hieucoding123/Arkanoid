package entity.Effect;

import com.main.gamemode.GameMode;
import entity.TextureManager;
import entity.object.Ball;
import entity.object.Paddle;
import entity.object.brick.BricksMap;

import java.util.ArrayList;

public class ShieldEffect extends EffectItem {
    private static boolean shield = false;
    private static boolean up = false;

    public ShieldEffect(float x, float y, float dy) {
        super(x, y, dy, TextureManager.shieldTexture);
    }

    @Override
    public void applyEffect(Paddle paddle, ArrayList<Ball> balls, BricksMap bricksMap) {
        shield = true;
        paddle.activateShield();
        this.setDestroyed(true);
    }

    public static boolean isShield() {
        return shield;
    }

    public static void setShield() {
        shield = false;
    }

    public static boolean isUp() {
        return up;
    }

    public static void setUp() {
        up = true;
    }
}
