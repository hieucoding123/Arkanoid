package entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class GameScreen {
    //Scene2D stuff
    private Stage stage;
    private Viewport viewport;
    private Skin skin;
    private BitmapFont font;

    //Label
    private Label scoreLabel;
    private Label livesLabel;
    private Label timeLabel;

    //Score
    private ScoreManager scoreManager;

    //Constants
    private final float RSCORE_X = 200f;
    private final float SCORE_Y = 970f;
    private final float LIVES_X = 80f;
    private final float LIVES_Y = 900f;
    private final float TIME_X = 152f;
    private final float TIME_Y = 900f;

    //Constructor
    public GameScreen(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
    }

    public void create(){
        viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport, new SpriteBatch());
        font = new BitmapFont(Gdx.files.internal("ui/F_Retro.fnt"));
        font.getData().setScale(0.6f);

        skin = new Skin();
        skin.add("default-font", font);

        // Color
        Label.LabelStyle uiLabelStyle = new Label.LabelStyle(font, new Color(148f, 148f, 148f, 0.8f));
        skin.add("default", uiLabelStyle);

        // Labels
        scoreLabel = new Label("0.0", skin); // Initial text
        livesLabel = new Label("3", skin);   // Initial text
        timeLabel = new Label("00:00", skin); // Initial text

        livesLabel.setPosition(LIVES_X, LIVES_Y);
        timeLabel.setPosition(TIME_X, TIME_Y);

        stage.addActor(scoreLabel);
        stage.addActor(livesLabel);
        stage.addActor(timeLabel);
    }

    public void render() {
        // Update Text
        scoreLabel.setText(String.valueOf(scoreManager.getScore()));
        scoreLabel.pack(); // Recalc size
        scoreLabel.setPosition(RSCORE_X - scoreLabel.getPrefWidth(), SCORE_Y - scoreLabel.getPrefHeight());

        // Later
        // livesLabel.setText(String.valueOf(playerLives));
        // timeLabel.setText(formatTime(gameTimer));

        //Render UI
        viewport.apply(true);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void dispose() {
        if (stage != null) stage.dispose();
        if (skin != null) skin.dispose();
        if (font != null) font.dispose();
    }

    // Getter
    public Stage getStage() {
        return stage;
    }
}

