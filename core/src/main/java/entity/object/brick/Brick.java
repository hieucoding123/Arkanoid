package entity.object.brick;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.main.Game;
import entity.object.MovableObject;
import entity.TextureManager;

/**
 * Represents a single brick object in the game.
 * Each brick can have hit points, explosion behavior, color, and movement.
 */
public class Brick extends MovableObject {
    private int hitPoints;
    private boolean explosion;
    private boolean unbreak = false;
    private int row;
    private int col;
    private final int color;
    private boolean isExploding = false;
    private boolean explosiontimes = false;
    private float explosionTimer = 0f;
    public static final float EXPLOSION_DURATION = 0.2f;
    private static Texture explosionTexture;
    private boolean isMove = false;
    private float moveSpeed = 0;
    private final float originalX;
    private float moveRange;

    /**
     * Constructs a brick instance with specified properties.
     * @param x          the x-coordinate
     * @param y          the y-coordinate
     * @param hitPoints  number of hits required to destroy the brick
     * @param explosion  whether this brick can explode
     * @param row        the row position in the brick grid
     * @param col        the column position in the brick grid
     * @param color      color identifier
     * @param texture    texture representing the brick
     */
    public Brick(float x, float y, int hitPoints, boolean explosion, int row, int col, int color, Texture texture) {
        super(x, y, texture);
        originalX = x;
        this.hitPoints = hitPoints;
        this.explosion = explosion;
        this.row = row;
        this.col = col;
        this.color = color;
        this.setDestroyed(this.hitPoints <= 0);
        explosionTexture = TextureManager.ExplosionTexture;
    }

    /**
     * Updates the brick's state, including explosion timing and movement.
     * @param delta time elapsed since the last update
     */
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

    /**
     * Enables horizontal movement for the brick.
     * @param moveSpeed the speed of movement
     * @param moveRange the maximum horizontal range
     */
    public void setMovement(float moveSpeed, float moveRange) {
        this.isMove = true;
        this.moveSpeed = moveSpeed * 60f;
        this.moveRange = moveRange;
        this.dx = this.moveSpeed;
    }

    /**
     * Draws the brick using the provided sprite batch.
     * @param batch the sprite batch used for rendering
     */
    @Override
    public void draw(SpriteBatch batch) {
        if (isDestroyed()) {
            return;
        }
        if (isExploding) {
            batch.draw(explosionTexture, getX(), getY(), getWidth(), getHeight());
        } else {
            super.draw(batch);
        }
    }

    /** Reduces hit points when hit, and changes texture if needed. */
    public void takeHit() {
        if (unbreak) {
            return;
        }
        com.main.Game.sfx_pop.play();
        this.hitPoints--;
        if (this.hitPoints == 1) {
            this.texture = TextureManager.brick1HIT;
        }
    }

    /** @return the current hit points of the brick */
    public int gethitPoints() {
        return this.hitPoints;
    }

    /**
     * Sets the brick's hit points.
     * @param hitPoints the new hit point value
     */
    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    /** @return whether the brick has explosion behavior */
    public boolean getExplosion() {
        return explosion;
    }

    /** @return the brick's row index */
    public int getRow() {
        return row;
    }

    /** @return the brick's column index */
    public int getCol() {
        return col;
    }

    /** Starts the explosion animation and marks the brick as destroyed. */
    public void startExplosion() {
        if (!isExploding && !isDestroyed()) {
            Game.playSfx(Game.sfx_explode, 0.8f);
            this.isExploding = true;
            this.explosionTimer = EXPLOSION_DURATION;
            this.setHitPoints(0);
        }
    }

    /** @return the color identifier of the brick */
    public int getColor() {
        return this.color;
    }

    /** @return true if the brick should explode this frame */
    public boolean shouldExplode() {
        return isExploding && explosionTimer <= 0 && !explosiontimes;
    }

    /** Marks the brick as already exploded. */
    public void setexplosiontimes() {
        this.explosiontimes = true;
    }

    /** Sets the brick as unbreakable and updates its texture. */
    public void setUnbreak() {
        this.unbreak = true;
        this.texture = TextureManager.brickNOHIT;
        this.explosion = false;
    }

    /** @return true if the brick is unbreakable */
    public boolean isUnbreak() {
        return this.unbreak;
    }

    /** Clears the unbreakable status and restores the original texture. */
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

    /**
     * Sets the brick's row position.
     * @param row the new row index
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Sets the brick's column position.
     * @param col the new column index
     */
    public void setCol(int col) {
        this.col = col;
    }

    /** @return true if the brick is currently exploding */
    public boolean isExploding() {
        return this.isExploding;
    }
}
