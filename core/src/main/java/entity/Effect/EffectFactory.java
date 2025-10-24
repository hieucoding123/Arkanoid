package entity.Effect;

import entity.object.Ball;
import entity.object.Paddle;
import entity.object.brick.Brick;

import java.util.Random;

public class EffectFactory {
    private Random rand = new Random();

    public EffectItem tryCreateEffectItem(Brick brick, Paddle paddle, Ball ball,
                                          double expand,
                                          double shield,
                                          double bigball,
                                          double slowball,
                                          double threeball) {
        double chance = rand.nextDouble();

        if (chance <= expand) {
            return new ExpandEffect(brick.getX(), brick.getY(), -1, paddle);

        } else if (chance <= shield) {
            return new ShieldEffect(brick.getX(), brick.getY(), -1);

        } else if (chance <= bigball) {
            return new BigballEffect(brick.getX(), brick.getY(), -1);

        } else if (chance <= slowball) {
            return new SlowBallEffect(brick.getX(), brick.getY(), -1);

        } else if (chance <= threeball) {
            return new ThreeBallsEffect(brick.getX(), brick.getY(), -1);
        }
        return null;
    }
}
