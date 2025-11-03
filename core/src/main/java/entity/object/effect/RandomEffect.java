package entity.object.effect;

import com.main.Game;
import entity.object.Ball;
import entity.object.Paddle;
import com.main.components.TextureManager;
import entity.object.brick.BricksMap;
import java.util.ArrayList;
import java.util.Random;

/**
 * Triggers a random power-up effect when collected by the paddle.
 */
public class RandomEffect extends EffectItem {

    private static final Random rand = new Random();

    /**
     * Constructs a RandomEffect instance.
     * @param x  the x-coordinate of the effect item
     * @param y  the y-coordinate of the effect item
     * @param dy the vertical velocity multiplier
     */
    public RandomEffect(float x, float y, float dy) {
        super(x, y, dy, TextureManager.RandomEffectTexture);
    }

    /**
     * Applies the chosen effect and immediately triggers its behavior.
     * @param effect    the chosen effect item to apply
     * @param paddle    the player's paddle
     * @param balls     the list of active balls
     * @param bricksMap the current brick map
     */
    private void applyChosenEffect(EffectItem effect, Paddle paddle, ArrayList<Ball> balls, BricksMap bricksMap) {
        if (effect != null) {
            EffectItem.items.remove(effect);
            effect.applyEffect(paddle, balls, bricksMap);
        }
    }

    @Override
    public void applyEffect(Paddle paddle, ArrayList<Ball> balls, BricksMap bricksMap) {
        Game.playSfx(Game.sfx_random,0.8f);
        int RadomNumber = rand.nextInt(8) + 1;

        float x = this.getX();
        float y = this.getY();

        switch (RadomNumber) {
            case 1:
                applyChosenEffect(new ShieldEffect(x, y, dy), paddle, balls, bricksMap);
                break;
            case 2:
                applyChosenEffect(new BigballEffect(x, y, dy), paddle, balls, bricksMap);
                break;
            case 3:
                applyChosenEffect(new FastBallEffect(x, y, dy), paddle, balls, bricksMap);
                break;
            case 4:
                applyChosenEffect(new SlowBallEffect(x, y, dy), paddle, balls, bricksMap);
                break;
            case 5:
                applyChosenEffect(new StunPaddleEffect(x, y, dy), paddle, balls, bricksMap);
                break;
            case 6:
                applyChosenEffect(new ThreeBallsEffect(x, y, dy), paddle, balls, bricksMap);
                break;
            case 7:
                applyChosenEffect(new UnbreakBrickEffect(x, y, dy), paddle, balls, bricksMap);
                break;
            case 8:
                applyChosenEffect(new ExpandEffect(x, y, dy), paddle, balls, bricksMap);
                break;
        }

        this.setDestroyed(true);
    }
}
