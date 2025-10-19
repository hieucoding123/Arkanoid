package entity.gamemode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.main.Game;
import entity.Ball;
import entity.BricksMap;
import entity.Paddle;
import entity.TextureManager;

import java.util.ArrayList;

public class InfiniteMode extends GameMode {
    private final ArrayList<BricksMap> bricksMaps;
    private final ArrayList<Ball> balls;
    boolean flowPaddle = true;      // Ball follow paddle
    private Paddle paddle;
    private BricksMap currentMap;

    public InfiniteMode() {
        super();
        balls = new ArrayList<>();
        bricksMaps = new ArrayList<>();
        create();
    }

    @Override
    public void create() {
        for (int i = 1; i <= 5; i++) {
            String mapPath = "/maps/map" + i + ".txt";
            bricksMaps.add(new BricksMap(mapPath));
        }
        currentMap =  bricksMaps.get(0);
        paddle = new Paddle(Game.SCREEN_WIDTH / 2f - 48, 50, TextureManager.paddleTexture);
        balls.add(new Ball(paddle.getX() + paddle.getWidth() / 2f - 12,
            paddle.getY() + paddle.getHeight(),
            TextureManager.ballTexture,
            2.0f)
        );
    }

    @Override
    public void update() {
        currentMap.update();
        paddle.update();
        if (balls.isEmpty()) this.reset();

        if (flowPaddle) {       // follow paddle
            balls.get(0).setX(paddle.getX() + (paddle.getWidth() / 2f) - balls.get(0).getWidth() / 2f);
            balls.get(0).setY(paddle.getY() + paddle.getHeight());
            balls.get(0).setAngle((float)Math.PI / 2f);
        }
        for (Ball ball : balls) {
            ball.update();
            ball.collisionWith(paddle);
            ball.collisionWith(currentMap);
        }
        balls.removeIf(Ball::isDestroyed);
    }

    @Override
    public void render(SpriteBatch sp) {
        this.handleInput(
            com.badlogic.gdx.Input.Keys.LEFT,
            com.badlogic.gdx.Input.Keys.RIGHT,
            com.badlogic.gdx.Input.Keys.UP,
            com.badlogic.gdx.Input.Keys.DOWN
        );
        this.update();
        this.draw(sp);
    }

    @Override
    public void handleInput(int LEFT, int RIGHT, int UP, int DOWN) {
        //Press LEFT
        if (Gdx.input.isKeyPressed(LEFT)) {
            paddle.moveLeft();
        }
        //Press RIGHT
        else if (Gdx.input.isKeyPressed(RIGHT)) {
            paddle.moveRight();
        }
        //IF NO PRESS KEEP IT STAND
        else {
            paddle.setVelocity(0, 0);
        }
        // New state of the ball
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.SPACE)) {
            flowPaddle = false;             // pulled ball up
            balls.get(0).updateVelocity();
        }
    }

    @Override
    public void draw(SpriteBatch sp) {
        sp.draw(TextureManager.bgTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        currentMap.draw(sp);
        for (Ball ball : balls) ball.draw(sp);
        paddle.draw(sp);
    }

    public void reset() {
        balls.clear();
        balls.add(new Ball(paddle.getX() + (paddle.getWidth() / 2f) - 12,
            paddle.getY() + paddle.getHeight(),
            TextureManager.ballTexture, 3.0f));
        paddle.setX(Game.SCREEN_WIDTH / 2f - paddle.getWidth() / 2f);
        paddle.setY(50);
        flowPaddle = true;
    }
}
