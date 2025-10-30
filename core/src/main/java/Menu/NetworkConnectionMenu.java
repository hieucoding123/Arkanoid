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
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
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
            new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()))
        );

        this.setFont(new BitmapFont(Gdx.files.internal("ui/F_Retro.fnt")));
        this.getFont().getData().setScale(1);

        this.setSkin(new Skin(Gdx.files.internal("ui2/ui2.json")));
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

        // HOST BUTTON
        TextButton hostButton = new TextButton("Host Game", this.getSkin());
        hostButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hostGame();
            }
        });

        serversList = new List<>(this.getSkin());
        serversListScrollPane = new ScrollPane(serversList, this.getSkin());
        serversListScrollPane.setFadeScrollBars(false);

        // REFRESH BUTTON
        TextButton refreshButton = new TextButton("Refresh List", this.getSkin());
        refreshButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                discoverServers();
            }
        });

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
        buttonTable.add(hostButton).padBottom(20).padTop(30).row();
        buttonTable.add(serversListScrollPane).width(300).height(150).padBottom(10).row();
        buttonTable.add(refreshButton).padBottom(20).row();
        buttonTable.add(joinButton).padBottom(40).padTop(30).row();
        buttonTable.add(backButton).padBottom(40).padTop(30).row();

        mainTable.add(buttonTable);
    }

    // Discovering servers by send UDP
    private void discoverServers() {
        statusLabel.setText("Discovering Servers...");
        foundServers.clear();
        serversList.clearItems();

        new Thread(() -> {
            // Client for discovering
            Client tempClient = new Client(8192, 2048);
            NetworkProtocol.register(tempClient);
            tempClient.start();

            tempClient.addListener(new Listener() {
                @Override
                public void received(Connection connection, Object object) {
                    if (object instanceof NetworkProtocol.ServerInfoResponse) {
                        NetworkProtocol.ServerInfoResponse response = (NetworkProtocol.ServerInfoResponse) object;
                        // sender's IP
                        InetAddress serverIP = connection.getRemoteAddressTCP().getAddress();
                        String displayName = String.format("%s (%d/%d)",
                            response.hostName,
                            response.currentPlayers,
                            response.maxPlayers
                        );
                        foundServers.put(displayName, serverIP);
                        Gdx.app.postRunnable(() -> {
                            serversList.setItems(foundServers.keySet().toArray(new String[0]));
                        });
                    }
                }
            });
            tempClient.sendUDP(new NetworkProtocol.DiscoverServerRequest());
            // Waiting 3s
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Dọn dẹp
            tempClient.stop();
            tempClient.close();

            Gdx.app.postRunnable(() -> {
                if (foundServers.isEmpty()) {
                    statusLabel.setText("No servers found.");
                } else {
                    statusLabel.setText("Found " + foundServers.size() + " servers.");
                }
            });

        }).start();
    }

    // Join game -> Client
    private void joinGame() {
        String selectedDisplayName = serversList.getSelected();
        if (selectedDisplayName == null) {
            statusLabel.setText("Please Select Server!");
            return;
        }

        InetAddress serverIP = foundServers.get(selectedDisplayName);
        if (serverIP == null) {
            statusLabel.setText("Error joining server!. Please refresh");
            return;
        }

        String serverIPString = serverIP.getHostAddress();
        statusLabel.setText("Connected to: " + serverIPString);

        new Thread(() -> {
            try {
                Thread.sleep(500);      // Connection delay
                Gdx.app.postRunnable(() -> {
                    statusLabel.setText("Connected! Staring game...");
                    getMain().startNetworkGame(serverIPString, false);
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
