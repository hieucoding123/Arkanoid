package entity.object.brick;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import entity.ScoreManager;
import entity.TextureManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;

public class BricksMap {
    public final int rows = 15;
    public final int cols = 8;
    public static final int xBeginCoord = 90;
    public static final int yBeginCoord = 810;
    public static final int brickW = 78;
    public static final int brickH = 36;
    private final int[] r = {-1, 1, 0, 0};
    private final int[] c = {0, 0, -1, 1};
//    private final int[] r = {-1, 1, 0, 0, -1, -1, 1, 1};
//    private final int[] c = {0, 0, -1, 1, -1, 1, 1, -1};

    public final ArrayList<Brick> bricks;

    /**
     * Initialize brick map with map file.
     * @param path map file path
     */
    public BricksMap(String path) {
        bricks = new ArrayList<>();
        try {
            InputStream is = getClass().getResourceAsStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            for (int i = 0; i < rows; i++) {
                String[] line = br.readLine().split(" ");
                for (int j = 0; j < cols; j++) {
                    int color = Integer.parseInt(line[j]);

                    Random rand = new Random();
                    boolean explosion_check = (rand.nextInt(2) == 0) ? true : false;

                    if (color == 1) {
                        bricks.add(new Brick(
                            xBeginCoord + j * brickW,
                            yBeginCoord - i * brickH,
                            1,
                            explosion_check,
                            i, j,
                            color,
                            TextureManager.brickTextures.get(color)));
                    } else if (color == 0) {
                        bricks.add(new Brick(
                            xBeginCoord + j * brickW,
                            yBeginCoord - i * brickH,
                            2,
                            explosion_check,
                            i, j,
                            color,
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
     * Update bricks(remove, add, explosion, ...).
     * @param delta fps balance
     * @param score Score Manager
     */
    public void update(float delta, ScoreManager score) {
        Map<Integer, Brick> save_brick = new HashMap<>();
        ArrayList<Brick> bricksNeighbors = new ArrayList<>();

        for (Brick brick : bricks) {
            brick.update(delta);
            if (brick.shouldExplode()) {
                bricksNeighbors.add(brick);
            }
        }
        if (!bricksNeighbors.isEmpty()) {
            Brick[][] grid = new Brick[rows][cols];
            for (Brick b : bricks) {
                if (b != null && !b.isDestroyed()) {
                    save_brick.put(b.getRow() * cols + b.getCol(), b);
                    grid[b.getRow()][b.getCol()] = b;
                }
            }
            for (Brick cur : bricksNeighbors) {
                cur.setexplosiontimes();
                for (int i = 0; i < r.length; i++) {
                    int new_row = cur.getRow() + r[i];
                    int new_col = cur.getCol() + c[i];

                    if (new_row >= 0 && new_row < rows && new_col >= 0 && new_col < cols) {
                        Brick new_brick = grid[new_row][new_col];
                        if (new_brick != null && new_brick.getExplosion()) {
//                            int hp = new_brick.gethitPoints();
                            save_brick.get(new_row * cols + new_col).setHitPoints(0);
                            new_brick.startExplosion();
//                            if (new_brick.getColor() == 1) {
//                                score.addScore100();
//                            } else if (new_brick.getColor() == 0) {
//                                score.addScore200();
//                            }
                            score.addScore(new_brick);
                        }
                    }
                }
            }
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

    public ArrayList<Brick> getBricks() {
        return this.bricks;
    }

    public int getSize() {
        return bricks.size();
    }
}
