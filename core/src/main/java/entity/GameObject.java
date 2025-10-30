package entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class GameObject {
    protected float x;
    protected float y;
    protected float scaleWidth;
    protected float scaleHeight;
    protected float orgWidth;       // original width
    protected float orgHeight;      // original height
    protected float scale;
    private boolean isDestroyed;

    protected Texture texture;
    /**
     * Initialize game object.
     * @param x x coordinate of the object
     * @param y y coordinate of the object
     * @param texture texture contains image of object
     */
    public GameObject(float x, float y, Texture texture) {
        this.scaleWidth = 1;
        this.scaleHeight = 1;
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.orgWidth = this.texture.getWidth();
        this.orgHeight = this.texture.getHeight();
        this.setDestroyed(false);
    }

    /**
     * Initialize game object.
     * @param x x coordinate of the object
     * @param y y coordinate of the object
     * @param scaleWidth scale width of object
     * @param scaleHeight scale height of object
     * @param texture texture contains image of object
     */
    public GameObject(float x, float y, float scaleWidth, float scaleHeight, Texture texture) {
        this.x = x;
        this.y = y;
        this.scaleWidth = scaleWidth;
        this.scaleHeight = scaleHeight;
        this.texture = texture;
        this.orgWidth = this.texture.getWidth();
        this.orgHeight = this.texture.getHeight();
        this.setDestroyed(false);
    }

    /**
     *Bounding box to check for collisions.
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, this.getWidth(), this.getHeight());
    }

    /**
     * Check collision with other object.
     * @param other other object
     * @return true if collided
     */
    public boolean checkCollision(GameObject other) {
        return this.getBounds().overlaps(other.getBounds());
    }
    /**
     * Draw object on game screen.
     * @param batch game drawing programming
     */
    public void draw(SpriteBatch batch) {
        float drawWidth = this.orgWidth * this.scaleWidth;
        float drawHeight = this.orgHeight *  this.scaleHeight;

        batch.draw(texture, x, y, drawWidth, drawHeight);
    }

    /**
     * Get x coordinate.
     * @return x coordinate
     */
    public float getX() {
        return x;
    }

    /**
     * Set x coordinate of object.
     * @param x x coordinate
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Get y coordinate.
     * @return y coordinate
     */
    public float getY() {
        return y;
    }

    /**
     * Set y coordinate of object.
     * @param y y coordinate
     */
    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return this.orgWidth *  this.scaleWidth;
    }

    public float getHeight() {
        return this.orgHeight * this.scaleHeight;
    }

    /**
     * set scale of object.
     * @param scaleWidth scale width of object
     * @param scaleHeight scale height of object
     */
    public void setScale(float scaleWidth, float scaleHeight) {
        this.scaleWidth = scaleWidth;
        this.scaleHeight = scaleHeight;
    }

    /**
     * Set destroyed state of object.
     * @param isDestroyed destroyed state
     */
    public void setDestroyed(boolean isDestroyed) {
        this.isDestroyed = isDestroyed;
    }

    /**
     * Check if object is destroyed.
     * @return true if object is destroyed
     */
    public boolean isDestroyed() {
        return this.isDestroyed;
    }
}
