package com.main.input;

import com.badlogic.gdx.InputAdapter;
import com.main.gamemode.GameMode;
import entity.object.Paddle;

public class IngameInputHandler extends InputAdapter {

    private GameMode currentMode;

    //Paddles Movements
    private boolean paddle1Left = false;
    private boolean paddle1Right = false;
    private boolean paddle2Left = false;
    private boolean paddle2Right = false;

    public IngameInputHandler(GameMode currentMode) {
        this.currentMode = currentMode;
    }

    public void processMovement() {

        Paddle p1 = currentMode.getPaddle1();
        if (p1 != null) {
            if (paddle1Left) {
                p1.moveLeft();
            } else if (paddle1Right) {
                p1.moveRight();
            } else {
                p1.setVelocity(0,0);
            }
        }

    }
}
