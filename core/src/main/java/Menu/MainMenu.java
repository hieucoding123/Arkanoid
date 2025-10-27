package ui;

import Menu.UserInterface;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL32;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
//import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.main.GameState;
import com.main.Main;
import entity.Player;

import static com.badlogic.gdx.Gdx.gl;

public class MainMenu extends UserInterface {
    private TextField textField;
    private Skin textFieldSkin;

    //Button skins
    private Skin playButtonSkin;
    private Skin settingsButtonSkin;
    private Skin quitButtonSkin;

    //Texture
    private Texture Leaderboard;

    public MainMenu(Main main, Player player) {
        super(main, player);
    }

    @Override
    public void create() {
        this.setBackground(new Texture(Gdx.files.internal("ui/bg.png")));

        //Load all skins
        this.playButtonSkin = new Skin(Gdx.files.internal("ui/playButton.json"));
        this.settingsButtonSkin = new Skin(Gdx.files.internal("ui/settingbutton.json"));
        this.quitButtonSkin = new Skin(Gdx.files.internal("ui/quitButton.json"));
        this.textFieldSkin = new Skin(Gdx.files.internal("ui-skin/ui-skin.json"));

        //Image Button
        this.Leaderboard = new Texture(Gdx.files.internal("images/trophy.png"));

        //Set def skin
        this.setSkin(playButtonSkin);

        this.setStage(new Stage(new FitViewport(800, 1000)));

        this.setFont(new BitmapFont(Gdx.files.internal("ui/F_Retro.fnt")));
        this.getFont().getData().setScale(1);

        Gdx.input.setInputProcessor(this.getStage());

        //Table
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        this.getStage().addActor(mainTable);

        //Background
        mainTable.setBackground(
            new TextureRegionDrawable(new TextureRegion(this.getBGTexture()))
        );

        //Label Styles
        Label.LabelStyle MenuText = new Label.LabelStyle(this.getFont(), Color.WHITE);

        //Title
        Label titleLabel = new Label("Welcome to Arkanoid!", MenuText);
        titleLabel.setFontScale(1f);
        mainTable.add(titleLabel).padBottom(40).padTop(30);
        mainTable.row();

        //Button Table
        Table buttonTable = new Table();

        Main main = this.getMain();

        //Play Button
        Button playButton = new Button(this.playButtonSkin);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setGameState(GameState.SELECT_MODE);
            }
        });

        //Setting Button
        Button settingsButton = new Button(this.settingsButtonSkin);
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setGameState(GameState.SETTINGS);
                System.out.println("Settings Clicked");
            }
        });

        //Enter name
        this.textField = new TextField("", this.textFieldSkin);
        this.textField.setMessageText("Enter name");
        this.textField.setAlignment(Align.center);

        //Quit Btn
        Button quitButton = new Button(this.quitButtonSkin);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        // LeaderBoard Button
        Image lbButton = new Image(this.Leaderboard);
        lbButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setGameState(GameState.LEADER_BOARD);
            }
        });

        //Button arrange
        buttonTable.add(textField).width(300).height(72).padBottom(40);
        buttonTable.row();
        buttonTable.add(playButton).width(220).height(72).padBottom(20);
        buttonTable.row();
        buttonTable.add(settingsButton).width(220).height(72).padBottom(20);
        buttonTable.row();
        buttonTable.add(quitButton).width(220).height(72).padBottom(20);

        lbButton.setPosition(750,0);
        lbButton.setSize(50,50);
        this.getStage().addActor(lbButton);

        //Add to main table
        mainTable.add(buttonTable);
    }

    @Override
    public void render() {
        super.render();
        // Updates player's name
        if (this.getPlayer() != null && this.textField != null) {
            this.getPlayer().setName(textField.getText());
        }
    }

    // Clean up all loaded skins
    @Override
    public void dispose() {
        super.dispose(); // Disposes the main skin (playButtonSkin)
        if (textFieldSkin != null) {
            textFieldSkin.dispose();
        }
        if (settingsButtonSkin != null) {
            settingsButtonSkin.dispose();
        }
        if (quitButtonSkin != null) {
            quitButtonSkin.dispose();
        }
    }
}
