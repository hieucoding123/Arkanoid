package entity.Effect;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.main.components.CollisionManager;
import com.main.gamemode.GameMode;
import entity.MovableObject;
import entity.object.Ball;
import entity.object.Paddle;
import entity.object.brick.Brick;
import entity.object.brick.BricksMap;

import java.util.ArrayList;

public abstract class EffectItem extends MovableObject {

    static ArrayList<EffectItem> items = new ArrayList<EffectItem>();

    protected Ball triggeringBall = null;

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

    public static ArrayList<EffectItem> getItems() {
        return items;
    }

    /**
     * Apply effect.
     */
    public abstract void applyEffect(Paddle paddle, ArrayList<Ball> balls, BricksMap bricksMap);

    /**
     * Add a new effect item to list.
     * @param effectItem EffectItem object
     */
    public static void addEffectItem(EffectItem effectItem) {
        items.add(effectItem);
    }

    /**
     * Update all effect items and check collision with paddle.
     */
    public static void updateEffectItems(Paddle paddle, ArrayList<Ball> balls, BricksMap bricksMap, float delta) {
        ArrayList<EffectItem> items_fake = new ArrayList<EffectItem>(items);
        for (EffectItem effectItem : items_fake) {
            effectItem.update(delta);
            if (CollisionManager.checkRectRect(paddle, effectItem)) {
                effectItem.applyEffect(paddle, balls, bricksMap);
            }
        }
        items.removeIf(EffectItem::isDestroyed);
    }

    public static void moveItems(float delta) {
        ArrayList<EffectItem> items_copy = new ArrayList<EffectItem>(items);
        for (EffectItem effectItem : items_copy) {
            effectItem.update(delta);
        }
    }

    public static void effectCollision(Paddle paddle, ArrayList<Ball> balls, BricksMap bricksMap) {
        ArrayList<EffectItem> items_copy = new ArrayList<EffectItem>(items);
        for (EffectItem effectItem : items_copy) {
            if (paddle.checkCollision(effectItem)) {
                effectItem.applyEffect(paddle, balls, bricksMap);
            }
        }
        items.removeIf(EffectItem::isDestroyed);
    }
    /**
     * Draw all effect items on game screen.
     * @param batch SpriteBatch object
     */
    public static void drawEffectItems(SpriteBatch batch) {
        for (EffectItem item : items) {
            item.draw(batch);
        }
    }

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
