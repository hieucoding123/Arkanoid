package Menu.leaderboard;

import Menu.UserInterface;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
//import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.main.Game;
import com.main.GameState;
import com.main.Main;
import entity.Player;
import table.InfiDataHandler;

import java.util.ArrayList;

public class LeaderBoard extends UserInterface {
    private Table scrollableContent;
    private String showLeaderBoard = "";
    private ArrayList<String> data;
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
        Label coopModeButton = new Label("Co-op", whiteText);
        Label infiModeButton = new Label("Infinite", whiteText);
        Label levelsModeButton = new Label("Singleplayer", whiteText);

        //Inf button
        infiModeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Game.playSfx(Game.sfx_click,0.8f);
                showLeaderBoard = "INFINITE";
                scrollableContent.clear();
                infiModeButton.setColor(Color.YELLOW);
                levelsModeButton.setColor(Color.WHITE);
                coopModeButton.setColor(Color.WHITE);
            }
        });

        //Singleplayer button
        levelsModeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Game.playSfx(Game.sfx_click,0.8f);
                showLeaderBoard = "LEVELS";
                scrollableContent.clear();
                levelsModeButton.setColor(Color.YELLOW);
                coopModeButton.setColor(Color.WHITE);
                infiModeButton.setColor(Color.WHITE);
            }
        });

        //Coop button
        coopModeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Game.playSfx(Game.sfx_click,0.8f);
                showLeaderBoard = "COOP";
                scrollableContent.clear();
                coopModeButton.setColor(Color.YELLOW);
                infiModeButton.setColor(Color.WHITE);
                levelsModeButton.setColor(Color.WHITE);
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

        mainTable.add(scrollPane).expandY().width(800).height(400).pad(20);
        mainTable.row();

        //Back Button
        Label backButton = new Label("Return", whiteText);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                com.main.Game.sfx_back.play(1.0f);
                main.setGameState(GameState.MAIN_MENU);
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                backButton.setColor(Color.YELLOW);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                backButton.setColor(Color.WHITE);
            }
        });

        backButton.setPosition(10, 0);
        backButton.setFontScale(0.6f);
        this.getStage().addActor(backButton);

        mainTable.add(buttonTable);
    }

    public void update() {
        if (showLeaderBoard.equals("INFINITE")) {
            loadInfiniteTable();
            showLeaderBoard = "";
        }
    }

    private void loadInfiniteTable() {
        data = InfiDataHandler.getLeaderboardData();
        Label.LabelStyle whiteText = new Label.LabelStyle(this.getFont(), Color.WHITE);
        Label nameHeader = new Label("Name", whiteText);
        Label scoreHeader = new Label("Score", whiteText);
        Label timeHeader = new Label("Time Played", whiteText);

        nameHeader.setFontScale(0.8f);
        scoreHeader.setFontScale(0.8f);
        timeHeader.setFontScale(0.8f);

        scrollableContent.add(nameHeader).width(200).pad(10).left();
        scrollableContent.add(scoreHeader).width(150).pad(10).right();
        scrollableContent.add(timeHeader).width(150).pad(10).right();
        scrollableContent.row();

        for (int i = 0; i < data.size(); i++) {
            Label.LabelStyle textStyle;

            if (i == 0) {
                textStyle = new Label.LabelStyle(this.getFont(), Color.GOLD); // Top 1
            } else if (data.size() > 1 && i == 1) {
                textStyle = new Label.LabelStyle(this.getFont(), Color.LIGHT_GRAY); // Top 2
            } else if (data.size() > 2 && i == 2) {
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
            scrollableContent.add(score).width(150).pad(5).right();
            scrollableContent.add(time).width(150).pad(5).right();
            scrollableContent.row();
        }
    }
}
