package com.main.gamescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class VsGameScreen {
    private Stage stage;
    private Viewport viewport;
    private Skin skin;
    private BitmapFont font;

    private Label scoreLabel1;
    private double p1Score;
    private int p1Wins;
    private double p2Score;
    private Label scoreLabel2;
    private int p2Wins;
    private String result;
    private Label timeLabel;
    private Label resultLabel;
    // Textures and Images for icons
    private Texture clockTexture;

    private double times;

    public VsGameScreen() {
        this.times = 0.0;
        this.p1Score = 0.0;
        this.p2Score = 0.0;
        this.p1Wins = 0;
        this.p2Wins = 0;
    }

    public void create() {
        viewport = new FitViewport(800, 1000);
        stage = new Stage(viewport, new SpriteBatch());
        font = new BitmapFont(Gdx.files.internal("ui/F_Retro.fnt"));
        font.getData().setScale(0.6f);

        skin = new Skin();
        skin.add("default-font", font);

        Label.LabelStyle uiLabelStyle = new Label.LabelStyle(font, new Color(148f, 148f, 148f, 0.8f));
        skin.add("default", uiLabelStyle);

        scoreLabel1 = new Label("0.0", skin);
        scoreLabel1.setColor(Color.GREEN);
        scoreLabel2 = new Label("0.0", skin);
        scoreLabel2.setColor(Color.BLUE);
        result = p1Wins + " - " + p2Wins;
        resultLabel = new Label(result, skin);
        resultLabel.setColor(Color.GOLD);
//        float RESULT_X = 105f;
//        float RESULT_Y = 900f;
//        resultLabel.setPosition(RESULT_X - resultLabel.getPrefWidth() / 2, RESULT_Y);
        timeLabel = new Label(String.valueOf(this.times), skin);

        clockTexture = new Texture(Gdx.files.internal("images/clock.png"));
        Image clockImage = new Image(clockTexture);
        float ICON_SIZE = 25f;
        clockImage.setSize(ICON_SIZE, ICON_SIZE);

        // This will be the icon's X
        float TIME_X = 170f;
        // This will be the icon's Y
        float TIME_Y = 900f;
        clockImage.setPosition(TIME_X, TIME_Y +5);
        // Padding between icon and label
        float ICON_LABEL_PADDING = 5f;
        timeLabel.setPosition(TIME_X + ICON_SIZE + ICON_LABEL_PADDING, TIME_Y);

        stage.addActor(scoreLabel1);
        stage.addActor(scoreLabel2);
        stage.addActor(clockImage);
        stage.addActor(timeLabel);
        stage.addActor(resultLabel);
    }

    public void render() {
        scoreLabel1.setText(String.valueOf(p1Score));
        scoreLabel1.pack(); // Recalc size
        float RSCORE1_X = 175f;
        float SCORE1_Y = 970f;
        scoreLabel1.setPosition(RSCORE1_X - scoreLabel1.getPrefWidth(), SCORE1_Y - scoreLabel1.getPrefHeight());

        scoreLabel2.setText(String.valueOf(p2Score));
        scoreLabel2.pack(); // Recalc size
        float RSCORE2_X = 250f;
        float SCORE2_Y = 970f;
        scoreLabel2.setPosition(RSCORE2_X, SCORE2_Y - scoreLabel2.getPrefHeight());
        resultLabel.setText(p1Wins + " - " + p2Wins);

        resultLabel.pack();
        float RESULT_X = 105f;
        float RESULT_Y = 900f;
        resultLabel.setPosition(RESULT_X - resultLabel.getPrefWidth() / 2, RESULT_Y);

        timeLabel.setText(formatTime(times));
        viewport.apply(true);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void resize(int width, int height) {
        if (viewport != null) {
            viewport.update(width, height, true);
        }
    }

    private String formatTime(double times) {
        int minutes = (int) (times / 60);
        int seconds = (int) (times % 60);
        return String.format("%02d:%02d", minutes, seconds);
    }

    public void setTime(double timeInSeconds) {
        this.times = timeInSeconds;
        if (this.times <= 10) {
            if ((int) times % 2 == 0)
                timeLabel.setColor(Color.RED);
            else
                timeLabel.setColor(Color.GRAY);
        } else {
            timeLabel.setColor(Color.WHITE);
        }
    }

    public void setScores(double p1Score, int p1Wins, double p2Score, int p2Wins) {
        this.p1Score = p1Score;
        this.p1Wins = p1Wins;
        this.p2Score = p2Score;
        this.p2Wins = p2Wins;
        result = p1Wins + " - " + p2Wins;
    }

    public void dispose() {
        if (stage != null) stage.dispose();
        if (skin != null) skin.dispose();
        if (font != null) font.dispose();
        if (clockTexture != null) clockTexture.dispose();
    }

    public Stage getStage() {
        return stage;
    }

}
