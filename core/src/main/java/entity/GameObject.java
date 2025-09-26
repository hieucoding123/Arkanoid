package entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameObject {
    private int x;
    private int y;
    private int width;
    private int height;

    SpriteBatch batch;
    Texture texture;

    public GameObject(int x, int y, int w, int h, SpriteBatch batch, String path) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.batch = batch;
        this.texture = new Texture(path);
    }

    public void draw() {
        batch.draw(texture, x, y, width, height);
    }
    public void dispose() {
        texture.dispose();
    }
}
