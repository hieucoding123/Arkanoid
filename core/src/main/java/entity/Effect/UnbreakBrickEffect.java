package entity.Effect;

import com.main.Game;
import entity.TextureManager;
import entity.object.Ball;
import entity.object.Paddle;
import entity.object.brick.Brick;
import entity.object.brick.BricksMap;
import java.util.ArrayList;
import java.util.Random;

public class UnbreakBrickEffect extends EffectItem {
    private Random random = new Random();

    public UnbreakBrickEffect(float x, float y, float dy) {
        super(x, y, dy, TextureManager.UnbreakBrickTexture);
    }

    @Override
    public void applyEffect(Paddle paddle, ArrayList<Ball> balls, BricksMap bricksMap) {
        Game.playSfx(Game.sfx_bricked,0.8f);
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
