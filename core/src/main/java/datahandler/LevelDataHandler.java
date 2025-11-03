package datahandler;

import jdk.internal.vm.annotation.Hidden;
import java.util.ArrayList;

/**
 * Handle data from LevelMode.
 */
public class LevelDataHandler extends BaseLevelDataHandler {

    private static final String TABLE_NAME = "level_scores";
    private static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
        + " playerName TEXT PRIMARY KEY NOT NULL,"
        + " level1 REAL DEFAULT 0,"
        + " level2 REAL DEFAULT 0,"
        + " level3 REAL DEFAULT 0,"
        + " level4 REAL DEFAULT 0,"
        + " level5 REAL DEFAULT 0,"
        + " total REAL DEFAULT 0,"
        + " maxLevelUnlocked INTEGER DEFAULT 1"
        + ");";

    @Hidden
    public static String getUrlDatabase() {
        return "jdbc:sqlite:leaderBoard/level_scores.db";
    }

    /**
     * Get max level of player.
     * @param playerName name of player
     * @return max level of player
     */
    public static int getPlayerMaxLevel(String playerName) {
        return getPlayerMaxLevel(playerName, getUrlDatabase(), SQL_CREATE_TABLE, TABLE_NAME);
    }

    /**
     * Update player achievement.
     * @param playerName name of player
     * @param levelNumber player level achieved
     * @param newScore new score of player
     * @param Win player win or lose
     */
    public static void updatePlayerScore(String playerName, int levelNumber, double newScore, boolean Win) {
        updatePlayerScore(playerName, levelNumber, newScore, Win, getUrlDatabase(), SQL_CREATE_TABLE, TABLE_NAME);
    }

    /**
     * Get total score player achieved
     * @param playerName name of player
     * @return total score
     */
    public static double getPlayerTotalScore(String playerName) {
        return getPlayerTotalScore(playerName, getUrlDatabase(), TABLE_NAME);
    }

    /**
     * Get player's score in specific level
     * @param playerName name of player
     * @param levelNumber game level
     * @return player's score in level
     */
    public static double getPlayerScoreForLevel(String playerName, int levelNumber) {
        return getPlayerScoreForLevel(playerName, levelNumber, getUrlDatabase(), TABLE_NAME);
    }

    /**
     * Get data from table in database.
     * Query table to get data.
     * @return data
     */
    public static ArrayList<String> getLeaderboardData() {
        return getLeaderboardData(getUrlDatabase(), SQL_CREATE_TABLE, TABLE_NAME);
    }
}
