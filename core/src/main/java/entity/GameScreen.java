package entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.Color;



public class GameScreen {
    private SpriteBatch batch;
    private BitmapFont font;
    private ScoreManager scoreManager;

    public GameScreen(ScoreManager scoreManager) {
        batch = new SpriteBatch();
        this.scoreManager = scoreManager;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts\\PressStart2P-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 28;
        parameter.color = Color.CYAN;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 1;
        parameter.shadowOffsetX = 1;
        parameter.shadowOffsetY = -1;
        parameter.shadowColor = Color.DARK_GRAY;
        parameter.characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!?.:;,'\"() ";

        font = generator.generateFont(parameter);
        generator.dispose();
    }

    public void draw(SpriteBatch batch) {
        font.draw(batch, String.valueOf(scoreManager.getScore()), 8, Gdx.graphics.getHeight() - 8);
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
