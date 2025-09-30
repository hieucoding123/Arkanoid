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
        return new Rectangle(x, y, this.orgWidth * this.scale, this.orgHeight * this.scale);
    }
    /**
     * Draw object on game screen.
     * @param batch game drawing programming
     */
    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, this.orgWidth * this.scale, this.orgHeight * this.scale);
    }
}
