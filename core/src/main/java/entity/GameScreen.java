package entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture; // Import Texture
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image; // Import Image
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
//import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
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

    // Textures and Images for icons
    private Texture heartTexture;
    private Texture clockTexture;
    private Image heartImage;
    private Image clockImage;

    //Score
    private ScoreManager scoreManager;
    //Lives
    private int lives;
    //Time
    private double times;

    //Constants
    private final float RSCORE_X = 300f;
    private final float SCORE_Y = 970f;
    private final float LIVES_X = 80f; // This will be the icon's X
    private final float LIVES_Y = 900f; // This will be the icon's Y
    private final float TIME_X = 170f; // This will be the icon's X
    private final float TIME_Y = 900f; // This will be the icon's Y
    private final float ICON_LABEL_PADDING = 5f; // Padding between icon and label
    private final float ICON_SIZE = 25f; // New constant for icon size

    //Constructor
    public GameScreen(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
        this.lives = 3;
        this.times = 0.0f;
    }

    public void create(){
        viewport = new FitViewport(800, 1000);
        stage = new Stage(viewport, new SpriteBatch());
        font = new BitmapFont(Gdx.files.internal("ui/F_Retro.fnt"));
        font.getData().setScale(0.6f);

        skin = new Skin();
        skin.add("default-font", font);

        // Color
        Label.LabelStyle uiLabelStyle = new Label.LabelStyle(font, new Color(148f, 148f, 148f, 0.8f));
        skin.add("default", uiLabelStyle);

        // Labels
        scoreLabel = new Label("0.0", skin);
        livesLabel = new Label("3", skin);
        timeLabel = new Label("0.0", skin);

        // Load textures
        heartTexture = new Texture(Gdx.files.internal("images/heart.png"));
        clockTexture = new Texture(Gdx.files.internal("images/clock.png"));

        // Create images
        heartImage = new Image(heartTexture);
        clockImage = new Image(clockTexture);

        // Set size
        heartImage.setSize(ICON_SIZE, ICON_SIZE);
        clockImage.setSize(ICON_SIZE, ICON_SIZE);

        // Position icons
        heartImage.setPosition(LIVES_X, LIVES_Y+5);
        clockImage.setPosition(TIME_X, TIME_Y+5);

        // Position labels next to icons
        livesLabel.setPosition(LIVES_X + ICON_SIZE + ICON_LABEL_PADDING, LIVES_Y);
        timeLabel.setPosition(TIME_X + ICON_SIZE + ICON_LABEL_PADDING, TIME_Y);

        // Add actors to stage
        stage.addActor(scoreLabel);
        stage.addActor(heartImage);
        stage.addActor(livesLabel);
        stage.addActor(clockImage);
        stage.addActor(timeLabel);
    }

    public void render() {
        // Update Text
        scoreLabel.setText(String.valueOf(scoreManager.getScore()));
        scoreLabel.pack(); // Recalc size
        scoreLabel.setPosition(RSCORE_X - scoreLabel.getPrefWidth(), SCORE_Y - scoreLabel.getPrefHeight());

        // Later
        livesLabel.setText(String.valueOf(this.lives));
        timeLabel.setText(formatTime(times));

        //Render UI
        viewport.apply(true);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    private String formatTime(double time) {
        int minutes = (int) (times / 60);
        int seconds = (int) (times % 60);
        return String.format("%02d:%02d", minutes, seconds);
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setTime(double timeInSeconds) {
        this.times = timeInSeconds;
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        if (stage != null) stage.dispose();
        if (skin != null) skin.dispose();
        if (font != null) font.dispose();
        if (heartTexture != null) heartTexture.dispose();
        if (clockTexture != null) clockTexture.dispose();
    }

    // Getter
    public Stage getStage() {
        return stage;
    }

}
