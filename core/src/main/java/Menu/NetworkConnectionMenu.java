package Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.main.GameState;
import com.main.Main;
import com.main.network.IPDisplayDialog;
import com.main.network.NetworkProtocol;
import com.main.network.NetworkUtils;
import entity.Player;

public class NetworkConnectionMenu extends  UserInterface{
    private TextField textField;
    private Label statusLabel;
    private Label serverIPLabel;
    private TextButton showAllIPsButton;
    private boolean isHosting;

    public NetworkConnectionMenu(Main main, Player player) {
        super(main, player);
    }

    private void showIPDialog(Skin skin) {
        IPDisplayDialog dialog = new IPDisplayDialog("Server IP Information", skin);
        dialog.show(this.getStage());
    }

    @Override
    public void create() {
        this.setBackground(new Texture(Gdx.files.internal("ui/bg.png")));

        this.setStage(new Stage(
            new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()))
        );

        this.setFont(new BitmapFont(Gdx.files.internal("ui/F_Retro.fnt")));
        this.getFont().getData().setScale(1);

        this.setSkin(new Skin(Gdx.files.internal("ui-skin/ui-skin.json")));
        this.getSkin().add("default-font", this.getFont());

        Gdx.input.setInputProcessor(this.getStage());

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        this.getStage().addActor(mainTable);

        //Background
        mainTable.setBackground(
            new TextureRegionDrawable(new TextureRegion(this.getBGTexture()))
        );

        Label.LabelStyle LBStyle = new Label.LabelStyle(this.getFont(), Color.YELLOW);

        Label titleLabel = new Label("Network Multiplayer", LBStyle);
        titleLabel.setFontScale(1f);
        mainTable.add(titleLabel).padBottom(40).padTop(30);
        mainTable.row();

        Table buttonTable = new Table();

        TextButton hostButton = new TextButton("Host Game", this.getSkin());
        hostButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hostGame();
            }
        });

        serverIPLabel = new Label("", this.getSkin());
        serverIPLabel.setColor(Color.GREEN);
        serverIPLabel.setFontScale(1.0f);
        serverIPLabel.setVisible(false);        // invisible first
//        buttonTable.add(serverIPLabel).padBottom(40).row();

        showAllIPsButton = new TextButton("Show All Network IPs", this.getSkin());
        showAllIPsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showIPDialog(getSkin());
            }
        });
        showAllIPsButton.setVisible(false);

        this.textField = new TextField("", this.getSkin());
        textField.setMessageText("Enter server IP");

        TextButton joinButton = new TextButton("Join Game", this.getSkin());
        joinButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                joinGame();
            }
        });

        Main main = this.getMain();

        statusLabel = new Label("", this.getSkin());
        statusLabel.setColor(Color.YELLOW);
        buttonTable.add(statusLabel).padTop(20).row();

        TextButton backButton = new TextButton("Back", this.getSkin());
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setGameState(GameState.SELECT_MODE);
            }
        });
        buttonTable.add(hostButton).padBottom(40).padTop(30).row();
        buttonTable.add(serverIPLabel).padBottom(40).row();
        buttonTable.add(showAllIPsButton).width(250).height(40).padBottom(20).row();
        buttonTable.add(textField).padBottom(40).padTop(30).row();
        buttonTable.add(joinButton).padBottom(40).padTop(30).row();
        buttonTable.add(backButton).padBottom(40).padTop(30).row();

        mainTable.add(buttonTable);
    }

    // Join game -> Client
    private void joinGame() {
        String severIP = textField.getText();
        if (severIP.isEmpty()) {
            statusLabel.setText("Please enter server IP");
            return;
        }

        statusLabel.setText("Connecting to " + severIP + " sever");

        new Thread(() -> {
            try {
                Thread.sleep(500);      // Connection delay
                Gdx.app.postRunnable(() -> {
                    statusLabel.setText("Connected! Staring game...");
                    getMain().startNetworkGame(severIP, false);
                });
            } catch (Exception e) {
                Gdx.app.postRunnable(() -> {
                    statusLabel.setText("Failed to connect!");
                });
            }
        }).start();
    }

    private void
    hostGame() {
        statusLabel.setText("Staring sever...");
        isHosting = true;

        new Thread(() -> {
            try {
                String localIP = NetworkUtils.getLocalIpAddress();
                Gdx.app.postRunnable(() -> {
                    statusLabel.setText("Sever started! Waiting for players...");

                    serverIPLabel.setText("Your Server IP: " + localIP + "\nPort: " + NetworkProtocol.TCP_PORT);
                    serverIPLabel.setVisible(true);

                    showAllIPsButton.setVisible(true);
                    this.getMain().startNetworkGame(localIP, true);
                });
            } catch (Exception e) {
                Gdx.app.postRunnable(() -> {
                    statusLabel.setText("Failed to start sever!");
                });
            }
        }).start();
    }
}
