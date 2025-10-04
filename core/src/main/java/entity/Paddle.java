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

    public void handleInput() {
        //Press LEFT
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.LEFT)) {
            if (this.getX() > 0) { // Check LEFT
                this.setVelocity(-this.speed, 0);
            } else {
                this.setVelocity(0, 0);
            }
        }
        //Press RIGHT
        else if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.RIGHT)) {
            if (this.getX() < Gdx.graphics.getWidth() - this.getWidth()) { //Check RIGHT
                this.setVelocity(this.speed, 0);
            } else {
                this.setVelocity(0, 0);
            }
        }
        //IF NO PRESS KEEP IT STAND
        else {
            this.setVelocity(0, 0);
        }
    }
}
