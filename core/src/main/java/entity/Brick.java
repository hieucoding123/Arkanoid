package entity;

import com.badlogic.gdx.graphics.Texture;

public class Brick extends GameObject {
    private int hitPoints;
    private boolean explosion;
    private int row;
    private int col;

    public Brick(float x, float y, int hitPoints, boolean explosion, int row, int col, Texture texture) {
        super(x, y, texture);
        this.hitPoints = hitPoints;
        this.explosion = explosion;
        this.row = row;
        this.col = col;
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

    public boolean getExplosion() {
        return explosion;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
