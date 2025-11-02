package entity;

import com.badlogic.gdx.graphics.Texture;
import entity.object.brick.Brick;
import entity.object.brick.BrickType;

import java.util.HashMap;

public class TextureManager {
    public static HashMap<BrickType, Texture> brickTextures;
    public static Texture bgTexture;
    public static Texture bgTexture1vs1;
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
    public static Texture RandomEffectTexture;
    public static Texture DeleteEffectTexture;
    /**
     * Load textures.
     */
    public static void loadTextures() {
        brickTextures = new HashMap<>();
        brickTextures.put(BrickType.T1HIT, new Texture("images/brick_bronze.png"));
        brickTextures.put(BrickType.T2HIT, new Texture("images/brick_iron.png"));
        brickTextures.put(BrickType.T3HIT, new Texture("images/brick_gold.png"));
        brickTextures.put(BrickType.T4HIT, new Texture("images/brick_diamond.png"));
        brickTextures.put(BrickType.EXPLO, new Texture("images/brick_red.png"));
        brickTextures.put(BrickType.NOHIT, new Texture("images/brick_black.png"));
        brickTextures.put(BrickType.UNBREAK, new Texture("images/e_bricked.png"));

        bgTexture = new Texture("images/background.png");
        bgTexture1vs1 = new Texture("images/vsmodebg.png");
        ballTexture = new Texture("images/tempball.png");
        paddleTexture = new Texture("images/paddle.png");
        flippedpaddleTexture = new Texture("images/flippedpaddle.png");
        threeBallsTextures = new Texture("images/e_triple.png");
        expandpaddleTexture = new Texture("images/e_bigpaddle.png");
        shieldTexture = new Texture("images/e_shielded.png");
        lineTexture = new  Texture("images/yellow_line.png");
        BALLTexture = new Texture("images/e_bigball.png");
        SlowBallTexture = new Texture("images/e_slowball.png");
        ExplosionTexture = new Texture("images/explosionEffect.png");
        FastBallTexture = new Texture("images/e_fastball.png");
        StunPaddleTexture = new Texture("images/e_stun.png");
        RandomEffectTexture = new Texture("images/e_random.png");
        DeleteEffectTexture = new Texture("images/e_removeeffect.png");
    }

    /**
     * Free texture memory.
     */
    public static void dispose() {
        for (Texture texture : brickTextures.values()) {
            texture.dispose();
        }
        brickTextures.clear();
        bgTexture.dispose();
        bgTexture1vs1.dispose();
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
        RandomEffectTexture.dispose();
        DeleteEffectTexture.dispose();
    }
}
