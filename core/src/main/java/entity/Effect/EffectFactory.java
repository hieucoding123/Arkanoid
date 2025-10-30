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
                                          double unbreak,
                                          double radom,
                                          double stunpaddle,
                                          double bigball,
                                          double slowball,
                                          double fastball,
                                          double threeball) {
        double chance = rand.nextDouble();

        if (chance <= expand) {
            return new ExpandEffect(brick.getX(), brick.getY(), -1);

        } else if (chance <= radom) {
            return new RandomEffect(brick.getX(), brick.getY(), -1);

        }  else if (chance <= shield) {
            return new ShieldEffect(brick.getX(), brick.getY(), -1);

        } else if (chance <= unbreak) {
            return new UnbreakBrickEffect(brick.getX(), brick.getY(), -1);

        } else if (chance <= stunpaddle) {
            return new StunPaddleEffect(brick.getX(), brick.getY(), -1);

        } else if (chance <= bigball) {
            if (ball != null) {
                return new BigballEffect(brick.getX(), brick.getY(), -1, ball);
            } else {
                return new BigballEffect(brick.getX(), brick.getY(), -1);
            }
        } else if (chance <= slowball) {
            if (ball != null) {
                return new SlowBallEffect(brick.getX(), brick.getY(), -1, ball);
            }
            return new SlowBallEffect(brick.getX(), brick.getY(), -1);

        } else if (chance <= fastball) {
            if (ball != null) {
                return new FastBallEffect(brick.getX(), brick.getY(), -1, ball);
            } else {
                return new FastBallEffect(brick.getX(), brick.getY(), -1);
            }
        } else if (chance <= threeball) {
            if (ball != null) {
                return new ThreeBallsEffect(brick.getX(), brick.getY(), -1, ball);
            } else {
                return new ThreeBallsEffect(brick.getX(), brick.getY(), -1);
            }
        }
        return null;
    }
}
