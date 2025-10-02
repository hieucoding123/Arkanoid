package entity;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class TextureManager {
    public static ArrayList<Texture> brickTextures = new ArrayList<>();
    public static Texture ballTexture;
    public static Texture paddleTexture;

    /**
     * Load textures.
     */
    public static void loadTextures() {
        brickTextures.add(new Texture("brick_blue.png"));
        brickTextures.add(new Texture("brick_cyan.png"));
        ballTexture = new Texture("ball.png");
        paddleTexture = new Texture("paddle.png");
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
    }
}
