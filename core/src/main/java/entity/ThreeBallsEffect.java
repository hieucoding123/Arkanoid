package entity;

import com.badlogic.gdx.graphics.Texture;
import com.main.Main;

public class ThreeBallsEffect extends EffectItem {

    public ThreeBallsEffect(float x, float y, float dy) {
        super(x, y, dy, TextureManager.threeBallsTextures);
    }

    public ThreeBallsEffect(float x, float y, float dy, float scale) {
        super(x, y, dy, scale, TextureManager.threeBallsTextures);
    }

    @Override
    public void applyEffect() {
        Main.balls.add(new Ball(24, 6, TextureManager.ballTexture, 1));
        Main.balls.add(new Ball(300, 50, TextureManager.ballTexture, 1));
        Main.balls.add(new Ball(600, 100, TextureManager.ballTexture, 1));
    }
}
