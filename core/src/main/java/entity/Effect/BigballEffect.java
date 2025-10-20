package entity.Effect;

import com.main.Game;
import com.main.gamemode.GameMode;
import entity.object.Ball;
import entity.TextureManager;

public class BigballEffect extends EffectItem {
    private static final float EFFECT_DURATION = 10.0f;

    public BigballEffect(float x, float y, float dy) {
        super(x, y, dy, TextureManager.BALLTexture);
    }

    @Override
    public void applyEffect(GameMode gameMode) {
        for (Ball ball : gameMode.balls) {
            ball.activateBig(EFFECT_DURATION);
            //Left wall
            if (ball.getX() < Game.padding_left_right) {
                ball.setX(Game.padding_left_right);
            }
            //Right wall
            if (ball.getX() + ball.getWidth() > Game.SCREEN_WIDTH - Game.padding_left_right) {
                ball.setX(Game.SCREEN_WIDTH - Game.padding_left_right - ball.getWidth());
            }
            //Top wall
            if (ball.getY() + ball.getHeight() > Game.padding_top) {
                ball.setY(Game.padding_top - ball.getHeight());
            }
        }
        this.setDestroyed(true);
    }
}
