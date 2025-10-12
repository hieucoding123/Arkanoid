package entity;

import com.badlogic.gdx.graphics.Texture;

public class Brick extends GameObject {
    private int hitPoints;

    public Brick(float x, float y, int hitPoints, Texture texture) {
        super(x, y, texture);
        this.hitPoints = hitPoints;
        this.setDestroyed(this.hitPoints <= 0);
    }

    public Brick(float x, float y, float scaleWidth, float scaleHeight, int hitPoints, Texture texture) {
        super(x, y, scaleWidth, scaleHeight, texture);
        this.hitPoints = hitPoints;
        this.setDestroyed(this.hitPoints <= 0);
    }
    public void update() {
        this.setDestroyed(this.hitPoints <= 0);
    }

    public void takeHit() {
        this.hitPoints--;
    }

    public static int gethitPoints(Brick brick) {
        return brick.hitPoints;
    }

    public void  setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }
}
