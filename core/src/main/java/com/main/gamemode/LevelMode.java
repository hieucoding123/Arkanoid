package com.main.gamemode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.main.Game;
import com.main.input.IngameInputHandler;
import entity.Effect.*;
import entity.GameScreen;
import entity.Player;
import entity.ScoreManager;
import entity.object.Ball;
import entity.object.brick.Brick;
import entity.object.brick.BricksMap;
import entity.object.Paddle;
import entity.TextureManager;
import table.LevelDataHandler;

import java.util.ArrayList;

public class LevelMode extends GameMode {
    private final ArrayList<BricksMap> bricksMaps;
    private Paddle paddle;
    private BricksMap currentMap;
    private ScoreManager scoreManager;
    GameScreen gameScreen;
    private EffectFactory effectFactory;
    private int levelNumber;
    private int mapIndex;
    private int lives;
    private double timePlayed;

    public LevelMode(Player player, ScoreManager scoreManager, GameScreen gameScreen, int levelNumber) {
        super();
        bricksMaps = new ArrayList<>();
        this.setPlayer(player);
        this.scoreManager = scoreManager;
        this.gameScreen = gameScreen;
        this.effectFactory = new EffectFactory();
        this.levelNumber = levelNumber;
        this.lives = 3;
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
        mapIndex = this.levelNumber;

        if (mapIndex <= 0 || mapIndex > bricksMaps.size()) {
            mapIndex = 1;
        }
        currentMap = bricksMaps.get(mapIndex - 1);
        paddle = new Paddle(Game.SCREEN_WIDTH / 2f - 48, 50, TextureManager.paddleTexture);
        balls.add(new Ball(paddle.getX() + paddle.getWidth() / 2f - 12,
            paddle.getY() + paddle.getHeight(),
            TextureManager.ballTexture,
            5.0f)
        );

        this.inputHandler = new IngameInputHandler(this);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(gameScreen.getStage());
        multiplexer.addProcessor(this.inputHandler);
        Gdx.input.setInputProcessor(multiplexer);
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
            this.lives--;
        }

        if (followPaddle) {       // follow paddle
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
                        if (mapIndex == 1) {
                            newEffectItem = effectFactory.tryCreateEffectItem(brick, paddle, ball,
                                0.05, 0.09, 0.12, 0.14, 0.16, 0.18, 0.20);
                        } else if  (mapIndex == 2) {
                            newEffectItem = effectFactory.tryCreateEffectItem(brick, paddle, ball,
                                0.04, 0.07, 0.10, 0.12, 0.14, 0.16, 0.18);
                        } else if (mapIndex == 3) {
                            newEffectItem = effectFactory.tryCreateEffectItem(brick, paddle, ball,
                                0.03, 0.06, 0.08, 0.10, 0.12, 0.14, 0.15);
                        } else if (mapIndex == 4) {
                            newEffectItem = effectFactory.tryCreateEffectItem(brick, paddle, ball,
                                0.03, 0.05, 0.07, 0.08, 0.09, 0.10, 0.12);
                        } else {
                            newEffectItem = effectFactory.tryCreateEffectItem(brick, paddle, ball,
                                0.02, 0.04, 0.05, 0.06, 0.07, 0.08, 0.10);
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

        if ((currentMap.getBricks().isEmpty() && !this.isEnd()) || (this.lives == 0)) {
            this.setEnd(true);
            double levelscore = this.scoreManager.getScore();
            double bonusscore = (300.0 - (double)this.timePlayed) * (levelscore / 300.0);

            double total_score = levelscore + bonusscore;
            if (total_score < 0) total_score = 0;
            if (currentMap.getBricks().isEmpty() && !this.isEnd()) LevelDataHandler.updatePlayerScore(this.getPlayer().getName(), this.levelNumber, (double)((int)(total_score)), true);
            else LevelDataHandler.updatePlayerScore(this.getPlayer().getName(), this.levelNumber, (double)((int)(total_score)), false);
        }
    }

    @Override
    public void render(SpriteBatch sp, float delta) {
        this.draw(sp);
        gameScreen.setLives(this.lives);
        gameScreen.setTime(this.timePlayed);
    }

    @Override
    public void handleInput() {
        inputHandler.processMovement();
    }

    @Override
    public void draw(SpriteBatch sp) {
        sp.draw(TextureManager.bgTexture, 0, 0, 800, 1000);
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
        followPaddle = true;
    }

    @Override
    public Paddle getPaddle1() {
        return this.paddle;
    }

}
