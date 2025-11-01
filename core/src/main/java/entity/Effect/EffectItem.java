package entity.Effect;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.main.components.CollisionManager;
import entity.MovableObject;
import entity.object.Ball;
import entity.object.Paddle;
import entity.object.brick.BricksMap;

import java.util.ArrayList;

/**
 * Abstract base class representing all effect items in the game.
 */
public abstract class EffectItem extends MovableObject {

    static ArrayList<EffectItem> items = new ArrayList<>();

    protected Ball triggeringBall = null;

    /**
     * Constructs a new effect item with the specified position, velocity, and texture.
     * @param x        the x-coordinate of the effect item
     * @param y        the y-coordinate of the effect item
     * @param dy       the vertical velocity multiplier
     * @param texture  the texture of the effect item
     */
    public EffectItem(float x, float y, float dy, Texture texture) {
        super(x, y, texture);

        if (this.orgWidth != 0) {
            this.scaleWidth = 40.0f / this.orgWidth;
        }
        if (this.orgHeight != 0) {
            this.scaleHeight = 40.0f / this.orgHeight;
        }
        this.setVelocity(0, dy * 60f);
        items.add(this);
    }

    /**
     * Returns the list of all current effect items.
     * @return a list of {@link EffectItem} objects
     */
    public static ArrayList<EffectItem> getItems() {
        return items;
    }

    /**
     * Applies the specific effect behavior.
     * @param paddle    the player's paddle
     * @param balls     the list of active balls
     * @param bricksMap the current brick map
     */
    public abstract void applyEffect(Paddle paddle, ArrayList<Ball> balls, BricksMap bricksMap);

    /**
     * Adds a new effect item to the global list.
     * @param effectItem the effect item to add
     */
    public static void addEffectItem(EffectItem effectItem) {
        items.add(effectItem);
    }

    /**
     * Updates all effect items and applies their effects upon collision with the paddle.
     * @param paddle    the player's paddle
     * @param balls     the list of active balls
     * @param bricksMap the current brick map
     * @param delta     the time delta for updating
     */
    public static void updateEffectItems(Paddle paddle, ArrayList<Ball> balls, BricksMap bricksMap, float delta) {
        ArrayList<EffectItem> itemsCopy = new ArrayList<>(items);
        for (EffectItem effectItem : itemsCopy) {
            effectItem.update(delta);
            if (CollisionManager.checkRectRect(paddle, effectItem)) {
                effectItem.applyEffect(paddle, balls, bricksMap);
            }
        }
        items.removeIf(EffectItem::isDestroyed);
    }

    /**
     * Moves all active effect items.
     * @param delta the time delta for movement
     */
    public static void moveItems(float delta) {
        ArrayList<EffectItem> itemsCopy = new ArrayList<>(items);
        for (EffectItem effectItem : itemsCopy) {
            effectItem.update(delta);
        }
    }

    /**
     * Checks for collisions between the paddle and effect items, applying effects when necessary.
     * @param paddle    the player's paddle
     * @param balls     the list of active balls
     * @param bricksMap the current brick map
     */
    public static void effectCollision(Paddle paddle, ArrayList<Ball> balls, BricksMap bricksMap) {
        ArrayList<EffectItem> itemsCopy = new ArrayList<>(items);
        for (EffectItem effectItem : itemsCopy) {
            if (paddle.checkCollision(effectItem)) {
                effectItem.applyEffect(paddle, balls, bricksMap);
            }
        }
        items.removeIf(EffectItem::isDestroyed);
    }

    /**
     * Draws all active effect items on the game screen.
     *
     * @param batch the {@link SpriteBatch} used for rendering
     */
    public static void drawEffectItems(SpriteBatch batch) {
        for (EffectItem item : items) {
            item.draw(batch);
        }
    }

    /**
     * Clears all effect items from the list.
     */
    public static void clear() {
        items.clear();
    }

    public static void ClearAllEffect(Paddle p1, Paddle p2, ArrayList<Ball> balls) {
        items.clear();
        if (p1 != null) {
            p1.clearEffects();
        }
        if (p2 != null) {
            p2.clearEffects();
        }

        if (balls != null) {
            for (Ball ball : balls) {
                if (ball != null) {
                    ball.clearEffects();
                }
            }
        }

        ShieldEffect.setShield();
    }
}
