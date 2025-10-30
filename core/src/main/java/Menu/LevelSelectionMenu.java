package Menu;

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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.main.Game;
import com.main.GameState;
import com.main.Main;
import entity.Player;
import table.LevelDataHandler;
import Menu.MainMenu;

import static com.main.GameState.SELECT_MODE;

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
        Label.LabelStyle whiteText = new Label.LabelStyle(this.getFont(), Color.WHITE);
        Label.LabelStyle ButtonLabelStyle = new Label.LabelStyle(this.getFont(), Color.WHITE);
        ButtonLabelStyle.font.getData().setScale(0.6f);
        Label.LabelStyle LockedLabelStyle = new Label.LabelStyle(this.getFont(), Color.GRAY);
        LockedLabelStyle.font.getData().setScale(0.6f);

        Label titleLabel = new Label("CHOOSE LEVEL", whiteText);
        titleLabel.setFontScale(1f);
        mainTable.add(titleLabel).padTop(-130).padBottom(70);
        mainTable.row();

        Main main = this.getMain();
        String playerName = this.getPlayer().getName();
        int maxLevel = LevelDataHandler.getPlayerMaxLevel(playerName);

        int buttonWidth = 120;
        int buttonHeight = 50;
        int verticalPadding = 40;
        int horizontalPadding = 70;

        GameState[] levels = {
            GameState.LEVEL1, GameState.LEVEL2, GameState.LEVEL3,
            GameState.LEVEL4, GameState.LEVEL5
        };

//        Button Back = new Button(this.getSkin());
//        Back.addListener(new ClickListener() {
//            public void clicked(InputEvent event, float x, float y) {
//                main.setGameState(SELECT_MODE);
//            }
//        });
//        Back.setPosition(670, 10);
//        Back.setSize(buttonWidth, buttonHeight);
//        this.getStage().addActor(Back);

        Table buttonTable1 = new Table();

        final Color DEFAULT_COLOR = Color.WHITE;
        final Color HOVER_COLOR = Color.YELLOW;

// --- Level 1 ---
        Label levelButton1 = new Label("Level 1", whiteText);
        if (1 <= maxLevel) {
            levelButton1.setColor(DEFAULT_COLOR);
            levelButton1.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    Game.playSfx(Game.sfx_click,0.8f);
                    main.setGameState(levels[0]);
                }
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    levelButton1.setColor(HOVER_COLOR);
                }
                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    levelButton1.setColor(DEFAULT_COLOR);
                }
            });
        } else {
            levelButton1.setColor(Color.GRAY);
        }
        buttonTable1.add(levelButton1).width(buttonWidth).height(buttonHeight)
            .padRight(horizontalPadding);

// --- Level 2 ---
        Label levelButton2 = new Label("Level 2", whiteText);
        if (2 <= maxLevel) {
            levelButton2.setColor(DEFAULT_COLOR);
            levelButton2.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Game.playSfx(Game.sfx_click,0.8f);
                    main.setGameState(levels[1]);
                }
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    levelButton2.setColor(HOVER_COLOR);
                }
                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    levelButton2.setColor(DEFAULT_COLOR);
                }
            });
        } else {
            levelButton2.setColor(Color.GRAY);
        }
        buttonTable1.add(levelButton2).width(buttonWidth).height(buttonHeight)
            .padRight(horizontalPadding);

// --- Level 3 ---
        Label levelButton3 = new Label("Level 3", whiteText);
        if (3 <= maxLevel) {
            levelButton3.setColor(DEFAULT_COLOR);
            levelButton3.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Game.playSfx(Game.sfx_click,0.8f);
                    main.setGameState(levels[2]);
                }
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    levelButton3.setColor(HOVER_COLOR);
                }
                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    levelButton3.setColor(DEFAULT_COLOR);
                }
            });
        } else {
            levelButton3.setColor(Color.GRAY);
        }
        buttonTable1.add(levelButton3).width(buttonWidth).height(buttonHeight);

        mainTable.add(buttonTable1).padBottom(verticalPadding);
        mainTable.row();

        Table buttonTable2 = new Table();

// --- Level 4 ---
        Label levelButton4 = new Label("Level 4", whiteText);
        if (4 <= maxLevel) {
            levelButton4.setColor(DEFAULT_COLOR);
            levelButton4.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Game.playSfx(Game.sfx_click,0.8f);
                    main.setGameState(levels[3]);
                }
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    levelButton4.setColor(HOVER_COLOR);
                }
                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    levelButton4.setColor(DEFAULT_COLOR);
                }
            });
        } else {
            levelButton4.setColor(Color.GRAY);
        }
        buttonTable2.add(levelButton4).width(buttonWidth).height(buttonHeight)
            .padRight(horizontalPadding);

// --- Level 5 ---
        Label levelButton5 = new Label("Level 5", whiteText);
        if (5 <= maxLevel) {
            levelButton5.setColor(DEFAULT_COLOR);
            levelButton5.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Game.playSfx(Game.sfx_click,0.8f);
                    main.setGameState(levels[4]);
                }
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    levelButton5.setColor(HOVER_COLOR);
                }
                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    levelButton5.setColor(DEFAULT_COLOR);
                }
            });
        } else {
            levelButton5.setColor(Color.GRAY);
        }
        buttonTable2.add(levelButton5).width(buttonWidth).height(buttonHeight);

        mainTable.add(buttonTable2);

        //Back Button
        Label backButton = new Label("Return", whiteText);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Game.playSfx(Game.sfx_back,1.0f);
                main.setGameState(GameState.SELECT_MODE);
            }
        });

        backButton.setPosition(0, 0);
        backButton.setFontScale(0.6f);
        this.getStage().addActor(backButton);

    }
}
