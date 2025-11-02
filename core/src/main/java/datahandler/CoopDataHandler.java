package datahandler;

import jdk.internal.vm.annotation.Hidden;
import java.util.ArrayList;

public class CoopDataHandler extends BaseLevelDataHandler {

    private static final String TABLE_NAME = "coop_scores";
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
        return "jdbc:sqlite:leaderBoard/coop_scores.db";
    }

    public static int getPlayerMaxLevel(String playerName) {
        return getPlayerMaxLevel(playerName, getUrlDatabase(), SQL_CREATE_TABLE, TABLE_NAME);
    }

    public static void updatePlayerScore(String playerName, int levelNumber, double newScore, boolean Win) {
        updatePlayerScore(playerName, levelNumber, newScore, Win, getUrlDatabase(), SQL_CREATE_TABLE, TABLE_NAME);
    }

    public static double getPlayerTotalScore(String playerName) {
        return getPlayerTotalScore(playerName, getUrlDatabase(), TABLE_NAME);
    }

    public static double getPlayerScoreForLevel(String playerName, int levelNumber) {
        return getPlayerScoreForLevel(playerName, levelNumber, getUrlDatabase(), TABLE_NAME);
    }

    public static ArrayList<String> getLeaderboardData() {
        return getLeaderboardData(getUrlDatabase(), SQL_CREATE_TABLE, TABLE_NAME);
    }
}
