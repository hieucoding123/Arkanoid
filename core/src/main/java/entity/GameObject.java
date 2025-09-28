package entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameObject {
    private int x;
    private int y;
    private int width;
    private int height;

    Texture texture;

    public GameObject(int x, int y, int w, int h, Texture texture) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.texture = texture;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);
    }
}
