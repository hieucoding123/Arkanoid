package entity.object.brick;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import entity.ScoreManager;
import entity.TextureManager;
import entity.object.DSU;
import com.main.Game;
import com.badlogic.gdx.math.Rectangle;
import java.util.Random;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.*;

public class BricksMap {
    public final int rows = 15;
    public final int cols = 8;
    public static final int xBeginCoord = 90;
    public static final int yBeginCoord = 810;
    public static final int brickW = 77;
    public static final int brickH = 36;
    private final int[] r = {-1, 1, 0, 0};
    private final int[] c = {0, 0, -1, 1};
    private DSU dsu;
    private Map<Brick, Integer> brick_to_dsu;
    private Map<Integer, Brick> dsu_to_birck;
    private ArrayList<Brick> brick_dsu;
    private Brick[][] brickdsu;
    private Map<Integer, float[]> dsuVelocities = new HashMap<>();
    private Random rand = new Random();
    private static final float DSU_MOVE_SPEED = 1.5f * 60f;
    public final ArrayList<Brick> bricks;
    public boolean check_dsu = false;

    /**
     * Initialize brick map with map file.
     * @param path map file path
     */
    public BricksMap(String path) {
        bricks = new ArrayList<>();
        brick_to_dsu = new HashMap<>();
        dsu_to_birck = new HashMap<>();
        brick_dsu = new ArrayList<>();
        brickdsu = new Brick[rows][cols];
        try {
            InputStream is = getClass().getResourceAsStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            for (int i = 0; i < rows; i++) {
                String[] line = br.readLine().trim().split("\\s+");
                for (int j = 0; j < cols; j++) {
                    int color = Integer.parseInt(line[j]);
                    int radom = (int)(Math.random() * 10);
                    if (radom == -1 && color != -1) {
                        bricks.add(new Brick(
                            xBeginCoord + j * brickW,
                            yBeginCoord - i * brickH,
                            1,
                            true,
                            i, j,
                            color,
                            TextureManager.brickexplo));
                    }
                    else if (color == 1) {
                        bricks.add(new Brick(
                            xBeginCoord + j * brickW,
                            yBeginCoord - i * brickH,
                            1,
                            false,
                            i, j,
                            color,
                            TextureManager.brick1HIT));
                    } else if (color == 0) {
                        bricks.add(new Brick(
                            xBeginCoord + j * brickW,
                            yBeginCoord - i * brickH,
                            2,
                            false,
                            i, j,
                            color,
                            TextureManager.brick2HIT));
                    } else if (color == 31) {
                        bricks.add(new Brick(
                            xBeginCoord + j * brickW,
                            yBeginCoord - i * brickH,
                            1,
                            false,
                            i, j,
                            color,
                            TextureManager.brick1HIT));
                        bricks.get(bricks.size() - 1).setMovement(1.5f, BricksMap.brickW);
                    } else if (color == 32) {
                        bricks.add(new Brick(
                            xBeginCoord + j * brickW,
                            yBeginCoord - i * brickH,
                            2,
                            false,
                            i, j,
                            color,
                            TextureManager.brick2HIT));
                        bricks.get(bricks.size() - 1).setMovement(1.5f, BricksMap.brickW);
                    } else if (color == 41) {
                        check_dsu = true;
                        bricks.add(new Brick(
                            xBeginCoord + j * brickW,
                            yBeginCoord - i * brickH,
                            1,
                            false,
                            i, j,
                            color,
                            TextureManager.brick1HIT));
                        brick_dsu.add(bricks.get(bricks.size() - 1));
                        brickdsu[i][j] = bricks.get(bricks.size() - 1);
                    } else if (color == 42) {
                        check_dsu = true;
                        bricks.add(new Brick(
                            xBeginCoord + j * brickW,
                            yBeginCoord - i * brickH,
                            2,
                            false,
                            i, j,
                            color,
                            TextureManager.brick2HIT));
                        brick_dsu.add(bricks.get(bricks.size() - 1));
                        brickdsu[i][j] = bricks.get(bricks.size() - 1);
                    }
                }
            }
            br.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
        if (check_dsu) {
            buildDsu();
        }
    }

    /**
     * Update bricks(remove, add, explosion, ...).
     * @param delta fps balance
     * @param score Score Manager
     */
    public void update(float delta, ScoreManager score) {
        if (check_dsu) {
            if (dsu == null) {
                buildDsu();
            }
            Set<Integer> roots = dsu.get_roots();

            for (int root_id : roots) {
                float[] velocity = dsuVelocities.get(root_id);
                if (velocity == null) {
                    velocity = RandomVelocity(checkRight(root_id), checkLeft(root_id), checkTop(root_id), checkBottom(root_id));
                    dsuVelocities.put(root_id, velocity);
                }

                float dx = velocity[0];
                float dy = velocity[1];

                if (willDsuCollide(root_id, dx, dy, delta)) {
                    float[] newVelocity = new float[]{-dx, -dy};
                    dsuVelocities.put(root_id, newVelocity);
                    dx = newVelocity[0];
                    dy = newVelocity[1];
                }

                List<Integer> elementId = dsu.get_elements(root_id);
                for (int id : elementId) {
                    Brick brick = dsu_to_birck.get(id);
                    if (brick != null && !brick.isDestroyed()) {
                        brick.setVelocity(dx, dy);
                    }
                }
            }
        }
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
                        Random rand = new Random();
                        int radom = rand.nextInt(4) + 1;
                        if (new_brick != null && new_brick.getExplosion() && !new_brick.isDestroyed()) {
                            save_brick.get(new_row * cols + new_col).setHitPoints(0);
                            new_brick.startExplosion();
                            score.addScore(new_brick);
                        } else if (new_brick != null && radom == 1 && !new_brick.isDestroyed()) {
                            save_brick.get(new_row * cols + new_col).setHitPoints(0);
                            new_brick.startExplosion();
                            score.addScore(new_brick);
                        }
                    }
                }
            }
        }
        bricks.removeIf(Brick::isDestroyed);
    }

    /**
     * Updates the positions and states of all bricks without scoring logic.
     * @param delta time delta for frame-rate balance
     */
    public void update(float delta) {
        for (Brick brick : bricks) {
            brick.update(delta);
        }

        bricks.removeIf(Brick::isDestroyed);
    }

    /**
     * Builds and initializes the DSU (Disjoint Set Union) structure for connected bricks.
     */

    public void buildDsu() {
        brick_dsu.removeIf(Brick::isDestroyed);
        dsu = new DSU(brick_dsu.size());
        brick_to_dsu.clear();
        dsu_to_birck.clear();

        for (int i = 0; i < brick_dsu.size(); i++) {
            Brick brick = brick_dsu.get(i);
            brick_to_dsu.put(brick, i);
            dsu_to_birck.put(i, brick);
        }

        for (Brick brick : brick_dsu) {
            int row = brick.getRow();
            int col = brick.getCol();
            Integer current = brick_to_dsu.get(brick);
            if (current == null) {
                continue;
            }

            for (int i = 0; i < r.length; i++) {
                int new_row = row + r[i];
                int new_col = col + c[i];
                Brick neighbor = getbrickdsu(new_row, new_col);
                if (neighbor != null && !neighbor.isDestroyed()) {
                    if (brick_to_dsu.get(neighbor) != null) {
                        dsu.set_union(current,  brick_to_dsu.get(neighbor));
                    }
                }
            }
        }

        Set<Integer> roots = dsu.get_roots();
        Map<Integer, float[]> old_velocities = new HashMap<>(dsuVelocities);
        dsuVelocities.clear();

        for (int i : roots) {
            if (old_velocities.containsKey(i)) {
                dsuVelocities.put(i, old_velocities.get(i));
            } else {
                dsuVelocities.put(i, RandomVelocity(checkRight(i), checkLeft(i), checkTop(i), checkBottom(i)));
            }
        }
    }

    /**
     * Rebuilds the DSU when a brick is destroyed.
     * @param brick the brick that was destroyed
     */
    public void onBrickDestroyed(Brick brick) {
        brickdsu[brick.getRow()][brick.getCol()] = null;
        buildDsu();
    }

    /**
     * Returns the brick at the specified DSU grid coordinates.
     * @param row the brick's row index
     * @param col the brick's column index
     * @return the brick at the specified position, or null if invalid or destroyed
     */
    public Brick getbrickdsu(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return null;
        }
        Brick brick = brickdsu[row][col];
        if (brick == null || brick.isDestroyed()) {
            return null;
        }
        return brick;
    }

    /**
     * Generates a random velocity for a DSU group.
     * @param isRightBlocked whether movement to the right is blocked
     * @param isLeftBlocked whether movement to the left is blocked
     * @param isTopBlocked whether upward movement is blocked
     * @param isBottomBlocked whether downward movement is blocked
     * @return a random 2D velocity vector
     */
    public float[] RandomVelocity(boolean isRightBlocked, boolean isLeftBlocked, boolean isTopBlocked, boolean isBottomBlocked) {
        ArrayList<float[]> radom = new ArrayList<>();

        if (!isRightBlocked) radom.add(new float[]{DSU_MOVE_SPEED, 0});
        if (!isLeftBlocked) radom.add(new float[]{-DSU_MOVE_SPEED, 0});
        if (!isTopBlocked) radom.add(new float[]{0, DSU_MOVE_SPEED});
        if (!isBottomBlocked) radom.add(new float[]{0, -DSU_MOVE_SPEED});

        if (radom.isEmpty()) {
            return new float[]{0, 0};
        }

        return radom.get(rand.nextInt(radom.size()));
    }

    /**
     * Checks if the DSU group touches the right boundary.
     *
     * @param root_id the DSU root identifier
     * @return true if the right edge is blocked, false otherwise
     */
    public boolean checkRight(int root_id) {
        List<Integer> elementIds = dsu.get_elements(root_id);
        if (elementIds == null || elementIds.isEmpty()) {
            return false;
        }
        for (int id : elementIds) {
            Brick brick = dsu_to_birck.get(id);
            if (brick == null || brick.isDestroyed()) {
                continue;
            }
            if (brick.getX() + brick.getWidth() >= Game.SCREEN_WIDTH - Game.padding_left_right) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the DSU group touches the left boundary.
     * @param root_id the DSU root identifier
     * @return true if the left edge is blocked, false otherwise
     */
    public boolean checkLeft(int root_id) {
        List<Integer> elementIds = dsu.get_elements(root_id);
        if (elementIds == null || elementIds.isEmpty()) {
            return false;
        }
        for (int id : elementIds) {
            Brick brick = dsu_to_birck.get(id);
            if (brick == null || brick.isDestroyed()) {
                continue;
            }
            if (brick.getX() <= Game.padding_left_right) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the DSU group touches the top boundary.
     * @param root_id the DSU root identifier
     * @return true if the top edge is blocked, false otherwise
     */
    public boolean checkTop(int root_id) {
        List<Integer> elementIds = dsu.get_elements(root_id);
        if (elementIds == null || elementIds.isEmpty()) {
            return false;
        }
        for (int id : elementIds) {
            Brick brick = dsu_to_birck.get(id);
            if (brick == null || brick.isDestroyed()) {
                continue;
            }
            if (brick.getY() + brick.getHeight() >= Game.padding_top) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the DSU group touches the bottom boundary.
     * @param root_id the DSU root identifier
     * @return true if the bottom edge is blocked, false otherwise
     */
    public boolean checkBottom(int root_id) {
        List<Integer> elementIds = dsu.get_elements(root_id);
        if (elementIds == null || elementIds.isEmpty()) {
            return false;
        }
        for (int id : elementIds) {
            Brick brick = dsu_to_birck.get(id);
            if (brick == null || brick.isDestroyed()) {
                continue;
            }
            if (brick.getY() <= yBeginCoord - brickH * (rows - 1)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if a DSU group will collide with walls or other bricks after movement.
     * @param rootId the DSU root ID
     * @param dx horizontal velocity
     * @param dy vertical velocity
     * @param delta frame time delta
     * @return true if a collision would occur, false otherwise
     */
    private boolean willDsuCollide(int rootId, float dx, float dy, float delta) {
        List<Integer> elementIds = dsu.get_elements(rootId);

        for (int id : elementIds) {
            Brick brick = dsu_to_birck.get(id);
            if (brick == null || brick.isDestroyed()) continue;

            float nextX = brick.getX() + dx * delta;
            float nextY = brick.getY() + dy * delta;
            float width = brick.getWidth();
            float height = brick.getHeight();

            if (nextX < Game.padding_left_right || nextX + width > Game.SCREEN_WIDTH - Game.padding_left_right) {
                return true;
            }
            if (nextY + height > Game.padding_top || nextY < yBeginCoord - brickH * (rows - 1)) {
                return true;
            }

            Rectangle another = new Rectangle(nextX, nextY, width, height);

            for (Brick otherBrick : brick_dsu) {
                if (otherBrick == null || otherBrick.isDestroyed() || otherBrick == brick) {
                    continue;
                }

                Integer otherId = brick_to_dsu.get(otherBrick);
                if (otherId != null && dsu.find(id) == dsu.find(otherId)) {
                    continue;
                }

                if (another.overlaps(otherBrick.getBounds())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Draw bricks on game screen.
     * @param batch game drawing programming
     */
    public void draw(SpriteBatch batch) {
        for (Brick brick : bricks) {
            if (!brick.isDestroyed()) {
                brick.draw(batch);
            }
        }
    }

    /**
     * Gets the list of all bricks in the map.
     * @return the list of bricks
     */
    public ArrayList<Brick> getBricks() {
        return this.bricks;
    }

    /**
     * Gets the number of remaining bricks.
     * @return total brick count
     */
    public int getSize() {
        return bricks.size();
    }

    /**
     * Counts the number of breakable bricks.

     * @return number of breakable bricks
     */
    public int getNumberBreakBrick() {
        int cnt = 0;
        for (Brick brick : bricks) {
            if (!brick.isUnbreak()) {
                cnt += 1;
            }
        }
        return cnt;
    }
}
