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

public class ModeMenu extends UserInterface {

    private Skin infiniteSkin;
    private Skin levelsSkin;
    private Skin coopSkin;
    private Skin vsModeSkin;
    private Skin ui_skin;

    public ModeMenu(Main main, Player player) {
        super(main, player);
    }

    @Override
    public void create() {
        this.setBackground(new Texture(Gdx.files.internal("ui/bg.png")));

        //Load skin
        this.infiniteSkin = new Skin(Gdx.files.internal("ui/infbutton.json"));
        this.levelsSkin = new Skin(Gdx.files.internal("ui/Singleplayerbutton.json"));
        this.coopSkin = new Skin(Gdx.files.internal("ui/CoopButton.json"));
        this.vsModeSkin = new Skin(Gdx.files.internal("ui/vsmodebutton.json"));
        this.ui_skin = new Skin(Gdx.files.internal("ui-skin/ui-skin.json"));
        //Set def skin
        this.setSkin(this.infiniteSkin);

        this.setStage(new Stage(new FitViewport(800, 1000)));

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
        Label.LabelStyle whiteText = new Label.LabelStyle(this.getFont(), Color.WHITE);
        Label.LabelStyle LBStyle = new Label.LabelStyle(this.getFont(), Color.YELLOW);

        //Title Label
        Label titleLabel = new Label("Select your mode", whiteText);
        titleLabel.setFontScale(1f);
        mainTable.add(titleLabel).padBottom(40).padTop(30);
        mainTable.row();

        //Button Table
        Table buttonTable = new Table();

        Main main = this.getMain();

        //InfiniteMode Button
        Button infiModeButton = new Button(this.infiniteSkin);
        infiModeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Game.playSfx(Game.sfx_click,0.8f);
                main.setGameState(GameState.INFI_MODE);
            }
        });

        //Singleplayer button
        Button levelsModeButton = new Button(this.levelsSkin);
        levelsModeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Game.playSfx(Game.sfx_click,0.8f);
                main.setLevelSelectionMode(false);
            }
        });

        //Coop button
        Button coopModeButton = new Button(this.coopSkin);
        coopModeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Game.playSfx(Game.sfx_click,0.8f);
                main.setLevelSelectionMode(true);
            }
        });

        //Vs button
        Button vsModeButton = new Button(this.vsModeSkin);
        vsModeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Game.playSfx(Game.sfx_click,0.8f);
                //System.out.println("VS MODE");
                main.setGameState(GameState.NETWORK_CONNECTION_MENU);
            }
        });

        //Arrange the Buttons in the buttonTable
        buttonTable.add(levelsModeButton).width(220).height(72).padBottom(20);
        buttonTable.row();

        buttonTable.add(coopModeButton).width(220).height(72).padBottom(20);
        buttonTable.row();

        buttonTable.add(infiModeButton).width(220).height(72).padBottom(20);
        buttonTable.row();

        buttonTable.add(vsModeButton).width(220).height(72).padBottom(20);
        buttonTable.row();

        //Back Button
        Label backButton = new Label("Return", whiteText);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Game.playSfx(Game.sfx_back,1.0f);
                main.setGameState(GameState.MAIN_MENU);
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                backButton.setColor(Color.YELLOW);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                backButton.setColor(Color.WHITE);
            }
        });

        backButton.setPosition(10, 0);
        backButton.setFontScale(0.6f);
        this.getStage().addActor(backButton);

        //Add to main table
        mainTable.add(buttonTable);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (levelsSkin != null) {
            levelsSkin.dispose();
        }
        if (coopSkin != null) {
            coopSkin.dispose();
        }
    }
}
