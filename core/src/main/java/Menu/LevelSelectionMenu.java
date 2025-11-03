package Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.main.Game;
import com.main.GameState;
import com.main.Main;
import entity.Player;
import datahandler.LevelDataHandler;

/**
 * Base level selection menu providing common level selection UI functionality.
 *
 * <p>This class provides the core level selection interface with buttons for
 * each campaign level. Levels are locked/unlocked based on player progression.
 * Extended by single-player and co-op specific implementations that add
 * mode-specific statistics and data handling.</p>
 *
 * @see SinglePlayerLevelSelectionMenu Single-player implementation
 * @see CoopPlayerLevelSelectionMenu Co-op implementation
 * @see LevelDataHandler Player progression data
 * @see UserInterface Base menu class
 */
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

        Table buttonTable1 = new Table();

        // --- Level 1 ---
        Label levelButton1 = createClickableLabel(
            "Level 1",
            (1 <= maxLevel),
            () -> main.setGameState(levels[0])
        );
        buttonTable1.add(levelButton1).width(buttonWidth).height(buttonHeight)
            .padRight(horizontalPadding);

        // --- Level 2 ---
        Label levelButton2 = createClickableLabel(
            "Level 2",
            (2 <= maxLevel),
            () -> main.setGameState(levels[1])
        );
        buttonTable1.add(levelButton2).width(buttonWidth).height(buttonHeight)
            .padRight(horizontalPadding);

        // --- Level 3 ---
        Label levelButton3 = createClickableLabel(
            "Level 3",
            (3 <= maxLevel),
            () -> main.setGameState(levels[2])
        );
        buttonTable1.add(levelButton3).width(buttonWidth).height(buttonHeight);

        mainTable.add(buttonTable1).padBottom(verticalPadding);
        mainTable.row();

        Table buttonTable2 = new Table();

        // --- Level 4 ---
        Label levelButton4 = createClickableLabel(
            "Level 4",
            (4 <= maxLevel),
            () -> main.setGameState(levels[3])
        );
        buttonTable2.add(levelButton4).width(buttonWidth).height(buttonHeight)
            .padRight(horizontalPadding);

        // --- Level 5 ---
        Label levelButton5 = createClickableLabel(
            "Level 5",
            (5 <= maxLevel),
            () -> main.setGameState(levels[4])
        );
        buttonTable2.add(levelButton5).width(buttonWidth).height(buttonHeight);

        mainTable.add(buttonTable2);

        //Back Button
        Label backButton = createClickableLabel(
            "Return",
            Game.sfx_back,
            1.0f,
            () -> main.setGameState(GameState.SELECT_MODE)
        );

        backButton.setPosition(10, 0);
        backButton.setFontScale(0.6f);
        this.getStage().addActor(backButton);

    }
}
