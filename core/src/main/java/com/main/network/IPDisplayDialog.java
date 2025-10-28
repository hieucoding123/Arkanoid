package com.main.network;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


public class IPDisplayDialog extends Dialog {
    public IPDisplayDialog(String title, Skin skin) {
        super(title, skin);

        String localIP = NetworkUtils.getLocalIpAddress();
        String allIPs = NetworkUtils.getAllIPAddress();

        Table contentTable = getContentTable();
        contentTable.pad(20);

        Label recommendedLabel = new Label("Recommended IP ", skin);
        recommendedLabel.setColor(Color.GREEN);
        contentTable.add(recommendedLabel).padBottom(10).left().row();

        Label mainIPLabel = new Label(localIP, skin);
        mainIPLabel.setFontScale(1.5f);
        mainIPLabel.setColor(Color.CYAN);
        contentTable.add(mainIPLabel).padBottom(10).left().row();

        Label portLabel = new Label("Port: " + NetworkProtocol.TCP_PORT, skin);
        portLabel.setColor(Color.YELLOW);
        contentTable.add(portLabel).padBottom(10).left().row();

        Label allIPsLabel = new Label("All Network Interfaces", skin);
        allIPsLabel.setColor(Color.GRAY);
        contentTable.add(recommendedLabel).left().row();

        Label allIPsValue = new Label(allIPs, skin);
        allIPsValue.setFontScale(0.8f);
        contentTable.add(allIPsValue).padBottom(5).left().row();

        Table buttonTable = getButtonTable();
        buttonTable.pad(10);

        TextButton closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });
        buttonTable.add(closeButton).width(150).height(40);
    }

    public Dialog show(Stage stage) {
        super.show(stage);
        setPosition(
            (stage.getWidth() - getWidth()) / 2,
            (stage.getHeight() - getHeight()) / 2
        );
        return null;
    }
}
