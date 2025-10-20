package entity.Effect;

import com.main.Main;
import com.main.gamemode.GameMode;
import entity.object.Ball;
import entity.TextureManager;

public class ThreeBallsEffect extends EffectItem {

    public ThreeBallsEffect(float x, float y, float dy) {
        super(x, y, dy, TextureManager.threeBallsTextures);
    }

//    public ThreeBallsEffect(float x, float y, float dy, float scale) {
//        super(x, y, dy, scale, TextureManager.threeBallsTextures);
//    }

    @Override
    public void applyEffect(GameMode gameMode) {
        if (this.isDestroyed() || gameMode.balls.isEmpty()) {
            return;
        }
        this.setDestroyed(true);

        Ball originalBall = gameMode.balls.get(0);

        Ball ball1 = new Ball(originalBall);
        ball1.setAngle(originalBall.getAngle() + 0.5f);
        ball1.updateVelocity();

        Ball ball2 = new Ball(originalBall);
        ball2.setAngle(originalBall.getAngle() - 0.5f);
        ball2.updateVelocity();

        gameMode.balls.add(ball1);
        gameMode.balls.add(ball2);
    }
}
