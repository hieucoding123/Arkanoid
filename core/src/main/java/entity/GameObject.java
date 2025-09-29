package entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameObject {
    private int x;
    private int y;
    private int width;
    private int height;

    Texture texture;

    /**
     * Initialize game object.
     * @param x x coordinate of the object
     * @param y y coordinate of the object
     * @param w width of the rectangle containing the object
     * @param h height of the rectangle containing the object
     * @param texture texture contains image of object
     */
    public GameObject(int x, int y, int w, int h, Texture texture) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.texture = texture;
    }

    /**
     * Draw object on game screen.
     * @param batch game drawing programming
     */
    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);
    }
}
