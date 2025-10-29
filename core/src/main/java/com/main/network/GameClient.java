package com.main.network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

public class GameClient {
    private Client client;
    private int myPNumber;
    private boolean connected = false;
    private NetworkProtocol.GameStateUpdate lastGameState;
    private GameClientListener listener;

    public interface GameClientListener {
        void onConnected(int pNumber);
        void onGameStateUpdated(NetworkProtocol.GameStateUpdate state);
        void onGameStarted();
        void onDisconnected(String reason);
        void onMessage(String message);
    }

    public GameClient(GameClientListener listener) {
        this.listener = listener;
        client = new Client(16384, 8192);
        NetworkProtocol.register(client);
    }

    // connect to sever
    public void connect(String host, String pName, NetworkProtocol.GameMode mode) throws IOException {
        client.start();
        client.connect(5000, host, NetworkProtocol.TCP_PORT, NetworkProtocol.UDP_PORT);

        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                handleMessage(object);
            }
            @Override
            public void disconnected(Connection connection) {
                handleDisconnect();
            }
        });

        NetworkProtocol.LoginRequest loginRequest = new NetworkProtocol.LoginRequest(
            pName, mode
        );
        client.sendTCP(loginRequest);
    }

    // Send player input to sever
    public void sendInput(NetworkProtocol.InputType inputType) {
        if (!connected) return;

        NetworkProtocol.PlayerInput input = new NetworkProtocol.PlayerInput(
            myPNumber, inputType
        );
        client.sendUDP(input);
    }

    // Send ready signal
    public void sendReady() {
        if (!connected) return;

        NetworkProtocol.StartGameRequest request = new NetworkProtocol.StartGameRequest();
        client.sendTCP(request);
    }

    // Handle message from sever
    private void handleMessage(Object obj) {
        if (obj instanceof NetworkProtocol.LoginResponse) {
            handleLoginResponse((NetworkProtocol.LoginResponse) obj);
        }
        else if (obj instanceof NetworkProtocol.GameStateUpdate) {
            handleGameStateUpdate((NetworkProtocol.GameStateUpdate) obj);
        }
        else if (obj instanceof NetworkProtocol.LobbyUpdate) {
            handleLobbyUpdate((NetworkProtocol.LobbyUpdate) obj);
        }
        else if (obj instanceof String) {
            String message = (String) obj;
            if (message.equals("GAME_START")) {
                listener.onGameStarted();
            } else {
                listener.onMessage(message);
            }
        }
    }

    private void handleGameStateUpdate(NetworkProtocol.GameStateUpdate state) {
        lastGameState = state;
        listener.onGameStateUpdated(state);
    }

    // Handle login response
    private void handleLoginResponse(NetworkProtocol.LoginResponse loginResponse) {
        if (loginResponse.success) {
            connected = true;
            myPNumber = loginResponse.pNumber;
            listener.onConnected(myPNumber);
            System.out.println("Connected as Player " + myPNumber);
        } else {
            System.out.println("Connection failed: " + loginResponse.message);
            listener.onDisconnected(loginResponse.message);
        }
    }

    private void  handleLobbyUpdate(NetworkProtocol.LobbyUpdate lobbyUpdate) {

    }

    private void handleDisconnect() {
        connected = false;
        listener.onDisconnected("Connection lost");
    }

    public boolean isConnected() {
        return connected;
    }
    public void disconnect() {
        if (connected) {
            client.close();
            connected = false;
        }
    }
    public int getMyPNumber() {
        return myPNumber;
    }
    public NetworkProtocol.GameStateUpdate getLastGameState() {
        return lastGameState;
    }
}
