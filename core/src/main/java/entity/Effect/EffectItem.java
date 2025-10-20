package entity.Effect;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.main.gamemode.GameMode;
import entity.MovableObject;
import entity.object.Paddle;
import com.main.Game;
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
    public abstract void applyEffect(GameMode gameMode);

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
    public static void updateEffectItems(Paddle paddle, GameMode gameMode, float delta) {
        for (EffectItem effectItem : new ArrayList<>(items)) {
            effectItem.update(delta);
            if (paddle.checkCollision(effectItem)) {
                effectItem.applyEffect(gameMode);
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
