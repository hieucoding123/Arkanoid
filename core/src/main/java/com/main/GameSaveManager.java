package com.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.main.gamemode.CoopMode;
import com.main.gamemode.GameMode;
import com.main.gamemode.LevelMode;
import entity.Player;
import entity.object.Ball;
import entity.object.Paddle;
import entity.object.brick.Brick;
import entity.object.brick.BricksMap;
import entity.ScoreManager;
import entity.TextureManager;
import entity.Effect.*;

import java.util.ArrayList;

public class GameSaveManager {

    private static Json json = new Json();

    static boolean isSaveableGameMode(GameState state) {
        if (state == GameState.LEVEL1 ||  state == GameState.LEVEL2 || state == GameState.LEVEL3
            || state == GameState.LEVEL4 || state == GameState.LEVEL5) {
            return true;
        } else {
            return false;
        }
    }

    private static FileHandle getSaveFile(Player player, GameState state, boolean isCoop) {
        String playerName = player.getName();
        if (playerName == null || playerName.isEmpty() || playerName.equals("Enter name")) {
            playerName = "Guest";
        }

        String safePlayerName = "";
        for (int i = 0; i < playerName.length(); i++) {
            if (playerName.charAt(i) == '.' || playerName.charAt(i) == '-' || (playerName.charAt(i) >= 'a' &&  playerName.charAt(i) <= 'z')
            || (playerName.charAt(i) >= 'A' &&  playerName.charAt(i) <= 'Z') ||  (playerName.charAt(i) >= '0' && playerName.charAt(i) <= '9')) {
                safePlayerName += playerName.charAt(i);
            } else {
                safePlayerName += '_';
            }
        }
        String coopSuffix = isCoop ? "_COOP" : "_SOLO";
        String fileName = "save_" + safePlayerName + "_" + state.name() + coopSuffix + ".json";

        return Gdx.files.local("saves/" + fileName);
    }

    public static boolean HaveToSave(Player player, GameState state, boolean isCoop) {
        if (player == null || state == null || !isSaveableGameMode(state)) {
            return false;
        }
        FileHandle file = getSaveFile(player, state, isCoop);
        return file.exists() && file.length() > 0;
    }

    public static void deleteSave(Player player, GameState state, boolean isCoop) {
        if (player == null || state == null || !isSaveableGameMode(state)) {
            return;
        }
        FileHandle file = getSaveFile(player, state, isCoop);
        if (file.exists()) {
            file.delete();
        }
    }

    public static void saveGame(Player player, GameMode gameMode, GameState state, ScoreManager scoreManager, int lives, double timePlayed, boolean isCoop) {
        if (player == null || gameMode == null || state == null || scoreManager == null || !isSaveableGameMode(state)) {
            return;
        }
        GameData data = new GameData();
        data.score = scoreManager.getScore();
        data.lives = lives;
        data.timePlayed = timePlayed;
        data.followPaddle = gameMode.isFollowPaddle();
        data.isCoop = isCoop;
        data.cnt_combo = scoreManager.getComboCount();
        data.TimeLastHit = scoreManager.getLastHitTime();

        if (gameMode instanceof LevelMode) {
            data.levelNumber = ((LevelMode) gameMode).getLevelNumber();
        } else if (gameMode instanceof CoopMode) {
            data.levelNumber = ((CoopMode) gameMode).getLevelNumber();
        }

        Paddle p1 = gameMode.getPaddle1();
        if (p1 != null) {
            data.paddle1 = new GameData.PaddleData();
            data.paddle1.x = p1.getX();
            data.paddle1.y = p1.getY();
            data.paddle1.expandEnd = p1.getExpandEnd();
            data.paddle1.StunEnd = p1.getStunEnd();
            data.paddle1.shieldActive = p1.hasShield();
        }

        Paddle p2 = gameMode.getPaddle2();
        if (p2 != null) {
            data.paddle2 = new GameData.PaddleData();
            data.paddle2.x = p2.getX();
            data.paddle2.y = p2.getY();
            data.paddle2.expandEnd = p2.getExpandEnd();
            data.paddle2.StunEnd = p2.getStunEnd();
            data.paddle2.shieldActive = p2.hasShield();
        }

        for (Ball ball : gameMode.getBalls()) {
            GameData.BallData ballData = new GameData.BallData();
            ballData.x = ball.getX();
            ballData.y = ball.getY();
            ballData.dx = ball.getDx();
            ballData.dy = ball.getDy();
            ballData.angle = ball.getAngle();
            ballData.speed = ball.getSpeed();
            ballData.originalspeed = ball.getOriginalSpeed();
            ballData.BigEnd = ball.getBigEnd();
            ballData.SlowEnd = ball.getSlowEnd();
            ballData.FastEnd = ball.getFastEnd();
            data.balls.add(ballData);
        }

        BricksMap map = gameMode.getCurrentMap();
        if (map != null) {
            for (Brick brick : map.getBricks()) {
                GameData.BrickData brickData = new GameData.BrickData();
                brickData.x = brick.getX();
                brickData.y = brick.getY();
                brickData.hitPoints = brick.gethitPoints();
                brickData.isDestroyed = brick.isDestroyed();
                brickData.unbreak = brick.isUnbreak();
                brickData.color = brick.getColor();
                brickData.row = brick.getRow();
                brickData.col = brick.getCol();
                brickData.dx = brick.getDx();
                brickData.dy = brick.getDy();
                data.bricks.add(brickData);
            }
        }

        for (EffectItem item : EffectItem.getItems()) {
            GameData.EffectItemData itemData = new GameData.EffectItemData();
            itemData.x = item.getX();
            itemData.y = item.getY();
            itemData.dx = item.getDx();
            itemData.dy = item.getDy();
            itemData.EffectType = item.getClass().getSimpleName();
            data.effectItems.add(itemData);
        }

        try {
            FileHandle file = getSaveFile(player, state, isCoop);
            file.writeString(json.prettyPrint(data), false);
        } catch (Exception e) {
            Gdx.app.error("GameSaveManager", "Failed to save game!", e);
        }
    }

    public static void loadGame(Player player, GameMode gameMode, GameState state, ScoreManager scoreManager, boolean isCoop) {
        FileHandle file = getSaveFile(player, state, isCoop);
        if (!file.exists()) {
            return;
        }

        try {
            String fileData = file.readString();
            GameData data = json.fromJson(GameData.class, fileData);

            scoreManager.setScore(data.score);
            gameMode.setLives(data.lives);
            gameMode.setTimePlayed(data.timePlayed);
            gameMode.setFollowPaddle(data.followPaddle);
            scoreManager.setComboCount(data.cnt_combo);
            scoreManager.setLastHitTime(data.TimeLastHit);

            Paddle p1 = gameMode.getPaddle1();
            if (p1 != null && data.paddle1 != null) {
                p1.setX(data.paddle1.x);
                p1.setY(data.paddle1.y);
                p1.setExpandEnd(data.paddle1.expandEnd);
                p1.setStunEnd(data.paddle1.StunEnd);
                p1.setShieldActive(data.paddle1.shieldActive);
            }

            Paddle p2 = gameMode.getPaddle2();
            if (p2 != null && data.paddle2 != null) {
                p2.setX(data.paddle2.x);
                p2.setY(data.paddle2.y);
                p2.setExpandEnd(data.paddle2.expandEnd);
                p2.setStunEnd(data.paddle2.StunEnd);
                p2.setShieldActive(data.paddle2.shieldActive);
            }

            gameMode.getBalls().clear();
            for (GameData.BallData ballData : data.balls) {
                Ball ball = new Ball(ballData.x, ballData.y, TextureManager.ballTexture, ballData.originalspeed);
                ball.setAngle(ballData.angle);
                ball.setBigEnd(ballData.BigEnd);
                ball.setSlowEnd(ballData.SlowEnd);
                ball.setFastEnd(ballData.FastEnd);
                gameMode.getBalls().add(ball);
            }

            BricksMap map = gameMode.getCurrentMap();
            if (map != null) {
                ArrayList<Brick> mapBricks = map.getBricks();
                for (Brick brick : mapBricks) {
                    brick.setDestroyed(true);
                }
                for (GameData.BrickData brickData : data.bricks) {
                    for (Brick brick : mapBricks) {
                        if (brick.getRow() == brickData.row && brick.getCol() == brickData.col) {
                            brick.setHitPoints(brickData.hitPoints);
                            brick.setDestroyed(brickData.isDestroyed);
                            if (brickData.unbreak) brick.setUnbreak();

                            brick.setX(brickData.x);
                            brick.setY(brickData.y);
                            brick.setDx(brickData.dx);
                            brick.setDy(brickData.dy);

                            break;
                        }
                    }
                }
                mapBricks.removeIf(Brick::isDestroyed);
            }

            EffectItem.clear();
            for (GameData.EffectItemData itemData : data.effectItems) {
                EffectItem newItem = null;
                float x = itemData.x;
                float y = itemData.y;
                float dy = 0;
                switch (itemData.EffectType) {
                    case "ExpandEffect":
                        newItem = new ExpandEffect(x, y, dy);
                        break;
                    case "ShieldEffect":
                        newItem = new ShieldEffect(x, y, dy);
                        break;
                    case "UnbreakBrickEffect":
                        newItem = new UnbreakBrickEffect(x, y, dy);
                        break;
                    case "RandomEffect":
                        newItem = new RandomEffect(x, y, dy);
                        break;
                    case "RemoveEffect":
                        newItem = new RemoveEffect(x, y, dy);
                        break;
                    case "StunPaddleEffect":
                        newItem = new StunPaddleEffect(x, y, dy);
                        break;
                    case "BigballEffect":
                        newItem = new BigballEffect(x, y, dy);
                        break;
                    case "SlowBallEffect":
                        newItem = new SlowBallEffect(x, y, dy);
                        break;
                    case "FastBallEffect":
                        newItem = new FastBallEffect(x, y, dy);
                        break;
                    case "ThreeBallsEffect":
                        newItem = new ThreeBallsEffect(x, y, dy);
                        break;
                }

                if (newItem != null) {
                    newItem.setDx(itemData.dx);
                    newItem.setDy(itemData.dy);
                }
            }
            gameMode.isStart(true);

        } catch (Exception e) {
            Gdx.app.error("GameSaveManager", "Failed to load game!", e);
            deleteSave(player, state, isCoop);
        }
    }
}
