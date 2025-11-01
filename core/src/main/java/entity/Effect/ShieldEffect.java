package entity.Effect;

import com.main.Game;
import com.main.gamemode.GameMode;
import entity.TextureManager;
import entity.object.Ball;
import entity.object.Paddle;
import entity.object.brick.BricksMap;

import java.util.ArrayList;

/**
 * Represents an effect that grants the player a temporary shield.
 */
public class ShieldEffect extends EffectItem {
    private static boolean shield = false;
    private static boolean up = false;

    /**
     * Constructs a new ShieldEffect instance.
     *
     * @param x  the x-coordinate of the effect
     * @param y  the y-coordinate of the effect
     * @param dy the vertical velocity of the effect
     */
    public ShieldEffect(float x, float y, float dy) {
        super(x, y, dy, TextureManager.shieldTexture);
    }

    @Override
    public void applyEffect(Paddle paddle, ArrayList<Ball> balls, BricksMap bricksMap) {
        Game.playSfx(Game.sfx_shield,0.8f);
        shield = true;
        paddle.activateShield();
        this.setDestroyed(true);
    }

    /**
     * Checks whether the shield is currently active.
     *
     * @return {@code true} if the shield is active, otherwise {@code false}
     */
    public static boolean isShield() {
        return shield;
    }

    /**
     * Deactivates the shield.
     */
    public static void setShield() {
        shield = false;
    }

    /**
     * Checks whether the shield state is marked as “up”.
     *
     * @return {@code true} if the shield is up, otherwise {@code false}
     */
    public static boolean isUp() {
        return up;
    }

    /**
     * Marks the shield state as “up”.
     */
    public static void setUp() {
        up = true;
    }
}
