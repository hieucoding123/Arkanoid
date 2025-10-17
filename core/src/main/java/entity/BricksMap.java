package entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import entity.ScoreManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;

public class BricksMap {
    public final int rows = 15;
    public final int cols = 10;
    public static final int xBeginCoord = 25;
    public final int yBeginCoord = 820;
    public final int brickW = 75;
    public final int brickH = 35;
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
                            TextureManager.brickTextures.get(color)));
                    } else if (color == 0) {
                        bricks.add(new Brick(
                            xBeginCoord + j * brickW,
                            yBeginCoord - i * brickH,
                            2,
                            explosion_check,
                            i, j,
                            TextureManager.brickTextures.get(color)));
                    }
                }
            }
            br.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void bfs_explosion(int startRow, int startCol, ScoreManager score)  {
        Map<Integer, Brick> save_brick = new HashMap<>();
        Brick[][] grid = new Brick[rows][cols];
        for (Brick b : bricks) {
            if (b != null && !b.isDestroyed()) {
                save_brick.put(b.getRow() * cols + b.getCol(), b);
                grid[b.getRow()][b.getCol()] = b;
            }
        }
        Queue<Brick> q = new LinkedList<>();
        boolean [][] visited = new boolean[rows][cols];

        Brick start = grid[startRow][startCol];
        save_brick.get(startRow * cols + startCol).setHitPoints(0);
        q.add(start);
        visited[startRow][startCol] = true;

        while (!q.isEmpty()) {
            Brick cur = q.poll();
            cur.setHitPoints(0);

            for (int i = 0; i < r.length; i++) {
                int new_row = cur.getRow() + r[i];
                int new_col = cur.getCol() + c[i];

                if (new_row >= 0 && new_row < rows && new_col >= 0 && new_col < cols) {
                    Brick new_brick = grid[new_row][new_col];
                    if (new_brick != null && !visited[new_row][new_col] && new_brick.getExplosion()) {
                        save_brick.get(new_row * cols + new_col).setHitPoints(0);
                        visited[new_row][new_col] = true;
                        q.add(new_brick);
                        score.addScore();
                    }
                }
            }
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

    public ArrayList<Brick> getBricks() {
        return this.bricks;
    }

    public int getsize() {
        return bricks.size();
    }
}
