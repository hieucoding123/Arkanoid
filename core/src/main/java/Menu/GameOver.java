package Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.main.GameState;
import com.main.Main;
import com.main.network.GameClient;
import com.main.network.NetworkProtocol;
import entity.Player;

/**
 * <li>Represents the 'GAME OVER' screen in a network game.</li>
 * <li>It displays the result (win/lose/draw) of a game between two players,
 * allows players to exit, and handles network events like disconnections.</li>
 */
public class GameOver extends UserInterface implements GameClient.GameClientListener {
    private final GameClient client;
    private int myPNumber;
    private final int p1Score;
    private final int p2Score;
    private TextButton myQuitButton;
    private TextButton.TextButtonStyle textOnlyButtonStyle;
    private boolean hasQuit = false;

    public GameOver(Main main, Player player, int p1Score, int p2Score,
                    GameClient client, boolean isHost) {
        super(main, player);
        this.p1Score = p1Score;
        this.p2Score = p2Score;
        this.client = client;
        client.setListener(this);
        this.myPNumber = client.getMyPNumber();
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

        String p1String, p2String;
        if (p1Score > p2Score)          { p1String = "WIN"; p2String = "LOSE";
        } else if (p1Score < p2Score)   { p1String = "LOSE"; p2String = "WIN";
        } else                          { p1String = p2String = "DRAW"; }

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
        Label titleLabel = new Label("GAME OVER", this.getSkin());
        titleLabel.setFontScale(1.25f);
        titleLabel.setColor(Color.LIGHT_GRAY);
        mainTable.add(titleLabel).colspan(2).padTop(70).row();

        // PLAYER 1
        Table player1Table = new Table();
        player1Table.defaults().pad(10);

        Label nameP1 = new Label("PLAYER 1", this.getSkin());
        nameP1.setFontScale(1.2f);
        nameP1.setColor(Color.GREEN);
        player1Table.add(nameP1).colspan(1).row();

        Label p1Result = new Label(p1String, this.getSkin());
        p1Result.setColor(Color.YELLOW);
        player1Table.add(p1Result).colspan(1).row();

        if (myPNumber == 1) {
            myQuitButton = createQuitButton();
            player1Table.add(myQuitButton).width(120).height(40).padTop(10).row();
        }

        mainTable.add(player1Table).width(250).padRight(20);

        // Player 2
        Table player2Table = new Table();
        player2Table.defaults().pad(10);

        Label nameP2 = new Label("PLAYER 2", this.getSkin());
        nameP2.setFontScale(1.2f);
        nameP2.setColor(Color.BLUE);
        player2Table.add(nameP2).colspan(1).row();

        Label p2Result = new Label(p2String, this.getSkin());
        p2Result.setColor(Color.YELLOW);
        player2Table.add(p2Result).colspan(1).row();

        if (myPNumber == 2) {
            myQuitButton = createQuitButton();
            player2Table.add(myQuitButton).width(120).height(40).padTop(10).row();
        }

        mainTable.add(player2Table).width(250).padRight(20).row();


    }

    private TextButton createQuitButton() {
        TextButton button = new TextButton("QUIT", textOnlyButtonStyle);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                quit();
            }
        });
        return button;
    }

    private void quit() {
        if (hasQuit || !client.isConnected())
            return;
        hasQuit = true;
        myQuitButton.setDisabled(true);
        client.disconnect();
        getMain().setGameState(GameState.NETWORK_CONNECTION_MENU);
    }

    @Override
    public void onConnected(int pNumber) {
        this.myPNumber = pNumber;
    }

    @Override
    public void onGameStateUpdated(NetworkProtocol.GameStateUpdate state) {

    }

    @Override
    public void onGameStarted() {

    }

    @Override
    public void onDisconnected(String reason) {
        if (hasQuit)
            return;
        System.out.println("Disconnected from game over: " + reason);
        handleRemoteDisconnect();
    }

    @Override
    public void onMessage(String message) {
        if (hasQuit) return;

        if (message.startsWith("PLAYER_DISCONNECTED:")) {
            handleRemoteDisconnect();
        }
    }

    /**
     * Handle disconnect with Server
     */
    private void handleRemoteDisconnect() {
        if (hasQuit) return;

        hasQuit = true;
        Gdx.app.postRunnable(() -> {
            if (client.isConnected()) {
                client.disconnect();
            }
            getMain().setGameState(GameState.NETWORK_CONNECTION_MENU);
        });
    }

    @Override
    public void onLobbyUpdate(NetworkProtocol.LobbyUpdate update) {

    }
}
