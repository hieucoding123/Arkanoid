package entity.Effect;

import com.main.gamemode.GameMode;
import entity.object.Ball;
import entity.TextureManager;
import entity.object.Paddle;

import java.util.ArrayList;

public class ThreeBallsEffect extends EffectItem {

    public ThreeBallsEffect(float x, float y, float dy) {
        super(x, y, dy, TextureManager.threeBallsTextures);
    }

//    public ThreeBallsEffect(float x, float y, float dy, float scale) {
//        super(x, y, dy, scale, TextureManager.threeBallsTextures);
//    }

    @Override
    public void applyEffect(Paddle paddle, ArrayList<Ball> balls) {
        if (this.isDestroyed() || balls.isEmpty()) {
            return;
        }

        Ball originalBall = balls.get(0);

        Ball ball1 = new Ball(originalBall);
        ball1.setAngle(originalBall.getAngle() + 0.5f);
        ball1.updateVelocity();

        Ball ball2 = new Ball(originalBall);
        ball2.setAngle(originalBall.getAngle() - 0.5f);
        ball2.updateVelocity();

        balls.add(ball1);
        balls.add(ball2);
        this.setDestroyed(true);
    }
}
