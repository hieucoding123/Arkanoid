package entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public abstract class EffectItem extends MovableObject{
    private static ArrayList<EffectItem> items = new ArrayList<EffectItem>();
    protected long startTime;

    public EffectItem(float x, float y, float dy, Texture texture) {
        super(x, y, texture);
        this.dx = 0;            // can only drop down
        this.dy = dy;
        this.startTime = System.currentTimeMillis();
        items.add(this);
    }

    public EffectItem(float x, float y, float dy, float scale, Texture texture) {
        super(x, y, scale, texture);
        this.dx = 0;
        this.dy = dy;           // can only drop down
        this.startTime = System.currentTimeMillis();
        items.add(this);
    }

    /**
     * Apply effect.
     */
    public abstract void applyEffect();

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
    public static void updateEffectItems(Paddle paddle) {
        for (EffectItem effectItem : items) {
            effectItem.update();
            if (paddle.checkCollision(effectItem)) {
                effectItem.applyEffect();
            }
        }
        EffectItem.items.removeIf(EffectItem::isDestroyed);
    }

    /**
     * Draw all effect items on game screen.
     * @param batch SpriteBatch object
     */
    public static void drawEffectItems(SpriteBatch batch) {
        for (EffectItem effectItem : items) {
            effectItem.draw(batch);
        }
    }
}
