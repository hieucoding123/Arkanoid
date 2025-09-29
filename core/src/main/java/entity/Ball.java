package entity;

import com.badlogic.gdx.graphics.Texture;

public class Ball extends MovableObject {
    boolean checkCollision;
    public Ball(int x, int y, int w, int h, Texture texture) {
        super(x, y, w, h, texture);
    }
}
