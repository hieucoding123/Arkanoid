// Arkanoid/core/src/main/java/ui/UI.java

package ui;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL32;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.main.GameState;
import com.main.Main;

import static com.badlogic.gdx.Gdx.gl;

public class UI extends ApplicationAdapter {

    private Stage stage;
    private Skin skin;
    private Main main;
    private Texture bg;
    private BitmapFont font;

    public UI(Main main) {
        this.main = main;
    }

    @Override
    public void create() {
        bg = new Texture(Gdx.files.internal("ui/bg.png"));
        skin = new Skin(Gdx.files.internal("ui/buttontest.json"));
        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        font = new BitmapFont(Gdx.files.internal("ui/F_Retro.fnt"));
        font.getData().setScale(1);

        Gdx.input.setInputProcessor(stage);

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        mainTable.setBackground(new TextureRegionDrawable(new TextureRegion(bg)));

        Label.LabelStyle placeholderstyle = new Label.LabelStyle(font, Color.WHITE);
        Label placeholder = new Label("Welcome to Arkanoid!", placeholderstyle);
        mainTable.add(placeholder).padBottom(50);
        mainTable.row();

        Button button = new Button(skin);
        mainTable.add(button).center().width(320).height(100);

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setGameState(GameState.PLAYING);

            }
        });
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

    public Stage getStage() {
        return stage;
    }
}
