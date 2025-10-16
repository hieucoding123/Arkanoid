package entity.Effect;

import com.main.Main;
import entity.Ball;
import entity.TextureManager;

public class ThreeBallsEffect extends EffectItem {

    public ThreeBallsEffect(float x, float y, float dy) {
        super(x, y, dy, TextureManager.threeBallsTextures);
    }

//    public ThreeBallsEffect(float x, float y, float dy, float scale) {
//        super(x, y, dy, scale, TextureManager.threeBallsTextures);
//    }

    @Override
    public void applyEffect() {
        if (this.isDestroyed()) {
            return;
        }

        this.setDestroyed(true);

        if (Main.balls.isEmpty()) {
            this.setDestroyed(true);
            return;
        }

        Ball originalBall = Main.balls.get(0);

        float ballX = originalBall.getX();
        float ballY = originalBall.getY();

        Ball ball1 = new Ball(ballX, ballY, TextureManager.ballTexture, 2.0f);
        ball1.updateVelocity();

        Ball ball2 = new Ball(ballX, ballY, TextureManager.ballTexture, 2.0f);
        ball2.updateVelocity();

        Main.balls.add(ball1);
        Main.balls.add(ball2);

        this.setDestroyed(true);
    }
}
