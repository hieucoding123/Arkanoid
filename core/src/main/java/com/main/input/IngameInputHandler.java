package com.main.input;

import com.badlogic.gdx.Input;
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

        Paddle p2 = currentMode.getPaddle2();
        if (p2 != null) {
            if (paddle2Left) {
                p2.moveLeft();
            } else if (paddle2Right) {
                p2.moveRight();
            } else {
                p2.setVelocity(0, 0);
            }
        }
    }

    @Override
    public boolean keyDown(int keycode) {

        if(keycode == Input.Keys.SPACE) {
            currentMode.launchBall();
            currentMode.start(true);
            return true;
        }

        switch (keycode) {
            case Input.Keys.A:
                paddle1Left = true;
                return true;
            case Input.Keys.D:
                paddle1Right = true;
                return true;
        }

        boolean hasTwoPaddle = currentMode.getPaddle2() != null;
        if (hasTwoPaddle) {
            switch (keycode) {
                case Input.Keys.LEFT:
                    paddle2Left = true;
                    return true;
                case Input.Keys.RIGHT:
                    paddle2Right = true;
                    return true;
            }
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        switch (keycode) {
            case Input.Keys.A:
                paddle1Left = false;
                return true;
            case Input.Keys.D:
                paddle1Right = false;
                return true;
        }

        boolean hasTwoPaddle = currentMode.getPaddle2() != null;
        if (hasTwoPaddle) {
            switch (keycode) {
                case Input.Keys.LEFT:
                    paddle2Left = false;
                    return true;
                case Input.Keys.RIGHT:
                    paddle2Right = false;
                    return true;
            }
        }

        return false;
    }
}
