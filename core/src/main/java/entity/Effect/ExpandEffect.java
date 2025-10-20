package entity.Effect;

import com.main.gamemode.GameMode;
import entity.object.Ball;
import entity.object.Paddle;
import entity.TextureManager;
import entity.object.brick.BricksMap;
import com.badlogic.gdx.Gdx;

import java.util.ArrayList;

public class ExpandEffect extends EffectItem {
    private static final float EFFECT_DURATION = 5.0f;
    private Paddle paddle;

    public ExpandEffect(float x, float y, float dy, Paddle paddle) {
        super(x, y, dy, TextureManager.expandpaddleTexture);
        this.paddle = paddle;
    }
    @Override
    public void applyEffect(Paddle paddle, ArrayList<Ball> balls) {
        if (this.paddle != null) {
            this.paddle.activateExpand(EFFECT_DURATION);
            float screenWidth = Gdx.graphics.getWidth();
            if (this.paddle.getX() + this.paddle.getWidth() > screenWidth - BricksMap.xBeginCoord) {
                float newX = screenWidth - BricksMap.xBeginCoord - this.paddle.getWidth();
                this.paddle.setX(newX);
            }

            if (this.paddle.getX() < BricksMap.xBeginCoord) {
                this.paddle.setX(BricksMap.xBeginCoord);
            }
        }
        this.setDestroyed(true);
    }
}

