package com.main.gamemode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.main.Game;
import entity.Effect.*;
import entity.GameScreen;
import entity.Player;
import entity.ScoreManager;
import entity.object.Ball;
import entity.object.brick.Brick;
import entity.object.brick.BricksMap;
import entity.object.Paddle;
import entity.TextureManager;

import java.util.ArrayList;

public class LevelMode extends GameMode {
    private final ArrayList<BricksMap> bricksMaps;
    private final ArrayList<Ball> balls;
    boolean flowPaddle = true;      // Ball follow paddle
    private Paddle paddle;
    private BricksMap currentMap;
    private ScoreManager scoreManager;
    GameScreen gameScreen;
    private EffectFactory effectFactory;
    private int levelNumber;
    private int mapIndex;
    private int revie;
    private double timePlayed;
    private boolean start = false;

    public LevelMode(Player player, ScoreManager scoreManager, GameScreen gameScreen, int levelNumber) {
        super();
        balls = new ArrayList<>();
        bricksMaps = new ArrayList<>();
        this.setPlayer(player);

        this.scoreManager = scoreManager;
        this.gameScreen = gameScreen;
        this.effectFactory = new EffectFactory();
        this.levelNumber = levelNumber;
        this.revie = 3;
        this.setEnd(false);
        this.timePlayed = 0.0f;
        create();
    }

    @Override
    public void create() {
        gameScreen.create();

        for (int i = 1; i <= 5; i++) {
            String mapPath = "/maps_for_levelmode/map" + i + ".txt";
            bricksMaps.add(new BricksMap(mapPath));
        }
        mapIndex = this.levelNumber - 1;

        if (mapIndex < 0 || mapIndex >= bricksMaps.size()) {
            mapIndex = 0;
        }
        currentMap = bricksMaps.get(mapIndex);
        paddle = new Paddle(Game.SCREEN_WIDTH / 2f - 48, 50, TextureManager.paddleTexture);
        balls.add(new Ball(paddle.getX() + paddle.getWidth() / 2f - 12,
            paddle.getY() + paddle.getHeight(),
            TextureManager.ballTexture,
            5.0f)
        );
    }

    @Override
    public void update(float delta) {
        if (this.isEnd()) return;
        if (start) this.timePlayed += delta;
        currentMap.update(delta, this.scoreManager);
        paddle.update(delta);
        EffectItem.updateEffectItems(paddle, this.balls, delta);

        if (balls.isEmpty()) {
            scoreManager.deduction();
            this.reset();
            this.revie--;
            if (this.revie == 0) {
                EffectItem.clear();
            }
        }

        if (flowPaddle) {       // follow paddle
            balls.get(0).setX(paddle.getX() + (paddle.getWidth() / 2f) - balls.get(0).getWidth() / 2f);
            balls.get(0).setY(paddle.getY() + paddle.getHeight());
            balls.get(0).setAngle((float)Math.PI / 2f);
        }
        for (Ball ball : balls) {
            ball.update(delta);
            ball.collisionWith(paddle);

            for (Brick brick : currentMap.getBricks()) {
                if (ball.checkCollision(brick)) {
                    brick.takeHit();
                    if (ball.isBig()) brick.setHitPoints(0);
                    if (brick.gethitPoints() == 0) {
                        EffectItem newEffectItem = null;
                        if (mapIndex == 0) {
                            newEffectItem = effectFactory.tryCreateEffectItem(brick, paddle, ball,
                                0.06, 0.11, 0.15, 0.19, 0.20);
                        } else if  (mapIndex == 1) {
                            newEffectItem = effectFactory.tryCreateEffectItem(brick, paddle, ball,
                                0.05, 0.09, 0.13, 0.16, 0.18);
                        } else if (mapIndex == 2) {
                            newEffectItem = effectFactory.tryCreateEffectItem(brick, paddle, ball,
                                0.04, 0.07, 0.10, 0.13, 0.15);
                        } else if (mapIndex == 3) {
                            newEffectItem = effectFactory.tryCreateEffectItem(brick, paddle, ball,
                                0.03, 0.06, 0.08, 0.10, 0.12);
                        } else {
                            newEffectItem = effectFactory.tryCreateEffectItem(brick, paddle, ball,
                                0.02, 0.04, 0.06, 0.08, 0.10);
                        }

                        if (newEffectItem != null) {
                            EffectItem.addEffectItem(newEffectItem);
                        }
                        scoreManager.comboScore(brick);
                        if (brick.getExplosion()) {
                            brick.startExplosion();
                        } else {
                            brick.setDestroyed(true);
                        }
                    }
                    float ballCenterX = ball.getX() + ball.getWidth() / 2f;
                    float ballCenterY = ball.getY() + ball.getHeight() / 2f;
                    //Bottom and top collision
                    if (ballCenterX > brick.getX() && ballCenterX < brick.getX() + brick.getWidth()) {
                        ball.reverseY();
                    }
                    //Left and right collision
                    else if (ballCenterY > brick.getY() && ballCenterY < brick.getY() + brick.getHeight()) {
                        ball.reverseX();
                    }
                    //Corner collision
                    else {
                        ball.reverseY();
                        ball.reverseX();
                    }
                    break;
                }
            }
        }
        balls.removeIf(Ball::isDestroyed);

        if ((currentMap.getBricks().isEmpty() && this.isEnd() == false) || (this.revie == 0)) {
            this.setEnd(true);
            double levelscore = this.scoreManager.getScore();
            double bonusscore = (300.0 - (double)this.timePlayed) * (levelscore / 300.0);

            double total_score = levelscore + bonusscore;
            if (total_score < 0) total_score = 0;

        }
    }

    @Override
    public void render(SpriteBatch sp, float delta) {
        this.update(delta);
        this.handleInput();
        this.draw(sp);
        gameScreen.setLives(this.revie);
        gameScreen.setTime(this.timePlayed);
        gameScreen.render();
    }

    @Override
    public void handleInput() {
        //Press LEFT
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.LEFT)) {
            start = true;
            paddle.moveLeft();
        }
        //Press RIGHT
        else if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.RIGHT)) {
            start = true;
            paddle.moveRight();
        }
        //IF NO PRESS KEEP IT STAND
        else {
            paddle.setVelocity(0, 0);
        }
        // New state of the ball
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.SPACE)) {
            flowPaddle = false;             // pulled ball up
            start = true;
            balls.get(0).updateVelocity();
        }
    }

    @Override
    public void draw(SpriteBatch sp) {
        sp.draw(TextureManager.bgTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        currentMap.draw(sp);
        EffectItem.drawEffectItems(sp);
        if (ShieldEffect.isShield()) {
            sp.draw(
                TextureManager.lineTexture, Game.padding_left_right,
                0,
                Game.SCREEN_WIDTH - 2 * Game.padding_left_right,
                5
            );
        }
        for (Ball ball : balls) ball.draw(sp);
        paddle.draw(sp);
    }

    public void reset() {
        balls.clear();
        balls.add(new Ball(paddle.getX() + (paddle.getWidth() / 2f) - 12,
            paddle.getY() + paddle.getHeight(),
            TextureManager.ballTexture, 5.0f));
        paddle.setX(Game.SCREEN_WIDTH / 2f - paddle.getWidth() / 2f);
        paddle.setY(50);
        flowPaddle = true;
    }
}
