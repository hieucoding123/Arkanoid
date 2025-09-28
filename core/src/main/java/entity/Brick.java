package entity;

public class Brick extends GameObject {
    private final static int WIDTH = 74;
    private final static int HEIGHT = 30;

    private boolean isDestroyed;

    public Brick(int x, int y, String path) {
        super(x, y, WIDTH, HEIGHT, path);
        isDestroyed = false;
    }
    public boolean isDestroyed() {
        return isDestroyed;
    }
    public void destroy() {
        isDestroyed = true;
    }
}
