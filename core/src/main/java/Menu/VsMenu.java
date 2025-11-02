package Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

public class VsMenu extends UserInterface{
    private TextButton.TextButtonStyle textOnlyButtonStyle;
    public VsMenu(Main main, Player player) {
        super(main, player);
    }

    @Override
    public void create() {
        this.setBackground(new Texture(Gdx.files.internal("ui/bg.png")));

        this.setStage(new Stage(
            new FitViewport(800, 1000))
        );

        this.setFont(new BitmapFont(Gdx.files.internal("ui/F_Retro.fnt")));
        this.getFont().getData().setScale(1);

        this.setSkin(new Skin(Gdx.files.internal("ui-skin/ui-skin.json")));
        this.getSkin().add("default-font", this.getFont());

        Gdx.input.setInputProcessor(this.getStage());

        textOnlyButtonStyle = new TextButton.TextButtonStyle();
        textOnlyButtonStyle.font = this.getFont();
        textOnlyButtonStyle.up = null;
        textOnlyButtonStyle.down = null;
        textOnlyButtonStyle.over = null;
        textOnlyButtonStyle.disabled = null;

        textOnlyButtonStyle.fontColor = Color.WHITE;         // Normal
        textOnlyButtonStyle.overFontColor = Color.YELLOW;    // Hover
        textOnlyButtonStyle.downFontColor = Color.YELLOW;    // Click
        textOnlyButtonStyle.disabledFontColor = Color.GRAY;  // Disable

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        this.getStage().addActor(mainTable);

        mainTable.setBackground(
            new TextureRegionDrawable(new TextureRegion(this.getBGTexture()))
        );

        Label title = new Label("1 VS 1", this.getSkin());
        title.setFontScale(1.25f);
        title.setColor(Color.PURPLE);
        mainTable.add(title).padTop(10).padBottom(5).row();

        TextButton offlineButton = new TextButton("OFFLINE", textOnlyButtonStyle);
        offlineButton.setColor(Color.FIREBRICK);
        offlineButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent  event, float x, float y) {
                getMain().setGameState(GameState.VS_MODE);
            }
        });

        TextButton onlineButton = new TextButton("ONLINE", textOnlyButtonStyle);
        onlineButton.setColor(Color.OLIVE);
        onlineButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent  event, float x, float y) {
                getMain().setGameState(GameState.NETWORK_CONNECTION_MENU);
            }
        });
        mainTable.add(offlineButton).padTop(10).padBottom(5).row();
        mainTable.add(onlineButton).padTop(10).padBottom(5);

        Label backButton = createClickableLabel(
            "Return",
            Game.sfx_back,
            1.0f,
            () -> getMain().setGameState(GameState.MAIN_MENU)
        );

        backButton.setPosition(10, 0);
        backButton.setFontScale(0.6f);
        this.getStage().addActor(backButton);
    }
}
