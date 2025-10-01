package entity;

import com.badlogic.gdx.graphics.Texture;

public class Paddle extends MovableObject {
    public Paddle(float x, float y, Texture texture) {
        super(x, y, texture);
    }

    public Paddle(float x, float y, float scale, Texture texture) {
        super(x, y, scale, texture);
    }
}
