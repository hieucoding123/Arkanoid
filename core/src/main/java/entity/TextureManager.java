package entity;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class TextureManager {
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
        brickTextures.add(new Texture("images/brick_blue.png"));
        brickTextures.add(new Texture("images/brick_cyan.png"));
        ballTexture = new Texture("images/ball.png");
        paddleTexture = new Texture("images/paddle.png");
        threeBallsTextures = new Texture("images/threeBalls.png");
        expandpaddleTexture = new Texture("images/expandpaddle.png");
        shieldTexture = new Texture("images/shield.png");
        lineTexture = new  Texture("images/yellow_line.png");
        BALLTexture = new Texture("images/BallEffect.png");
        SlowBallTexture = new Texture("images/Slow.png");
        ExplosionTexture = new Texture("images/explosionEffect.png");
    }

    /**
     * Free texture memory.
     */
    public static void dispose() {
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
