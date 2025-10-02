package entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;

public class BricksMap {
    public final int rows = 12;
    public final int cols = 8;
    public final int xBeginCoord = 24;
    public final int yBeginCoord = 656;

    public final ArrayList<Brick> bricks;

    /**
     * Initialize brick map with map file.
     * @param path map file path
     */
    public BricksMap(float brickW, float brickH, float scale, String path) {
        bricks = new ArrayList<>();
        try {
            InputStream is = getClass().getResourceAsStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            for (int i = 0; i < rows; i++) {
                String[] line = br.readLine().split(" ");
                for (int j = 0; j < cols; j++) {
                    int color = Integer.parseInt(line[j]);
                    if (color >= 0) {
                        bricks.add(new Brick(
                            xBeginCoord + j * brickW,
                            yBeginCoord - i * brickH,
                            scale,
                            TextureManager.brickTextures.get(color)));
                    }
                }
            }
            br.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update bricks(remove, add, ...).
     */
    public void update() {
        for (Brick brick : bricks) {
            brick.update();
        }
        bricks.removeIf(Brick::isDestroyed);
    }

    /**
     * Draw bricks on game screen.
     * @param batch game drawing programming
     */
    public void draw(SpriteBatch batch) {
        for (Brick brick : bricks) {
            brick.draw(batch);
        }
    }
}
