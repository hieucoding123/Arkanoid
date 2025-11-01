package Menu.leaderboard;

import Menu.UserInterface;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.main.Game;
import com.main.GameState;
import com.main.Main;
import entity.Player;
import table.CoopDataHandler;
import table.InfiDataHandler;
import table.LevelDataHandler;

import java.util.ArrayList;

public class LeaderBoard extends UserInterface {
    private Table scrollableContent;
    private String showLeaderBoard = "INFINITE";
    private ArrayList<String> data;

    private Label coopModeButton;
    private Label infiModeButton;
    private Label levelsModeButton;

    public LeaderBoard(Main main, Player player) {
        super(main, player);
    }

    @Override
    public void create() {
        this.setBackground(new Texture(Gdx.files.internal("ui/bg.png")));
        this.setSkin(new Skin(Gdx.files.internal("ui2/ui2.json")));
        this.setStage(new Stage(new FitViewport(800, 1000)));
        this.setFont(new BitmapFont(Gdx.files.internal("ui/F_Retro.fnt")));
        this.getFont().getData().setScale(1);

        Label.LabelStyle whiteText = new Label.LabelStyle(this.getFont(), Color.WHITE);

        Gdx.input.setInputProcessor(this.getStage());

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        this.getStage().addActor(mainTable);

        mainTable.setBackground(
            new TextureRegionDrawable(
                new TextureRegion(this.getBGTexture())
            )
        );
        Table buttonTable = new Table();

        Main main = this.getMain();

        //Buttons
        coopModeButton = new Label("Co-op", whiteText);
        infiModeButton = new Label("Infinite", whiteText);
        levelsModeButton = new Label("Singleplayer", whiteText);

        infiModeButton.setColor(Color.YELLOW);

        //Inf button
        infiModeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Game.playSfx(Game.sfx_click,0.8f);
                if (!showLeaderBoard.equals("INFINITE")) {
                    showLeaderBoard = "INFINITE";
                    loadInfiniteTable();
                    infiModeButton.setColor(Color.YELLOW);
                    levelsModeButton.setColor(Color.WHITE);
                    coopModeButton.setColor(Color.WHITE);
                }
            }
        });

        //Singleplayer button
        levelsModeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Game.playSfx(Game.sfx_click,0.8f);
                if (!showLeaderBoard.equals("LEVELS")) {
                    showLeaderBoard = "LEVELS";
                    scrollableContent.clear();
                    loadLevelModeTable();
                    levelsModeButton.setColor(Color.YELLOW);
                    coopModeButton.setColor(Color.WHITE);
                    infiModeButton.setColor(Color.WHITE);
                }
            }
        });

        //Coop button
        coopModeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Game.playSfx(Game.sfx_click,0.8f);
                if (!showLeaderBoard.equals("COOP")) {
                    showLeaderBoard = "COOP";
                    scrollableContent.clear();
                    loadCoopModeTable();
                    coopModeButton.setColor(Color.YELLOW);
                    infiModeButton.setColor(Color.WHITE);
                    levelsModeButton.setColor(Color.WHITE);
                }
            }
        });

        infiModeButton.setPosition(50,150);
        levelsModeButton.setPosition(266,150);
        coopModeButton.setPosition(600,150);

        this.getStage().addActor(infiModeButton);
        this.getStage().addActor(levelsModeButton);
        this.getStage().addActor(coopModeButton);


        scrollableContent = new Table(this.getSkin());
        scrollableContent.top();

        ScrollPane scrollPane = new ScrollPane(scrollableContent,  this.getSkin());
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        mainTable.add(scrollPane).expandY().width(800).height(400).pad(10);
        mainTable.row();

        //Back Button
        Label backButton = createClickableLabel(
            "Return",        // text
            Game.sfx_back,   // clickSound
            1.0f,            // soundVolume
            () -> main.setGameState(GameState.MAIN_MENU) // clickAction
        );

        backButton.setPosition(10, 0);
        backButton.setFontScale(0.6f);
        this.getStage().addActor(backButton);

        mainTable.add(buttonTable);
        loadInfiniteTable();
    }

    private void loadInfiniteTable() {
        scrollableContent.clear();
        data = InfiDataHandler.getLeaderboardData();
        Label.LabelStyle whiteText = new Label.LabelStyle(this.getFont(), Color.WHITE);
        Label nameHeader = new Label("Name", whiteText);
        Label scoreHeader = new Label("Score", whiteText);
        Label timeHeader = new Label("Time Played", whiteText);

        nameHeader.setFontScale(0.8f);
        scoreHeader.setFontScale(0.8f);
        timeHeader.setFontScale(0.8f);

        scrollableContent.add(nameHeader).width(200).pad(10).left();
        scrollableContent.add(scoreHeader).width(200).pad(10).left();
        scrollableContent.add(timeHeader).width(200).pad(10).left();
        scrollableContent.row();

        for (int i = 0; i < data.size(); i++) {
            Label.LabelStyle textStyle;

            if (i == 0) {
                textStyle = new Label.LabelStyle(this.getFont(), Color.GOLD); // Top 1
            } else if (i == 1) {
                textStyle = new Label.LabelStyle(this.getFont(), Color.LIGHT_GRAY); // Top 2
            } else if (i == 2) {
                textStyle = new Label.LabelStyle(this.getFont(), Color.ORANGE); // Top 3
            } else {
                textStyle = whiteText;
            }
            String[] line = data.get(i).split(",");
            Label name = new Label(line[0], textStyle);
            Label score = new Label(line[1], textStyle);
            Label time = new Label(line[2], textStyle);
            name.setFontScale(0.7f);
            score.setFontScale(0.7f);
            time.setFontScale(0.7f);
            scrollableContent.add(name).width(200).pad(5).left();
            scrollableContent.add(score).width(200).pad(5).right();
            scrollableContent.add(time).width(200).pad(5).right();
            scrollableContent.row();
        }
    }

    private void loadLevelModeTable() {
        scrollableContent.clear();
        data = LevelDataHandler.getLeaderboardData();
        Label.LabelStyle whiteText = new Label.LabelStyle(this.getFont(), Color.WHITE);

        Label nameHeader = new Label("Name", whiteText);
        Label levelHeader = new Label("Level Max", whiteText);
        Label scoreHeader = new Label("Total Score", whiteText);

        nameHeader.setFontScale(0.8f);
        levelHeader.setFontScale(0.8f);
        scoreHeader.setFontScale(0.8f);

        scrollableContent.add(nameHeader).width(200).pad(10).left();
        scrollableContent.add(levelHeader).width(200).pad(10).right();
        scrollableContent.add(scoreHeader).width(200).pad(10).right();
        scrollableContent.row();

        for (int i = 0; i < data.size(); i++) {
            Label.LabelStyle textStyle;

            if (i == 0) {
                textStyle = new Label.LabelStyle(this.getFont(), Color.GOLD);
            } else if (i == 1) {
                textStyle = new Label.LabelStyle(this.getFont(), Color.LIGHT_GRAY);
            } else if (i == 2) {
                textStyle = new Label.LabelStyle(this.getFont(), Color.ORANGE);
            } else {
                textStyle = whiteText;
            }

            String[] line = data.get(i).split(",");
            Label name = new Label(line[0], textStyle);
            Label level = new Label(line[1], textStyle);
            Label score = new Label(line[2], textStyle);

            name.setFontScale(0.7f);
            level.setFontScale(0.7f);
            score.setFontScale(0.7f);

            scrollableContent.add(name).width(200).pad(5).left();
            scrollableContent.add(level).width(200).pad(5).right();
            scrollableContent.add(score).width(200).pad(5).right();
            scrollableContent.row();
        }
    }

    private void loadCoopModeTable() {
        scrollableContent.clear();
        data = CoopDataHandler.getLeaderboardData();
        Label.LabelStyle whiteText = new Label.LabelStyle(this.getFont(), Color.WHITE);

        Label nameHeader = new Label("Name", whiteText);
        Label levelHeader = new Label("Level Max", whiteText);
        Label scoreHeader = new Label("Total Score", whiteText);

        nameHeader.setFontScale(0.8f);
        levelHeader.setFontScale(0.8f);
        scoreHeader.setFontScale(0.8f);

        scrollableContent.add(nameHeader).width(200).pad(10).left();
        scrollableContent.add(levelHeader).width(200).pad(10).right();
        scrollableContent.add(scoreHeader).width(200).pad(10).right();
        scrollableContent.row();

        for (int i = 0; i < data.size(); i++) {
            Label.LabelStyle textStyle;

            if (i == 0) {
                textStyle = new Label.LabelStyle(this.getFont(), Color.GOLD);
            } else if (i == 1) {
                textStyle = new Label.LabelStyle(this.getFont(), Color.LIGHT_GRAY);
            } else if (i == 2) {
                textStyle = new Label.LabelStyle(this.getFont(), Color.ORANGE);
            } else {
                textStyle = whiteText;
            }

            String[] line = data.get(i).split(",");
            Label name = new Label(line[0], textStyle);
            Label level = new Label(line[1], textStyle);
            Label score = new Label(line[2], textStyle);

            name.setFontScale(0.7f);
            level.setFontScale(0.7f);
            score.setFontScale(0.7f);

            scrollableContent.add(name).width(200).pad(5).left();
            scrollableContent.add(level).width(200).pad(5).right();
            scrollableContent.add(score).width(200).pad(5).right();
            scrollableContent.row();
        }
    }
}
