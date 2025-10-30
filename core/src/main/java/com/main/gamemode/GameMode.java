package com.main.gamemode;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.main.input.IngameInputHandler;
import entity.Player;
import entity.object.Ball;
import entity.object.Paddle;
import entity.Effect.ShieldEffect;
import entity.object.brick.BricksMap;

import java.util.ArrayList;

public abstract class GameMode {
    private Player player;
    private boolean isEnd;

    protected IngameInputHandler inputHandler;
    protected boolean start = false;

    protected ArrayList<Ball> balls;
    protected boolean followPaddle = true;

    private float debugPrintTimer = 0f;
    private static final float DEBUG_PRINT_INTERVAL = 0.5f;

    public GameMode() {
        balls = new ArrayList<>();
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    public abstract Paddle getPaddle1();
    public Paddle getPaddle2() {
        return null;
    }

    public void launchBall() {
        if (followPaddle) {
            followPaddle = false;
            if (!balls.isEmpty()) {
                balls.get(0).updateVelocity();
            }
        }
    }

    public void isStart(boolean start) {
        if (!this.start) {
            this.start = start;
        }
    }

    public abstract void create();
    public abstract void update(float delta);
    public abstract void render(SpriteBatch sp, float delta);
    public abstract void handleInput();
    public abstract void draw(SpriteBatch sp);

    public void printActiveEffects(float delta) {
        // Increment timer
        debugPrintTimer += delta;

        // Check if interval has passed
        if (debugPrintTimer < DEBUG_PRINT_INTERVAL) {
            return; // Not time to print yet
        }

        // Reset timer
        debugPrintTimer -= DEBUG_PRINT_INTERVAL;

        // --- The rest of the print logic ---
        System.out.println("--- ACTIVE EFFECTS DEBUG ---");

        // --- Check Paddle 1 ---
        Paddle paddle1 = getPaddle1();
        if (paddle1 != null) {
            long expandTime = paddle1.getTimeExpandEffect(); //
            long stunTime = paddle1.getTimeStunEffect();

            if (expandTime > 0) {
                System.out.println("P1: Expand Active (" + String.format("%.2f", expandTime / 1000.0) + "s)");
            }
            if (stunTime > 0) {
                System.out.println("P1: Stunned Active (" + String.format("%.2f", stunTime / 1000.0) + "s)");
            }
        }

        // --- Check Paddle 2 (for VsMode) ---
        Paddle paddle2 = getPaddle2();
        if (paddle2 != null) {
            long expandTimeP2 = paddle2.getTimeExpandEffect(); //
            long stunTimeP2 = paddle2.getTimeStunEffect();

            if (expandTimeP2 > 0) {
                System.out.println("P2: Expand Active (" + String.format("%.2f", expandTimeP2 / 1000.0) + "s)");
            }
            if (stunTimeP2 > 0) {
                System.out.println("P2: Stunned Active (" + String.format("%.2f", stunTimeP2 / 1000.0) + "s)");
            }
        }

        // --- Check All Balls ---
        if (balls != null && !balls.isEmpty()) {
            for (int i = 0; i < balls.size(); i++) {
                Ball ball = balls.get(i);
                long bigTime = ball.getTimeBigWEffect();   //
                long slowTime = ball.getTimeSlowEffect();  //
                long fastTime = ball.getTimeFastEffect();

                if (bigTime > 0) {
                    System.out.println("Ball " + i + ": Big Active (" + String.format("%.2f", bigTime / 1000.0) + "s)");
                }
                if (slowTime > 0) {
                    System.out.println("Ball " + i + ": Slow Active (" + String.format("%.2f", slowTime / 1000.0) + "s)");
                }
                if (fastTime > 0) {
                    System.out.println("Ball " + i + ": Fast Active (" + String.format("%.2f", fastTime / 1000.0) + "s)");
                }
            }
        }
        System.out.println("----------------------------");
    }
}
