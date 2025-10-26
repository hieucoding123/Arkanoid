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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.main.GameState;
import com.main.Main;
import entity.Player;

import static com.badlogic.gdx.Gdx.gl;

public class MainMenu extends UserInterface {
    private TextField textField;
    public MainMenu(Main main, Player player) {
        super(main, player);
    }

    @Override
    public void create() {
        this.setBackground(new Texture(Gdx.files.internal("ui/bg.png")));
        this.setSkin(new Skin(Gdx.files.internal("ui-skin/ui-skin.json")));
        this.setStage(new Stage(
            new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()))
        );

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
        Label.LabelStyle LBStyle = new Label.LabelStyle(this.getFont(), Color.YELLOW);

        // --- Title Label ---
        Label titleLabel = new Label("Welcome to Arkanoid!", MenuText);
        titleLabel.setFontScale(1f);
        mainTable.add(titleLabel).padBottom(40).padTop(30);
        mainTable.row();

        //Button Table
        Table buttonTable = new Table();

        Main main = this.getMain();

        //Play Button
        Button playButton = new Button(this.getSkin());
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setGameState(GameState.SELECT_MODE);
            }
        });

        // Leaderboard button
        Button lbButton = new Button(this.getSkin());
        lbButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setGameState(GameState.LEADER_BOARD);
            }
        });

        //Setting Button
        Label settingsLabel = new Label("Settings", MenuText);
        settingsLabel.setFontScale(0.8f);
        settingsLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setGameState(GameState.SETTINGS);
                System.out.println("Settings Clicked");
            }
        });

        // Text Field to get player's name
        this.textField = new TextField("Enter name", this.getSkin());

        //Quit Button
        Label quitLabel = new Label("Quit", MenuText);
        quitLabel.setFontScale(0.8f);
        quitLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        //Arrange the Buttons in the buttonTable
        buttonTable.add(playButton).width(120).height(50).padBottom(40);
        buttonTable.row();
        buttonTable.add(lbButton).width(60).height(30).padBottom(20);
        buttonTable.row();
        buttonTable.add(textField).width(300).height(50).padBottom(40);
        buttonTable.row();
        buttonTable.add(settingsLabel).padBottom(50);
        buttonTable.row();
        buttonTable.add(quitLabel).padBottom(50);

        //Add to main table
        mainTable.add(buttonTable);
    }

    @Override
    public void render() {
        super.render();
        // Updates player's name
        this.getPlayer().setName(textField.getText());
    }
}
