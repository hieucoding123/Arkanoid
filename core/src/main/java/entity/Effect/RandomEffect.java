package entity.Effect;

import entity.object.Ball;
import entity.object.Paddle;
import entity.TextureManager;
import entity.object.brick.BricksMap;
import java.util.ArrayList;
import java.util.Random;

public class RandomEffect extends EffectItem {

    private static final Random rand = new Random();

    public RandomEffect(float x, float y, float dy) {
        super(x, y, dy, TextureManager.RandomEffectTexture);
    }
    @Override
    public void applyEffect(Paddle paddle, ArrayList<Ball> balls, BricksMap bricksMap) {
        int RadomNumber = rand.nextInt(8);
        EffectItem Effectchosen = null;
        int x = 0;
        int y = 0;

        switch (RadomNumber) {
            case 0:
                Effectchosen = new ExpandEffect(x, y, dy);
                break;
            case 1:
                Effectchosen = new ShieldEffect(x, y, dy);
                break;
            case 2:
                Effectchosen = new BigballEffect(x, y, dy);
                break;
            case 3:
                Effectchosen = new FastBallEffect(x, y, dy);
                break;
            case 4:
                Effectchosen = new SlowBallEffect(x, y, dy);
                break;
            case 5:
                Effectchosen = new StunPaddleEffect(x, y, dy);
                break;
            case 6:
                Effectchosen = new ThreeBallsEffect(x, y, dy);
                break;
            case 7:
                Effectchosen = new UnbreakBrickEffect(x, y, dy);
                break;
        }

        if (Effectchosen != null) {
            Effectchosen.applyEffect(paddle, balls, bricksMap);
        }

        this.setDestroyed(true);
    }
}

