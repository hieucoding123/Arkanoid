package com.main.gamemode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.main.Game;
import com.main.input.IngameInputHandler;
import entity.Effect.EffectFactory;
import entity.Effect.EffectItem;
import entity.Effect.ShieldEffect;
import entity.GameScreen;
import entity.Player;
import entity.ScoreManager;
import entity.TextureManager;
import entity.object.Ball;
import entity.object.Paddle;
import entity.object.brick.Brick;
import entity.object.brick.BricksMap;

import java.util.ArrayList;

public class CoopMode extends GameMode {
    private final ArrayList<BricksMap> bricksMaps;
    private Paddle paddle1;
    private Paddle paddle2;
    private BricksMap currentMap;
    private ScoreManager scoreManager;
    GameScreen gameScreen;
    private EffectFactory effectFactory;
    private int levelNumber;
    private int mapIndex;
    private int lives;
    private double timePlayed;

    public CoopMode(Player player, ScoreManager scoreManager, GameScreen gameScreen, int levelNumber) {
        super();
        bricksMaps = new ArrayList<>();

        this.setPlayer(player);
        this.setEnd(false);
        this.scoreManager = scoreManager;
        this.gameScreen = gameScreen;
        this.effectFactory = new EffectFactory();
        this.levelNumber = levelNumber;
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

        paddle1 = new Paddle(Game.SCREEN_WIDTH / 2f - 48, 100, TextureManager.paddleTexture);
        paddle2 = new Paddle(Game.SCREEN_WIDTH / 2f - 48, 20, TextureManager.paddleTexture);

        balls.add(new Ball(paddle1.getX() + paddle1.getWidth() / 2f - 12,
            paddle1.getY() + paddle1.getHeight(),
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
        if (this.isEnd()) {
            return;
        }

        if (start) {
            this.timePlayed += delta;
        }

        currentMap.update(delta, this.scoreManager);
        paddle1.update(delta);
        paddle2.update(delta);
        EffectItem.updateEffectItems(paddle1, this.balls, delta);
        EffectItem.updateEffectItems(paddle2, this.balls, delta);

        if (balls.isEmpty()) {
            scoreManager.deduction();
            this.lives--;
            this.reset();
        }

        if (followPaddle) {
            balls.get(0).setX(paddle1.getX() + paddle1.getWidth() / 2f - balls.get(0).getWidth() / 2f);
            balls.get(0).setY(paddle1.getY() + paddle1.getHeight());
            balls.get(0).setAngle((float) Math.PI / 2f);
        }

        for (Ball ball : balls) {
            ball.update(delta);
            ball.collisionWith(paddle1);
            ball.collisionWith(paddle2);

            for (Brick brick : currentMap.getBricks()) {
                if (ball.checkCollision(brick)) {
                    brick.takeHit();
                    if (ball.isBig()) brick.setHitPoints(0);
                    if (brick.gethitPoints() == 0) {
                        EffectItem newEffectItem1 = null;
                        EffectItem newEffectItem2 = null;
                        if (mapIndex == 1) {
                            newEffectItem1 = effectFactory.tryCreateEffectItem(brick, paddle1, ball,
                                0.06, 0.11, 0.15, 0.19, 0.20);
                            newEffectItem2 = effectFactory.tryCreateEffectItem(brick, paddle2, ball,
                                0.06, 0.11, 0.15, 0.19, 0.20);
                        } else if (mapIndex == 2) {
                            newEffectItem1 = effectFactory.tryCreateEffectItem(brick, paddle1, ball,
                                0.05, 0.09, 0.13, 0.16, 0.18);
                            newEffectItem2 = effectFactory.tryCreateEffectItem(brick, paddle2, ball,
                                0.05, 0.09, 0.13, 0.16, 0.18);
                        } else if (mapIndex == 3) {
                            newEffectItem1 = effectFactory.tryCreateEffectItem(brick, paddle1, ball,
                                0.04, 0.07, 0.10, 0.13, 0.15);
                            newEffectItem2 = effectFactory.tryCreateEffectItem(brick, paddle2, ball,
                                0.04, 0.07, 0.10, 0.13, 0.15);
                        } else if (mapIndex == 4) {
                            newEffectItem1 = effectFactory.tryCreateEffectItem(brick, paddle1, ball,
                                0.03, 0.06, 0.08, 0.10, 0.12);
                            newEffectItem2 = effectFactory.tryCreateEffectItem(brick, paddle2, ball,
                                0.03, 0.06, 0.08, 0.10, 0.12);
                        } else {
                            newEffectItem1 = effectFactory.tryCreateEffectItem(brick, paddle1, ball,
                                0.02, 0.04, 0.06, 0.08, 0.10);
                            newEffectItem2 = effectFactory.tryCreateEffectItem(brick, paddle2, ball,
                                0.02, 0.04, 0.06, 0.08, 0.10);
                        }

                        if (newEffectItem1 != null) {
                            EffectItem.addEffectItem(newEffectItem1);
                        }

                        if (newEffectItem2 != null) {
                            EffectItem.addEffectItem(newEffectItem2);
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
    }

    @Override
    public void render(SpriteBatch sp, float delta) {
        this.update(delta);
        this.draw(sp);
        gameScreen.setLives(this.lives);
        gameScreen.setTime(this.timePlayed);
        gameScreen.render();
    }

    @Override
    public void handleInput() {
        inputHandler.processMovement();
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
        paddle1.draw(sp);
        paddle2.draw(sp);
    }

    public void reset() {
        balls.clear();
        balls.add(new Ball(paddle1.getX() + (paddle1.getWidth() / 2f) - 12,
            paddle1.getY() + paddle1.getHeight(),
            TextureManager.ballTexture, 5.0f));
        paddle1.setX(Game.SCREEN_WIDTH / 2f - paddle1.getWidth() / 2f);
        paddle1.setY(50);
        followPaddle = true;
    }

    @Override
    public Paddle getPaddle1() {
        return this.paddle1;
    }

    @Override
    public Paddle getPaddle2() {
        return this.paddle2;
    }

}
