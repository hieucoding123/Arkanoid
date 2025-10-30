package entity.Effect;

import com.main.Game;
import com.main.gamemode.GameMode;
import entity.TextureManager;
import entity.object.Ball;
import entity.object.Paddle;
import entity.object.brick.BricksMap;

import java.util.ArrayList;

public class ShieldEffect extends EffectItem {
    private static boolean shield = false;
    public ShieldEffect(float x, float y, float dy) {
        super(x, y, dy, TextureManager.shieldTexture);
    }

    @Override
    public void applyEffect(Paddle paddle, ArrayList<Ball> balls, BricksMap bricksMap) {
        Game.sfx_shield.play();
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
