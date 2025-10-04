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
        Ball ball1 = new Ball((float) (Math.random() * Main.SCREEN_WIDTH),
                                    140, TextureManager.ballTexture, 3.0f);
        ball1.updateVelocity();
        Ball ball2 = new Ball((float) (Math.random() * Main.SCREEN_WIDTH),
                                    140, TextureManager.ballTexture, 3.0f);
        ball2.updateVelocity();
        Ball ball3 = new Ball((float) (Math.random() * Main.SCREEN_WIDTH),
                                    140, TextureManager.ballTexture, 3.0f);
        ball3.updateVelocity();

        Main.balls.add(ball1);
        Main.balls.add(ball2);
        Main.balls.add(ball3);
    }
}
