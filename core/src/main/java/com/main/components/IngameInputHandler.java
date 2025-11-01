package com.main.components;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.main.gamemode.GameMode;
import entity.object.Paddle;

public class IngameInputHandler extends InputAdapter {

    private final GameMode currentMode;

    //Paddles Movements
    private boolean paddle1Left = false;
    private boolean paddle1Right = false;
    private boolean paddle2Left = false;
    private boolean paddle2Right = false;

    private boolean paddle1Shift = false;
    private boolean paddle2Shift = false;

    /**
     * Input Handler constructor for a game mode.
     * @param currentMode the game mode
     */
    public IngameInputHandler(GameMode currentMode) {
        this.currentMode = currentMode;
    }

    /**
     * Process movement method.
     */
    public void processMovement() {

        Paddle p1 = currentMode.getPaddle1();
        if (p1 != null) {
            if (paddle1Left) {
                p1.moveLeft(paddle1Shift);
            } else if (paddle1Right) {
                p1.moveRight(paddle1Shift);
            } else {
                p1.setVelocity(0,0);
            }
        }

        Paddle p2 = currentMode.getPaddle2();
        if (p2 != null) {
            if (paddle2Left) {
                p2.moveLeft(paddle2Shift);
            } else if (paddle2Right) {
                p2.moveRight(paddle2Shift);
            } else {
                p2.setVelocity(0, 0);
            }
        }
    }

    /**
     * Flag toggle for key press.
     * @param keycode one of the constants in {@link Input.Keys}
     * @return held down keycode or not and handled flag toggling.
     */
    @Override
    public boolean keyDown(int keycode) {

        if(keycode == Input.Keys.SPACE) {
            currentMode.launchBall();
            currentMode.isStart(true);
            return true;
        }

        switch (keycode) {
            case Input.Keys.A:
                paddle1Left = true;
                return true;
            case Input.Keys.D:
                paddle1Right = true;
                return true;
            case Input.Keys.SHIFT_LEFT:
                paddle1Shift = true;
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
                case Input.Keys.SHIFT_RIGHT:
                    paddle2Shift = true;
                    return true;
            }
        }

        return false;
    }

    /**
     * Flag toggle for key release.
     * @param keycode one of the constants in {@link Input.Keys}
     * @return held down keycode or not and handled flag toggling.
     */
    @Override
    public boolean keyUp(int keycode) {

        switch (keycode) {
            case Input.Keys.A:
                paddle1Left = false;
                return true;
            case Input.Keys.D:
                paddle1Right = false;
                return true;
            case Input.Keys.SHIFT_LEFT:
                paddle1Shift = false;
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
                case Input.Keys.SHIFT_RIGHT:
                    paddle2Shift = false;
                    return true;
            }
        }

        return false;
    }
}
