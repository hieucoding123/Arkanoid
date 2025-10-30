package entity.Effect;

import com.main.Game;
import entity.object.Ball;
import entity.object.Paddle;
import entity.TextureManager;
import entity.object.brick.Brick;
import entity.object.brick.BricksMap;
import java.util.ArrayList;
import java.util.Random;

public class RemoveEffect extends EffectItem {

    private static final Random rand = new Random();

    public RemoveEffect(float x, float y, float dy) {
        super(x, y, dy, TextureManager.DeleteEffectTexture);
    }

    @Override
    public void applyEffect(Paddle paddle, ArrayList<Ball> balls, BricksMap bricksMap) {
        Game.sfx_cleareffect.play();
        EffectItem.clear();
        if (paddle != null) {
            paddle.clearEffects();
        }
        for (Ball ball : balls) {
            ball.clearEffects();
        }

        if (bricksMap != null) {
            for (Brick brick : bricksMap.getBricks()) { //
                brick.clearUnbreak();
            }
        }
        ShieldEffect.setShield();
        this.setDestroyed(true);
    }
}
