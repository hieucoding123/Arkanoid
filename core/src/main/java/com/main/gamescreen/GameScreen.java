package com.main.gamescreen;

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
import entity.ScoreManager;


public class GameScreen {
    //Scene2D stuff
    private Stage stage;
    private Viewport viewport;
    private Skin skin;

    //Label
    private Label scoreLabel;
    private Label livesLabel;
    private Label timeLabel;

    // Textures and Images for icons
    private Texture heartTexture;
    private Texture clockTexture;

    //Score
    private final ScoreManager scoreManager;
    //Lives
    private int lives;
    //Time
    private double times;

    //Constructor
    public GameScreen(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
        this.lives = 3;
        this.times = 0.0f;
    }

    public void create(){
        viewport = new FitViewport(800, 1000);
        stage = new Stage(viewport, new SpriteBatch());
        BitmapFont font = new BitmapFont(Gdx.files.internal("ui/F_Retro.fnt"));
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
        Image heartImage = new Image(heartTexture);
        Image clockImage = new Image(clockTexture);

        // Set size
        // New constant for icon size
        float ICON_SIZE = 25f;
        heartImage.setSize(ICON_SIZE, ICON_SIZE);
        clockImage.setSize(ICON_SIZE, ICON_SIZE);

        // Position icons
        // This will be the icon's X
        float LIVES_X = 80f;
        // This will be the icon's Y
        float LIVES_Y = 900f;
        heartImage.setPosition(LIVES_X, LIVES_Y +5);
        // This will be the icon's X
        float TIME_X = 170f;
        // This will be the icon's Y
        float TIME_Y = 900f;
        clockImage.setPosition(TIME_X, TIME_Y +5);

        // Position labels next to icons
        // Padding between icon and label
        float ICON_LABEL_PADDING = 5f;
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
        scoreLabel.pack();
        //Constants
        float RSCORE_X = 300f;
        float SCORE_Y = 970f;
        scoreLabel.setPosition(RSCORE_X - scoreLabel.getPrefWidth(), SCORE_Y - scoreLabel.getPrefHeight());

        // Later
        livesLabel.setText(String.valueOf(this.lives));
        timeLabel.setText(formatTime());

        //Render UI
        viewport.apply(true);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    /**
     * Formats the class member 'times' (total seconds) into "MM:SS" string.
     * <p>
     * Note: This method currently ignores the 'time' parameter and uses the
     * class instance variable 'times' for the calculation.
     *
     * @return A {@link String} representing the formatted time in "MM:SS" format,
     * based on the 'times' class member.
     */
    private String formatTime() {
        int minutes = (int) (times / 60);
        int seconds = (int) (times % 60);
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Sets the number of lives remaining.
     *
     * @param lives The new number of lives.
     */
    public void setLives(int lives) {
        this.lives = lives;
    }

    /**
     * Sets the total elapsed time.
     *
     * @param timeInSeconds The new total time in seconds.
     */
    public void setTime(double timeInSeconds) {
        this.times = timeInSeconds;
    }

    public void resize(int width, int height) {
        if (viewport != null) {
            viewport.update(width, height, true);
        }
    }

    /**
     * Stage getter.
     * @return stage
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Disposes of all resources managed by this screen, such as the
     * Stage, Skin, Font, and Textures.
     */
    public void dispose() {
        if (stage != null) {
            stage.dispose();
        }
        if (skin != null) {
            skin.dispose();
        }

        if (heartTexture != null) {
            heartTexture.dispose();
        }
        if (clockTexture != null) {
            clockTexture.dispose();
        }
    }

}
