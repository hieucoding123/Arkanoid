package entity.Effect;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import entity.MovableObject;
import entity.object.Paddle;
import com.main.Main;
import java.util.ArrayList;

public abstract class EffectItem extends MovableObject {
    private static ArrayList<EffectItem> items = new ArrayList<EffectItem>();

    public EffectItem(float x, float y, float dy, Texture texture) {
        super(x, y, texture);
        this.setVelocity(0, dy * 60f);
        items.add(this);
    }
    /**
     * Apply effect.
     */
    public abstract void applyEffect(Main main);

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
    public static void updateEffectItems(Paddle paddle, Main main, float delta) {
        for (EffectItem effectItem : new ArrayList<>(items)) {
            effectItem.update(delta);
            if (paddle.checkCollision(effectItem)) {
                effectItem.applyEffect(main);
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
}
