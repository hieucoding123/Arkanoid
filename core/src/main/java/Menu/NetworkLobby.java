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

public class NetworkLobby extends UserInterface implements GameClient.GameClientListener {
    private GameClient client;
    private int myPNumber;
    private boolean isHost;

    private Label titleLabel;
    private Label player1StatusLabel;
    private Label player2StatusLabel;
    private Label player1ReadyLabel;
    private Label player2ReadyLabel;
    private TextButton myReadyButton;
    private TextButton myQuitButton;
    private Label waitingLabel;

    private boolean player1Connected = false;
    private boolean player2Connected = false;
    private boolean player1Ready = false;
    private boolean player2Ready = false;
    private boolean myReady = false;

    public NetworkLobby(Main main, Player player, GameClient client, boolean isHost) {
        super(main, player);
        this.client = client;
        this.isHost = isHost;
        this.myPNumber = client.getMyPNumber();

        if (myPNumber == 1)
            player1Connected = true;
        else if (myPNumber == 2)
            player2Connected = true;
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

        titleLabel = new Label("GAME LOBBY", this.getSkin());
        titleLabel.setFontScale(1.25f);
        titleLabel.setColor(Color.CYAN);
        mainTable.add(titleLabel).colspan(2).padBottom(40).row();

        if (isHost) {
            Label hostLabel = new Label("You are hosting the game", this.getSkin());
            hostLabel.setFontScale(1.25f);
            mainTable.add(hostLabel).colspan(2).padBottom(20).row();
        }

        // PLAYER 1
        Table player1Table = new Table();
        player1Table.defaults().pad(10);

        Label p1Title = new Label("PLAYER 1", this.getSkin());
        p1Title.setFontScale(1.5f);
        p1Title.setColor(Color.GREEN);
        player1Table.add(p1Title).row();

        player1StatusLabel = new Label("Waiting...", this.getSkin());
        player1StatusLabel.setColor(Color.YELLOW);
        player1Table.add(player1StatusLabel).row();

        player1ReadyLabel = new Label("Not Ready", this.getSkin());
        player1ReadyLabel.setColor(Color.RED);
        player1Table.add(player1ReadyLabel).pad(10).row();

        if (myPNumber == 1) {
            myReadyButton = new TextButton("READY", this.getSkin());
            myReadyButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    sendReady();
                }
            });
            player1Table.add(myReadyButton).width(150).height(30).padTop(20).row();

            myQuitButton = new TextButton("QUIT", this.getSkin());
            myQuitButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    quitLobby();
                }
            });
            player1Table.add(myReadyButton).width(150).height(30).padTop(10).row();
        }
        mainTable.add(player1Table).width(300).pad(20);

        // PLAYER 2
        Table player2Table = new Table();
        player2Table.defaults().pad(10);

        Label p2Title = new Label("PLAYER 2", this.getSkin());
        p2Title.setFontScale(1.5f);
        p2Title.setColor(Color.BLUE);
        player2Table.add(p2Title).row();

        player2StatusLabel = new Label("Waiting...", this.getSkin());
        player2StatusLabel.setColor(Color.YELLOW);
        player2Table.add(player2StatusLabel).row();

        player2ReadyLabel = new Label("Not Ready", this.getSkin());
        player2ReadyLabel.setColor(Color.RED);
        player2Table.add(player2ReadyLabel).pad(10).row();
        if (myPNumber == 2) {
            myReadyButton = new TextButton("READY", this.getSkin());
            myReadyButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    sendReady();
                }
            });
            player2Table.add(myReadyButton).width(150).height(30).padTop(20).row();

            myQuitButton = new TextButton("QUIT", this.getSkin());
            myQuitButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    quitLobby();
                }
            });
            player2Table.add(myQuitButton).width(150).height(50).padTop(10).row();
        }
        mainTable.add(player2Table).width(300).pad(20);

        waitingLabel = new Label("Waiting for players...", this.getSkin());
        waitingLabel.setColor(Color.YELLOW);
        waitingLabel.setScale(1.2f);
        mainTable.add(waitingLabel).colspan(2).padBottom(20).row();

        updatePlayerStatus();
    }

    /**
     * Send ready signal to Server
     */
    private void sendReady() {
        if (!myReady) {
            myReady = true;
            client.sendReady();

            myReadyButton.setText("READY!");
            myReadyButton.setDisabled(true);
            myReadyButton.setColor(Color.GRAY);

            if (myPNumber == 1) {
                player1ReadyLabel.setText("READY!");
                player1ReadyLabel.setColor(Color.GREEN);
            } else if (myPNumber == 2) {
                player2ReadyLabel.setText("READY!");
                player2ReadyLabel.setColor(Color.GREEN);
            }
            updateWattingMessage();
        }
    }

    private void quitLobby() {
        client.disconnect();
        getMain().setGameState(GameState.NETWORK_CONNECTION_MENU);
    }

    private void updatePlayerStatus() {
        if (player1Connected) {
            player1StatusLabel.setText("Connected!");
            player1StatusLabel.setColor(Color.GREEN);
        } else {
            player1StatusLabel.setText("Waiting...");
            player1StatusLabel.setColor(Color.GRAY);
        }

        if (player2Connected) {
            player2StatusLabel.setText("Connected!");
            player2StatusLabel.setColor(Color.GREEN);
        } else {
            player2StatusLabel.setText("Waiting...");
            player2StatusLabel.setColor(Color.GRAY);
        }
        updateWattingMessage();
    }

    private void updateWattingMessage() {
        if (!player2Connected || !player2Connected) {
            waitingLabel.setText("Waiting for players to connect...");
            waitingLabel.setColor(Color.YELLOW);
        } else if (!player1Ready || !player2Ready) {
            waitingLabel.setText("Waiting for players to ready up...");
            waitingLabel.setColor(Color.ORANGE);
        } else {
            waitingLabel.setText("Staring game...");
            waitingLabel.setColor(Color.GREEN);
        }
    }

    @Override
    public void onConnected(int pNumber) {
        this.myPNumber = pNumber;
        if (pNumber == 1) {
            player1Connected = true;
        } else {
            player2Connected = true;
        }
        updatePlayerStatus();
    }

    @Override
    public void onGameStateUpdated(NetworkProtocol.GameStateUpdate state) {
        // Do not update state in Lobby
    }

    @Override
    public void onGameStarted() {
        System.out.println("Game starting");
        Gdx.app.postRunnable(() -> {
            getMain().setGameState(GameState.NETWORK_VS);
        });
    }

    @Override
    public void onDisconnected(String reason) {
        System.out.println("Disconnected from lobby: " + reason);
        Gdx.app.postRunnable(() -> {
            getMain().setGameState(GameState.NETWORK_CONNECTION_MENU);
        });
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Lobby message: " + message);

        if (message.startsWith("PLAYER_CONNECTED:")) {
            int playerNum = Integer.parseInt(message.split(":")[1]);
            if (playerNum == 1) {
                player1Connected = true;
            }else {
                player2Connected = true;
            }
            updatePlayerStatus();
        } else if (message.startsWith("PLAYER_READY:")) {
            int playerNum = Integer.parseInt(message.split(":")[1]);
            if (playerNum == 1) {
                player1Ready = true;
                player1ReadyLabel.setText("READY!");
                player1ReadyLabel.setColor(Color.GREEN);
            } else if (playerNum == 2) {
                player2Ready = true;
                player2ReadyLabel.setText("READY!");
                player2ReadyLabel.setColor(Color.GREEN);
            }
            updateWattingMessage();
        } else if (message.startsWith("PLAYER_DISCONNECTED:")) {
            int playerNum = Integer.parseInt(message.split(":")[1]);
            System.out.println("Player " +  playerNum + " left the lobby");

            waitingLabel.setText("Player " + playerNum + " disconnected. Returning to menu...");
            waitingLabel.setColor(Color.RED);

            // Return network menu after 2 seconds
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Gdx.app.postRunnable(() -> {
                    getMain().setGameState(GameState.NETWORK_CONNECTION_MENU);
                });
            }).start();
        }
    }
}
