package ui;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL32;
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

import static com.badlogic.gdx.Gdx.gl;

public class ModeMenu extends ApplicationAdapter {
    private Stage stage;
    private Skin skin;
    private Main main;
    private Texture bg;
    private BitmapFont font;

    public ModeMenu(Main main) {
        this.main = main;
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void create() {
        bg = new Texture(Gdx.files.internal("ui/bg.png"));
        skin = new Skin(Gdx.files.internal("ui/buttontest.json"));
        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        font = new BitmapFont(Gdx.files.internal("ui/F_Retro.fnt"));
        font.getData().setScale(1);

        Gdx.input.setInputProcessor(stage);

        //Table
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        //Background
        mainTable.setBackground(new TextureRegionDrawable(new TextureRegion(bg)));

        //Label Styles
        Label.LabelStyle MenuText = new Label.LabelStyle(font, Color.WHITE);
        Label.LabelStyle LBStyle = new Label.LabelStyle(font, Color.YELLOW);

        // --- Title Label ---
        Label titleLabel = new Label("Welcome to Arkanoid!", MenuText);
        titleLabel.setFontScale(1f);
        mainTable.add(titleLabel).padBottom(40).padTop(30);
        mainTable.row();

        //Button Table
        Table buttonTable = new Table();

        //InfiniteMode Button
        Button infiModeButton = new Button(skin);
        infiModeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setGameState(GameState.INFI_MODE);
            }
        });

        // LevelsMode Button
        Button levelsModeButton = new Button(skin);
        levelsModeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setGameState(GameState.LEVELS_MODE);
            }
        });

        //Arrange the Buttons in the buttonTable
        buttonTable.add(infiModeButton).width(120).height(50).padBottom(40);
        buttonTable.row();

        buttonTable.add(levelsModeButton).width(120).height(50).padBottom(40);
        buttonTable.row();

        //Add to main table
        mainTable.add(buttonTable);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render() {
        gl.glClearColor(0, 0, 0, 1);
        gl.glClear(GL32.GL_COLOR_BUFFER_BIT);

        stage.getViewport().apply();
        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        bg.dispose();
    }
}
