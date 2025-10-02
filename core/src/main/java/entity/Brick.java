package entity;

import com.badlogic.gdx.graphics.Texture;

public class Brick extends GameObject {
    private int hitPoints;
    private boolean isDestroyed;

    public Brick(float x, float y, Texture texture) {
        super(x, y, texture);
        this.hitPoints = 1;
        this.isDestroyed = false;
    }

    public Brick(float x, float y, int hitPoints, Texture texture) {
        super(x, y, texture);
        this.hitPoints = hitPoints;
        this.isDestroyed = this.hitPoints <= 0;
    }

    public Brick(float x, float y, float scale, Texture texture) {
        super(x, y, scale, texture);
        this.hitPoints = 1;
        this.isDestroyed = false;
    }
    public Brick(float x, float y, float scale, int hitPoints, Texture texture) {
        super(x, y, scale, texture);
        this.hitPoints = hitPoints;
        this.isDestroyed = this.hitPoints <= 0;
    }
    public void update() {
        this.isDestroyed = this.hitPoints <= 0;
    }
    public boolean isDestroyed() {
        return isDestroyed;
    }
    public void takeHit() {
        this.hitPoints--;
    }
}
