package entity.object.brick;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import entity.MovableObject;
import entity.TextureManager;

public class Brick extends MovableObject {
    private int hitPoints;
    private boolean explosion;
    private boolean unbreak = false;
    private int row;
    private int col;
    private int color;
    private boolean isExploding = false;
    private boolean explosiontimes = false;
    private float explosionTimer = 0f;
    public static final float EXPLOSION_DURATION = 0.2f;
    private static Texture explosionTexture;
    private boolean isMove = false;
    private float moveSpeed = 0;
    private float originalX;
    private float moveRange;

    public Brick(float x, float y, int hitPoints, boolean explosion, int row, int col, int color, Texture texture) {
        super(x, y, texture);
        originalX = x;
        this.hitPoints = hitPoints;
        this.explosion = explosion;
        this.row = row;
        this.col = col;
        this.color = color;
        this.setDestroyed(this.hitPoints <= 0);
        this.explosionTexture = TextureManager.ExplosionTexture;
    }

    public void update(float delta) {
        super.update(delta);

        if (isExploding) {
            explosionTimer -= delta;
            if (explosionTimer <= 0) {
                setDestroyed(true);
            }
        }

        if (isMove) {
            float left = originalX - moveRange;
            float right = originalX + moveRange;

            if (dx > 0 && x >= right) {
                x = right;
                dx = -moveSpeed;
            } else if (dx < 0 && x <= left) {
                x = left;
                dx = moveSpeed;
            }
        }
    }

    public void setMovement(float moveSpeed, float moveRange) {
        this.isMove = true;
        this.moveSpeed = moveSpeed * 60f;
        this.moveRange = moveRange;
        this.dx = this.moveSpeed;
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
        if (unbreak) {
            return;
        }

        this.hitPoints--;
        if (this.hitPoints == 1) {
            this.texture = TextureManager.brick1HIT;
        }
    }

    public int gethitPoints() {
        return this.hitPoints;
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

    public int getColor() {
        return this.color;
    }

    public boolean shouldExplode() {
        return isExploding && explosionTimer <= 0 && !explosiontimes;
    }

    public void setexplosiontimes() {
        this.explosiontimes = true;
    }

    public void setUnbreak() {
        this.unbreak = true;
        this.texture = TextureManager.brickNOHIT;
        this.explosion = false;
    }

    public boolean isUnbreak() {
        return this.unbreak;
    }

    public void clearUnbreak() {
        if (this.unbreak) {
            this.unbreak = false;

            if (this.hitPoints >= 2) {
                this.texture = TextureManager.brick2HIT;
            } else {
                this.texture = TextureManager.brick1HIT;
            }
        }
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
