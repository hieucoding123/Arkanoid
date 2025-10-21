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

import java.util.Scanner;


public class LoginGate extends UserInterface{

    public LoginGate(Main main, Player player) {
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

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        this.getStage().addActor(mainTable);

        mainTable.setBackground(
            new TextureRegionDrawable(new TextureRegion(this.getBGTexture()))
        );

        //Label Styles
        Label.LabelStyle MenuText = new Label.LabelStyle(this.getFont(), Color.WHITE);

        Label titleLabel = new Label(this.getPlayer().getName(), MenuText);
        titleLabel.setFontScale(1f);
        mainTable.add(titleLabel).padBottom(40).padTop(30);
        mainTable.row();

        Table buttonTable = new Table();

        // Add button
        Main main = this.getMain();

        //Enter Button
        Button enterButton = new Button(this.getSkin());
        enterButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setGameState(GameState.SELECT_MODE);
            }
        });

        buttonTable.add(enterButton).width(120).height(50).padBottom(40);
        buttonTable.row();

        mainTable.add(buttonTable);

        // Add sub thread get player's name
        new Thread(new Runnable() {
            @Override
            public void run() {
                Scanner sc = new Scanner(System.in);
                final String newPartOfName = sc.nextLine();

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        if (!newPartOfName.equals("")) {
                            String currentName = getPlayer().getName() != null ? getPlayer().getName() : "";
                            String newName = currentName + newPartOfName;
                            getPlayer().setName(newName);
                        }
                    }
                });
            }
        }).start();
    }
}
