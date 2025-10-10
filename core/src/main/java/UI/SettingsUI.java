package ui;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.main.GameState;
import com.main.Main;

import static com.badlogic.gdx.Gdx.gl;

public class SettingsUI extends ApplicationAdapter {

    private Stage stage;
    private Main main;
    private Skin skin;
    private BitmapFont font;

    public SettingsUI(Main main) {
        this.main = main;
    }

    @Override
    public void create() {
        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        font = new BitmapFont(Gdx.files.internal("ui/F_Retro.fnt"));
        skin = new Skin();
        skin.add("default-font", font);

        // Create a texture for the slider background and knob
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        // Label Style
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        skin.add("default", labelStyle);

        // Title Label Style
        Label.LabelStyle titleLabelSyle = new Label.LabelStyle(font, Color.WHITE);
        titleLabelSyle.font.getData().setScale(2.0f);
        skin.add("title", titleLabelSyle);

        // Slider Style
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = skin.newDrawable("white", Color.DARK_GRAY);
        sliderStyle.knob = skin.newDrawable("white", Color.GRAY);
        skin.add("default-horizontal", sliderStyle);

        // TextButton Style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = font;
        skin.add("default", textButtonStyle);


        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Title
        Label titleLabel = new Label("Settings", skin, "title");
        table.add(titleLabel).padBottom(50).colspan(2);
        table.row();

        // Music Slider
        Label musicLabel = new Label("Music", skin);
        table.add(musicLabel).padRight(20);
        Slider musicSlider = new Slider(0, 100, 1, false, skin);
        table.add(musicSlider).width(300);
        table.row().padTop(20);

        // SFX Slider
        Label sfxLabel = new Label("SFX", skin);
        table.add(sfxLabel).padRight(20);
        Slider sfxSlider = new Slider(0, 100, 1, false, skin);
        table.add(sfxSlider).width(300);
        table.row().padTop(50);

        // Back Button
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setGameState(GameState.MAIN_MENU);
            }
        });
        table.add(backButton).colspan(2).width(200).height(80);
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
        font.dispose();
    }

    public Stage getStage() {
        return stage;
    }
}
