package entity.Effect;

import com.main.Game;
import entity.TextureManager;
import entity.object.Ball;
import entity.object.Paddle;
import entity.object.brick.Brick;
import entity.object.brick.BrickType;
import entity.object.brick.BricksMap;
import java.util.ArrayList;
import java.util.Random;

/**
 * Represents an effect that makes a random brick temporarily unbreakable.
 */
public class UnbreakBrickEffect extends EffectItem {
    /** Random number generator used to select a brick. */
    private final Random random = new Random();

    /**
     * Constructs an UnbreakBrickEffect instance.
     * @param x  the x-coordinate of the effect
     * @param y  the y-coordinate of the effect
     * @param dy the vertical velocity of the effect
     */
    public UnbreakBrickEffect(float x, float y, float dy) {
        super(x, y, dy, TextureManager.brickTextures.get(BrickType.UNBREAK));
    }

    /**
     * Applies the unbreakable effect to a random brick in the given map.
     * Plays a sound effect, selects a random non-unbreakable brick, and marks it as unbreakable.
     * @param paddle    the paddle (unused)
     * @param balls     the list of active balls (unused)
     * @param bricksMap the current bricks map containing bricks
     */
    @Override
    public void applyEffect(Paddle paddle, ArrayList<Ball> balls, BricksMap bricksMap) {
        Game.playSfx(Game.sfx_bricked, 0.8f);
        ArrayList<Brick> bricks = new ArrayList<>();
        if (bricksMap != null && !bricksMap.getBricks().isEmpty()) {
            for (Brick brick : bricksMap.getBricks()) {
                if (!brick.isUnbreak()) {
                    bricks.add(brick);
                }
            }
        }

        if (!bricks.isEmpty()) {
            int pos = random.nextInt(bricks.size());
            bricks.get(pos).setUnbreak();
        }
        this.setDestroyed(true);
    }
}
