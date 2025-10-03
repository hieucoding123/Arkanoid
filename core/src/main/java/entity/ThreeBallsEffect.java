package entity;

import com.badlogic.gdx.graphics.Texture;

public class ThreeBallsEffect extends EffectItem {

    public ThreeBallsEffect(float x, float y, float dy, Texture texture) {
        super(x, y, dy, texture);
    }

    public ThreeBallsEffect(float x, float y, float dy, float scale, Texture texture) {
        super(x, y, dy, scale, texture);
    }

    @Override
    public void applyEffect() {

    }
}
