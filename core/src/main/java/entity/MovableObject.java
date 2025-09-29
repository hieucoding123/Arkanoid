package entity;

import com.badlogic.gdx.graphics.Texture;

public class MovableObject extends GameObject {
    private int speed;
    public MovableObject(int x, int y, int w, int h, Texture texture) {
        super(x, y, w, h, texture);
    }

    /**
     * Update on object.
     */
    public void update() {

    }
}
