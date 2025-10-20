package entity.Effect;

import com.main.gamemode.GameMode;
import entity.object.Paddle;
import entity.TextureManager;
import entity.object.brick.BricksMap;
import com.badlogic.gdx.Gdx;

public class ExpandEffect extends EffectItem {
    private static final float EFFECT_DURATION = 5.0f;
    private Paddle paddle;

    public ExpandEffect(float x, float y, float dy, Paddle paddle) {
        super(x, y, dy, TextureManager.expandpaddleTexture);
        this.paddle = paddle;
    }
    @Override
    public void applyEffect(GameMode gameMode) {
        if (paddle != null) {
            paddle.activateExpand(EFFECT_DURATION);
            float screenWidth = Gdx.graphics.getWidth();
            if (paddle.getX() + paddle.getWidth() > screenWidth - BricksMap.xBeginCoord) {
                float newX = screenWidth - BricksMap.xBeginCoord - paddle.getWidth();
                paddle.setX(newX);
            }

            if (paddle.getX() < BricksMap.xBeginCoord) {
                paddle.setX(BricksMap.xBeginCoord);
            }
        }
        this.setDestroyed(true);
    }
}

