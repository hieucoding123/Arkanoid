package Menu;

import Menu.UserInterface;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.main.Game;
import com.main.GameState;
import com.main.Main;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import entity.Player;


public class SettingsUI extends UserInterface {
    public SettingsUI(Main main, Player player) {
        super(main, player);
    }

    @Override
    public void create() {
        this.setStage(
            new Stage(new FitViewport(800, 1000))
        );
        this.setBackground(new Texture(Gdx.files.internal("ui/bg.png")));
        this.setFont(new BitmapFont(Gdx.files.internal("ui/F_Retro.fnt")));
        this.setSkin(new Skin());
        this.getSkin().add("default-font", this.getFont());

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        this.getSkin().add("white", new Texture(pixmap));

        // Label Style
        Label.LabelStyle labelStyle = new Label.LabelStyle(this.getFont(), Color.WHITE);
        this.getSkin().add("default", labelStyle);

        // Title Label Style (with new font instance)
        BitmapFont titleFont = new BitmapFont(Gdx.files.internal("ui/F_Retro.fnt"));
        titleFont.getData().setScale(2.0f);
        Label.LabelStyle titleLabelStyle = new Label.LabelStyle(titleFont, Color.WHITE);
        this.getSkin().add("title", titleLabelStyle);

        // Slider Style
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = this.getSkin().newDrawable("white", Color.DARK_GRAY);
        Drawable knobDrawable = this.getSkin().newDrawable("white", Color.GRAY);
        knobDrawable.setMinWidth(20f);
        knobDrawable.setMinHeight(20f);
        sliderStyle.knob = knobDrawable;
        this.getSkin().add("default-horizontal", sliderStyle);

        // TextButton Style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = this.getSkin().newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = this.getSkin().newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = this.getFont();
        this.getSkin().add("default", textButtonStyle);


        Table table = new Table();
        table.setFillParent(true);
        this.getStage().addActor(table);

        // Title
        Label titleLabel = new Label("Settings", this.getSkin(), "title");
        table.add(titleLabel).padBottom(50).colspan(2);
        table.row();

        // Music Slider
        Label musicLabel = new Label("Music", this.getSkin());
        table.add(musicLabel).padRight(20);
        Slider musicSlider = new Slider(0, 100, 1, false, this.getSkin());
        table.add(musicSlider).width(300);
        table.row().padTop(20);

        musicSlider.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                float stageX = event.getStageX();
                float stageY = event.getStageY();
                System.out.println("Slider clicked at Stage coordinates: (" + stageX + ", " + stageY + ")");
                System.out.println("Slider's bottom-left corner is at: (" + musicSlider.getX() + ", " + musicSlider.getY() + ")");
                return true;
            }
        });

        musicSlider.setValue(Game.musicVolumePercent);
        musicSlider.setVisualPercent(Game.musicVolumePercent);

        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Game.musicVolumePercent = musicSlider.getValue()/100;
                Game.updateMusicVolume();
            }
        });

        // SFX Slider
        Label sfxLabel = new Label("SFX", this.getSkin());
        table.add(sfxLabel).padRight(20);
        Slider sfxSlider = new Slider(0, 100, 1, false, this.getSkin());
        table.add(sfxSlider).width(300);
        table.row().padTop(50);

        sfxSlider.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                float stageX = event.getStageX();
                float stageY = event.getStageY();
                System.out.println("Slider clicked at Stage coordinates: (" + stageX + ", " + stageY + ")");
                System.out.println("Slider's bottom-left corner is at: (" + sfxSlider.getX() + ", " + sfxSlider.getY() + ")");
                return true;
            }
        });

        // Starting pos
        sfxSlider.setValue(Game.sfxVolumePercent);
        sfxSlider.setVisualPercent(Game.sfxVolumePercent);

        sfxSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // When this slider moves, update the SFX static field
                Game.sfxVolumePercent = sfxSlider.getValue()/100;
            }
        });

        sfxSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!sfxSlider.isDragging()) {
                    Game.playSfx(Game.sfx_click);
                }
            }
        });



        // Main
        Main main = this.getMain();

        //Back Button
        Label backButton = createClickableLabel(
            "Back",
            Game.sfx_back,
            1.0f,
            () -> main.setGameState(GameState.MAIN_MENU)
        );

        table.add(backButton).colspan(2).padTop(15).padBottom(15).padLeft(50).padRight(50);
    }
}
