package entity;

import com.badlogic.gdx.graphics.Texture;

public abstract class EffectItem extends MovableObject{
    private long startTime;

    public EffectItem(float x, float y, float dy, Texture texture) {
        super(x, y, texture);
        this.dx = 0;            // can only drop down
        this.dy = dy;
        this.startTime = System.currentTimeMillis();
    }

    public EffectItem(float x, float y, float dy, float scale, Texture texture) {
        super(x, y, scale, texture);
        this.dx = 0;
        this.dy = dy;           // can only drop down
        this.startTime = System.currentTimeMillis();
    }

    public abstract void applyEffect();
}
