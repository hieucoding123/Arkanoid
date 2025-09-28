package entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;

public class BricksMap {
    private final int rows = 12;
    private final int cols = 8;
    private final int xBeginCoord = 24;
    private final int yBeginCoord = 656;

    private ArrayList<Brick> bricks;

    public BricksMap(String path) {
        bricks = new ArrayList<>();
        try {
            InputStream is = getClass().getResourceAsStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            for (int i = 0; i < rows; i++) {
                String[] line = br.readLine().split(" ");
                for (int j = 0; j < cols; j++) {
                    int color = Integer.parseInt(line[j]);
                    if (color >= 0) {
                        bricks.add(new Brick(xBeginCoord + j * 74, yBeginCoord - i * 30,
                            TextureManager.brickTextures.get(color)));
                    }
                }
            }
            br.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    public void update() {
        bricks.removeIf(brick -> brick.isDestroyed());
    }
    public void draw(SpriteBatch batch) {
        for (Brick brick : bricks) {
            brick.draw(batch);
        }
    }
}
