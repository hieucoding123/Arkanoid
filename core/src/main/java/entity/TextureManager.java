package entity;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class TextureManager {
    public static Texture bgTexture;
    public static Texture brick1HIT;
    public static Texture brick2HIT;
    public static Texture brickexplo;
    public static Texture brickNOHIT;
    public static Texture ballTexture;
    public static Texture paddleTexture;
    public static Texture flippedpaddleTexture;
    public static Texture threeBallsTextures;
    public static Texture expandpaddleTexture;
    public static Texture shieldTexture;
    public static Texture lineTexture;
    public static Texture BALLTexture;
    public static Texture SlowBallTexture;
    public static Texture ExplosionTexture;
    public static Texture FastBallTexture;
    public static Texture StunPaddleTexture;
    public static Texture UnbreakBrickTexture;
    /**
     * Load textures.
     */
    public static void loadTextures() {
        bgTexture = new Texture("images/background.png");
        brick2HIT = new Texture("images/brick_blue.png");
        brick1HIT = new Texture("images/brick_cyan.png");
        brickexplo = new Texture("images/brick_red.png");
        brickNOHIT = new Texture("images/brick_black.png");
        ballTexture = new Texture("images/tempball.png");
        paddleTexture = new Texture("images/paddle.png");
        flippedpaddleTexture = new Texture("images/flippedpaddle.png");
        threeBallsTextures = new Texture("images/threeBalls.png");
        expandpaddleTexture = new Texture("images/expandpaddle.png");
        shieldTexture = new Texture("images/shield.png");
        lineTexture = new  Texture("images/yellow_line.png");
        BALLTexture = new Texture("images/BallEffect.png");
        SlowBallTexture = new Texture("images/Slow.png");
        ExplosionTexture = new Texture("images/explosionEffect.png");
        FastBallTexture = new Texture("images/FastEffect.png");
        StunPaddleTexture = new Texture("images/StunPaddle.png");
        UnbreakBrickTexture = new Texture("images/brickinfi.png");
    }

    /**
     * Free texture memory.
     */
    public static void dispose() {
        bgTexture.dispose();
        brick1HIT.dispose();
        brick2HIT.dispose();
        brickexplo.dispose();
        brickNOHIT.dispose();
        ballTexture.dispose();
        paddleTexture.dispose();
        flippedpaddleTexture.dispose();
        threeBallsTextures.dispose();
        expandpaddleTexture.dispose();
        shieldTexture.dispose();
        lineTexture.dispose();
        BALLTexture.dispose();
        SlowBallTexture.dispose();
        ExplosionTexture.dispose();
        FastBallTexture.dispose();
        StunPaddleTexture.dispose();
        UnbreakBrickTexture.dispose();
    }
}
