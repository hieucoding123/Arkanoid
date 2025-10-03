package entity;

import com.badlogic.gdx.graphics.Texture;

public class Brick extends GameObject {
    private int hitPoints;

    public Brick(float x, float y, int hitPoints, Texture texture) {
        super(x, y, texture);
        this.hitPoints = hitPoints;
        this.setDestroyed(this.hitPoints <= 0);
    }

    public Brick(float x, float y, float scale, int hitPoints, Texture texture) {
        super(x, y, scale, texture);
        this.hitPoints = hitPoints;
        this.setDestroyed(this.hitPoints <= 0);
    }
    public void update() {
        this.setDestroyed(this.hitPoints <= 0);
    }

    public void takeHit() {
        this.hitPoints--;
    }
}
