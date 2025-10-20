package entity.Effect;

import entity.object.Ball;
import entity.object.Paddle;
import entity.object.brick.Brick;

import java.util.Random;

public class EffectFactory {
    private Random rand = new Random();

    public EffectItem tryCreateEffectItem(Brick brick, Paddle paddle, Ball ball) {
        float chance = rand.nextFloat();

        if (chance < 0.1) {
            return new SlowBallEffect(brick.getX(), brick.getY(), -1);
        }else if (chance < 0.2) {
            return new BigballEffect(brick.getX(), brick.getY(), -1);
        }else if (chance < 0.4) {
            return new ShieldEffect(ball.getX(), ball.getY(), -1);
        }else if (chance < 0.65) {
            return new ExpandEffect(brick.getX(), brick.getY(), -1, paddle);
        }
            return new ThreeBallsEffect(brick.getX(), brick.getY(), -1);

//        return null;
    }
}
