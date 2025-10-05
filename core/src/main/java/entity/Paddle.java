package entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Paddle extends MovableObject {
    public Paddle(float x, float y, Texture texture) {
        super(x, y, texture);
        this.speed = 3.0f;
    }

    public Paddle(float x, float y, float scale, Texture texture) {
        super(x, y, scale, texture);
        this.speed = 3.0f;
    }

}
