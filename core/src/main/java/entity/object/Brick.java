package entity.object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import entity.GameObject;
import entity.TextureManager;

public class Brick extends GameObject {
    private int hitPoints;
    private boolean explosion;
    private int row;
    private int col;
    private boolean isExploding = false;
    private boolean explosiontimes = false;
    private float explosionTimer = 0f;
    public static final float EXPLOSION_DURATION = 0.2f;
    private static Texture explosionTexture;

    public Brick(float x, float y, int hitPoints, boolean explosion, int row, int col, Texture texture) {
        super(x, y, texture);
        this.hitPoints = hitPoints;
        this.explosion = explosion;
        this.row = row;
        this.col = col;
        this.setDestroyed(this.hitPoints <= 0);
        this.explosionTexture = TextureManager.ExplosionTexture;
    }

    public void update(float delta) {
        if (isExploding) {
            explosionTimer -= delta;
            if (explosionTimer <= 0) {
                setDestroyed(true);
            }
        }
    }
    @Override
    public void draw(SpriteBatch batch) {
        if (isDestroyed()) return;
        if (isExploding) {
            batch.draw(explosionTexture, getX(), getY(), getWidth(), getHeight());
        } else {
            super.draw(batch);
        }
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
    //Handle explosion
    public void startExplosion() {
        if (!isExploding && !isDestroyed()) {
            this.isExploding = true;
            this.explosionTimer = EXPLOSION_DURATION;
            this.setHitPoints(0);
        }
    }

    public boolean isExploding() {
        return this.isExploding;
    }

    public boolean shouldExplode() {
        return isExploding && explosionTimer <= 0 && !explosiontimes;
    }

    public void setexplosiontimes() {
        this.explosiontimes = true;
    }


}
