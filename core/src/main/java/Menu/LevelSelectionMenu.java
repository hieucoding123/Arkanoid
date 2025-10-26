package Menu;

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
import com.main.GameState;
import com.main.Main;
import entity.Player;
import table.LevelDatabase;

public class LevelSelectionMenu extends UserInterface {

    public LevelSelectionMenu(Main main, Player player) {
        super(main, player);
    }

    @Override
    public void create() {
        this.setBackground(new Texture(Gdx.files.internal("ui/bg.png")));
        this.setSkin(new Skin(Gdx.files.internal("ui/buttontest.json")));
        this.setStage(new Stage(new FitViewport(800, 1000)));

        setFont(new BitmapFont(Gdx.files.internal("ui/F_Retro.fnt")));
        this.getFont().getData().setScale(1);

        Gdx.input.setInputProcessor(this.getStage());

        //Table
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        this.getStage().addActor(mainTable);

        //Background
        mainTable.setBackground( new TextureRegionDrawable( new TextureRegion(this.getBGTexture())));

        //Label Styles
        Label.LabelStyle MenuText = new Label.LabelStyle(this.getFont(), Color.WHITE);
        Label.LabelStyle ButtonLabelStyle = new Label.LabelStyle(this.getFont(), Color.WHITE);
        ButtonLabelStyle.font.getData().setScale(0.6f);
        Label.LabelStyle LockedLabelStyle = new Label.LabelStyle(this.getFont(), Color.GRAY);
        LockedLabelStyle.font.getData().setScale(0.6f);

        Label titleLabel = new Label("CHOOSE LEVEL", MenuText);
        titleLabel.setFontScale(1f);
        mainTable.add(titleLabel).padTop(-130).padBottom(70);
        mainTable.row();

        Main main = this.getMain();
        String playerName = this.getPlayer().getName();
        int maxLevel = LevelDatabase.getPlayerMaxLevel(playerName);

        int buttonWidth = 120;
        int buttonHeight = 50;
        int verticalPadding = 40;
        int horizontalPadding = 70;

        GameState[] levels = {
            GameState.LEVEL1, GameState.LEVEL2, GameState.LEVEL3,
            GameState.LEVEL4, GameState.LEVEL5
        };

        Table buttonTable1 = new Table();

        Button levelButton1 = new Button(this.getSkin());
        if (1 <= maxLevel) {
            levelButton1.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    main.setGameState(levels[0]);
                }
            });
        } else {
            levelButton1.setDisabled(true);
        }
        buttonTable1.add(levelButton1).width(buttonWidth).height(buttonHeight)
            .padRight(horizontalPadding);

        Button levelButton2 = new Button(this.getSkin());
        if (2 <= maxLevel) {
            levelButton2.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    main.setGameState(levels[1]);
                }
            });
        } else {
            levelButton2.setDisabled(true);
        }
        buttonTable1.add(levelButton2).width(buttonWidth).height(buttonHeight)
            .padRight(horizontalPadding);

        Button levelButton3 = new Button(this.getSkin());
        if (3 <= maxLevel) {
            levelButton3.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    main.setGameState(levels[2]);
                }
            });
        } else {
            levelButton3.setDisabled(true);
        }
        buttonTable1.add(levelButton3).width(buttonWidth).height(buttonHeight);

        mainTable.add(buttonTable1).padBottom(verticalPadding);
        mainTable.row();

        Table buttonTable2 = new Table();

        Button levelButton4 = new Button(this.getSkin());
        if (4 <= maxLevel) {
            levelButton4.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    main.setGameState(levels[3]);
                }
            });
        } else {
            levelButton4.setDisabled(true);
        }
        buttonTable2.add(levelButton4).width(buttonWidth).height(buttonHeight)
            .padRight(horizontalPadding);

        Button levelButton5 = new Button(this.getSkin());
        if (5 <= maxLevel) {
            levelButton5.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    main.setGameState(levels[4]);
                }
            });
        } else {
            levelButton5.setDisabled(true);
        }
        buttonTable2.add(levelButton5).width(buttonWidth).height(buttonHeight);

        mainTable.add(buttonTable2);
    }
}
