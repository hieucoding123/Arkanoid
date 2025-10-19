package entity;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class TextureManager {
    public static Texture bgTexture;
    public static ArrayList<Texture> brickTextures = new ArrayList<>();
    public static Texture ballTexture;
    public static Texture paddleTexture;
    public static Texture threeBallsTextures;
    public static Texture expandpaddleTexture;
    public static Texture shieldTexture;
    public static Texture lineTexture;
    public static Texture BALLTexture;
    public static Texture SlowBallTexture;
    public static Texture ExplosionTexture;
    /**
     * Load textures.
     */
    public static void loadTextures() {
        bgTexture = new Texture("background.png");
        brickTextures.add(new Texture("brick_blue.png"));
        brickTextures.add(new Texture("brick_cyan.png"));
        ballTexture = new Texture("ball.png");
        paddleTexture = new Texture("paddle.png");
        threeBallsTextures = new Texture("threeBalls.png");
        expandpaddleTexture = new Texture("expandpaddle.png");
        shieldTexture = new Texture("shield.png");
        lineTexture = new  Texture("yellow_line.png");
        BALLTexture = new Texture("BallEffect.png");
        SlowBallTexture = new Texture("Slow.png");
        ExplosionTexture = new Texture("explosionEffect.png");
    }

    /**
     * Free texture memory.
     */
    public static void dispose() {
        bgTexture.dispose();
        for (Texture texture: brickTextures) {
            texture.dispose();
        }
        ballTexture.dispose();
        paddleTexture.dispose();
        threeBallsTextures.dispose();
        expandpaddleTexture.dispose();
        shieldTexture.dispose();
        lineTexture.dispose();
        BALLTexture.dispose();
        SlowBallTexture.dispose();
        ExplosionTexture.dispose();
    }
}
