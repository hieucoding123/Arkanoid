package Menu;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent; // Keep this import
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
// import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle; // No longer needed
// import com.badlogic.gdx.scenes.scene2d.utils.ClickListener; // No longer needed
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.esotericsoftware.kryonet.Client;
import com.main.GameState;
import com.main.Main;
import com.main.network.NetworkProtocol;
import com.main.network.NetworkUtils;
import entity.Player;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

public class NetworkConnectionMenu extends  UserInterface{
    private List<String> serversList;
    private ScrollPane serversListScrollPane;
    private HashMap<String, InetAddress> foundServers;

    private Label statusLabel;
    private boolean isHosting;

    public NetworkConnectionMenu(Main main, Player player) {
        super(main, player);
        foundServers = new HashMap<>();
    }

    @Override
    public void create() {
        this.setBackground(new Texture(Gdx.files.internal("ui/bg.png")));

        this.setStage(new Stage(
            new FitViewport(800, 1000))
        );

        this.setFont(new BitmapFont(Gdx.files.internal("ui/F_Retro.fnt")));
        this.getFont().getData().setScale(1);

        this.setSkin(new Skin(Gdx.files.internal("ui2/ui2.json")));
        this.getSkin().add("default-font", this.getFont());

        Gdx.input.setInputProcessor(this.getStage());

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        this.getStage().addActor(mainTable);
        mainTable.setBackground(
            new TextureRegionDrawable(new TextureRegion(this.getBGTexture()))
        );

        // TITLE LABEL
        Label.LabelStyle LBStyle = new Label.LabelStyle(this.getFont(), Color.YELLOW);
        Label titleLabel = new Label("Network Multiplayer", LBStyle);
        titleLabel.setFontScale(1f);
        titleLabel.pack();

        // STATUS LABEL
        statusLabel = new Label("", this.getSkin());
        statusLabel.setColor(Color.YELLOW);
        statusLabel.pack();

        // Host button
        Label hostButton = createClickableLabel(
            "Host Game",
            null,
            0f,
            true,
            () -> hostGame()
        );
        hostButton.pack();

        // SERVER LIST
        serversList = new List<>(this.getSkin());
        serversListScrollPane = new ScrollPane(serversList, this.getSkin());
        serversListScrollPane.setFadeScrollBars(false);
        serversListScrollPane.setSize(270, 130);
        serversListScrollPane.setDebug(true);

        // Refresh button
        Label refreshButton = createClickableLabel(
            "Refresh List",
            null,
            0f,
            true,
            () -> discoverServers()
        );
        refreshButton.pack();

        // Join button
        Label joinButton = createClickableLabel(
            "Join Game",
            null,
            0f,
            true,
            () -> joinGame()
        );
        joinButton.pack();

        Main main = this.getMain();

        // Back button
        Label backButton = createClickableLabel(
            "Back",
            null,
            0f,
            true,
            () -> main.setGameState(GameState.SELECT_MODE)
        );
        backButton.pack();

        //Set up position
        float stageWidth = this.getStage().getWidth();
        float stageHeight = this.getStage().getHeight();
        float currentY;

        currentY = stageHeight - 250 - titleLabel.getHeight();
        titleLabel.setPosition(
            (stageWidth / 2f) - (titleLabel.getWidth() / 2f),
            currentY
        );
        this.getStage().addActor(titleLabel);

        currentY = currentY - 80 - hostButton.getHeight();
        hostButton.setPosition(
            (stageWidth / 2f) - (hostButton.getWidth() / 2f),
            currentY
        );
        this.getStage().addActor(hostButton);

        currentY = currentY - 40 - serversListScrollPane.getHeight();
        serversListScrollPane.setPosition(
            (stageWidth / 2f) - (serversListScrollPane.getWidth() / 2f),
            currentY
        );
        this.getStage().addActor(serversListScrollPane);

        currentY = currentY - 60 - refreshButton.getHeight();
        refreshButton.setPosition(
            (stageWidth / 2f) - (refreshButton.getWidth() / 2f),
            currentY
        );
        this.getStage().addActor(refreshButton);

        currentY = currentY - 40 - joinButton.getHeight();
        joinButton.setPosition(
            (stageWidth / 2f) - (joinButton.getWidth() / 2f),
            currentY
        );
        this.getStage().addActor(joinButton);

        currentY = currentY - 40 - backButton.getHeight();
        backButton.setPosition(
            (stageWidth / 2f) - (backButton.getWidth() / 2f),
            currentY
        );
        this.getStage().addActor(backButton);

        statusLabel.setPosition(
            (stageWidth / 2f) - (statusLabel.getWidth() / 2f),
            titleLabel.getY() - 20 - statusLabel.getHeight()
        );
        this.getStage().addActor(statusLabel);
    }

    // Discovering servers by send UDP
    private void discoverServers() {
        statusLabel.setText("Discovering Servers...");
        statusLabel.pack();
        statusLabel.setPosition(
            (getStage().getWidth() / 2f) - (statusLabel.getWidth() / 2f),
            statusLabel.getY()
        );

        foundServers.clear();
        serversList.clearItems();

        new Thread(() -> {
            // Client for discovering
            Client tempClient = new Client();
            tempClient.start();

            java.util.List<InetAddress> hosts = tempClient.discoverHosts(NetworkProtocol.UDP_PORT, 3000);

            tempClient.stop();
            tempClient.close();

            Gdx.app.postRunnable(() -> {
                String text = "";
                if (hosts.isEmpty()) {
                    text = "No servers found. Host one!";
                }else {
                    text = "Found " + hosts.size() + " server(s)!";
                    ArrayList<String> displayItems = new ArrayList<>();
                    for (InetAddress host : hosts) {
                        String IP = host.getHostAddress();

                        int roomCode = Math.abs(IP.hashCode() % 10000);
                        String displayName = String.format("Room #%04d", roomCode); // last 4 chars
                        foundServers.put(displayName, host);
                        displayItems.add(displayName);
                    }
                    serversList.setItems(displayItems.toArray(new String[0]));
                }

                statusLabel.setText(text);
                statusLabel.pack();
                statusLabel.setPosition(
                    (getStage().getWidth() / 2f) - (statusLabel.getWidth() / 2f),
                    statusLabel.getY()
                );
            });

        }).start();
    }

    // Join game -> Client
    private void joinGame() {
        String selectedDisplayName = serversList.getSelected();
        String text = "";

        if (selectedDisplayName == null) {
            text = "Please Select Server!";
        } else {
            InetAddress host = foundServers.get(selectedDisplayName);
            if (host == null) {
                text = "Error joining server!. Please refresh";
            } else {
                String IP = host.getHostAddress();
                text = "Connected to: " + IP;

                new Thread(() -> {
                    try {
                        Thread.sleep(500);      // Connection delay
                        Gdx.app.postRunnable(() -> {
                            statusLabel.setText("Connected! Staring game...");
                            statusLabel.pack();
                            statusLabel.setPosition(
                                (getStage().getWidth() / 2f) - (statusLabel.getWidth() / 2f),
                                statusLabel.getY()
                            );
                            getMain().startNetworkGame(IP, false);
                        });
                    } catch (Exception e) {
                        Gdx.app.postRunnable(() -> {
                            statusLabel.setText("Failed to connect!");
                            statusLabel.pack();
                            statusLabel.setPosition(
                                (getStage().getWidth() / 2f) - (statusLabel.getWidth() / 2f),
                                statusLabel.getY()
                            );
                        });
                    }
                }).start();
            }
        }

        if (!text.isEmpty()) {
            statusLabel.setText(text);
            statusLabel.pack();
            statusLabel.setPosition(
                (getStage().getWidth() / 2f) - (statusLabel.getWidth() / 2f),
                statusLabel.getY()
            );
        }
    }

    private void
    hostGame() {
        statusLabel.setText("Staring sever...");
        statusLabel.pack();
        statusLabel.setPosition(
            (getStage().getWidth() / 2f) - (statusLabel.getWidth() / 2f),
            statusLabel.getY()
        );
        isHosting = true;

        new Thread(() -> {
            try {
                String localIP = NetworkUtils.getLocalIpAddress();
                Gdx.app.postRunnable(() -> {
                    statusLabel.setText("Sever started! Waiting for players...");
                    statusLabel.pack();
                    statusLabel.setPosition(
                        (getStage().getWidth() / 2f) - (statusLabel.getWidth() / 2f),
                        statusLabel.getY()
                    );
                    this.getMain().startNetworkGame(localIP, true);
                });
            } catch (Exception e) {
                Gdx.app.postRunnable(() -> {
                    statusLabel.setText("Failed to start sever!");
                    statusLabel.pack();
                    statusLabel.setPosition(
                        (getStage().getWidth() / 2f) - (statusLabel.getWidth() / 2f),
                        statusLabel.getY()
                    );
                });
            }
        }).start();
    }
}
