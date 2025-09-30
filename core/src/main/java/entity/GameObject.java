package entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class GameObject {
    protected float x;
    protected float y;
    protected float orgWidth;       // original width
    protected float orgHeight;      // original height
    protected float scale;

    private final Texture texture;

    /**
     * Initialize game object.
     * @param x x coordinate of the object
     * @param y y coordinate of the object
     * @param w width of the rectangle containing the object
     * @param h height of the rectangle containing the object
     * @param texture texture contains image of object
     */
    public GameObject(float x, float y, float w, float h, Texture texture) {
        this.scale = 1;
        this.x = x;
        this.y = y;
        this.orgWidth = w;
        this.orgHeight = h;
        this.texture = texture;
    }

    /**
     * Initialize game object.
     * @param x x coordinate of the object
     * @param y y coordinate of the object
     * @param w width of the rectangle containing the object
     * @param h height of the rectangle containing the object
     * @param scale scale of object
     * @param texture texture contains image of object
     */
    public GameObject(float x, float y, float w, float h, float scale, Texture texture) {
        this.x = x;
        this.y = y;
        this.orgWidth = w;
        this.orgHeight = h;
        this.scale = scale;
        this.texture = texture;
    }

    /**
     *Bounding box to check for collisions.
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, this.getWidth(), this.getHeight());
    }
    /**
     * Draw object on game screen.
     * @param batch game drawing programming
     */
    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, this.getWidth(), this.getHeight());
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
        return this.orgWidth *  this.scale;
    }

    public float getHeight() {
        return this.orgHeight * this.scale;
    }

    /**
     * set scale of object.
     * @param scale scale of object
     */
    public void setScale(float scale) {
        this.scale = scale;
    }
}
