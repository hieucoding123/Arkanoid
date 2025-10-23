package Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.main.GameState;
import com.main.Main;
import entity.Player;

public class LevelSelectionMenu extends UserInterface {

    public LevelSelectionMenu(Main main, Player player) {
        super(main, player);
    }

    @Override
    public void create() {
        this.setBackground(new Texture(Gdx.files.internal("ui/bg.png")));
        this.setSkin(new Skin(Gdx.files.internal("ui/buttontest.json")));
        this.setStage(new Stage(new ExtendViewport(
            Gdx.graphics.getWidth(), Gdx.graphics.getHeight()))
        );

        setFont(new BitmapFont(Gdx.files.internal("ui/F_Retro.fnt")));
        this.getFont().getData().setScale(1);

        Gdx.input.setInputProcessor(this.getStage());

        //Table
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        this.getStage().addActor(mainTable);

        //Background
        mainTable.setBackground(
            new TextureRegionDrawable(
                new TextureRegion(this.getBGTexture()
                )
            )
        );

        //Label Styles
        Label.LabelStyle MenuText = new Label.LabelStyle(this.getFont(), Color.WHITE);
        Label.LabelStyle LBStyle = new Label.LabelStyle(this.getFont(), Color.YELLOW);

        // --- Title Label ---
        Label titleLabel = new Label("CHOOSE LEVEL", MenuText);
        titleLabel.setFontScale(1f);
        mainTable.add(titleLabel).padBottom(40).padTop(30);
        mainTable.row();

        //Button Table
        Table buttonTable = new Table();

        Main main = this.getMain();

        //InfiniteMode Button
        Button Level1 = new Button(this.getSkin());
        Level1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setGameState(GameState.LEVEL1);
            }
        });
        // LevelsMode Button
        Button Level2 = new Button(this.getSkin());
        Level2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setGameState(GameState.LEVEL2);
//                main.setGameState(GameState.LEVELS_MODE);
            }
        });

        Button Level3 = new Button(this.getSkin());
        Level3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setGameState(GameState.LEVEL3);
//                main.setGameState(GameState.LEVELS_MODE);
            }
        });

        Button Level4 = new Button(this.getSkin());
        Level4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setGameState(GameState.LEVEL4);
//                main.setGameState(GameState.LEVELS_MODE);
            }
        });

        Button Level5 = new Button(this.getSkin());
        Level5.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setGameState(GameState.LEVEL5);
//                main.setGameState(GameState.LEVELS_MODE);
            }
        });

        //Arrange the Buttons in the buttonTable
        buttonTable.add(Level1).width(120).height(50).padBottom(40);
        buttonTable.row();

        buttonTable.add(Level2).width(120).height(50).padBottom(40);
        buttonTable.row();

        buttonTable.add(Level3).width(120).height(50).padBottom(40);
        buttonTable.row();

        buttonTable.add(Level4).width(120).height(50).padBottom(40);
        buttonTable.row();

        buttonTable.add(Level5).width(120).height(50).padBottom(40);
        buttonTable.row();

        //Add to main table
        mainTable.add(buttonTable);
    }
}
