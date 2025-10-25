package table;

import com.badlogic.gdx.Gdx;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LevelDatabase {

    private static final String URL_DATABASE = "jdbc:sqlite:leaderBoard/level_scores.db";
    private static boolean isLoaded = false;

    private static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS level_scores ("
        + " playerName TEXT PRIMARY KEY NOT NULL,"
        + " level1 REAL DEFAULT 0,"
        + " level2 REAL DEFAULT 0,"
        + " level3 REAL DEFAULT 0,"
        + " level4 REAL DEFAULT 0,"
        + " level5 REAL DEFAULT 0,"
        + " total REAL DEFAULT 0,"
        + " maxLevelUnlocked INTEGER DEFAULT 1"
        + ");";


    private static void loadDriverIfNeeded() {
        if (isLoaded) {
            return;
        }
        else {
            try {
                Class.forName("org.sqlite.JDBC");
                isLoaded = true;
            } catch (ClassNotFoundException e) {
                Gdx.app.error("Database", "Not find SQLite JDBC Driver! Check build.gradle.", e);
                throw new RuntimeException(e);
            }
        }
    }

    public static int getPlayerMaxLevel(String playerName) {
        if (playerName == null || playerName.isEmpty() || playerName.equals("Enter name")) {
            return 1;
        }

        loadDriverIfNeeded();
        new File("leaderBoard").mkdirs();

        String sqlInsert = "INSERT OR IGNORE INTO level_scores (playerName) VALUES (?);";
        String sqlSelect = "SELECT maxLevelUnlocked FROM level_scores WHERE playerName = ?";

        try (Connection conn = DriverManager.getConnection(URL_DATABASE)) {
            conn.setAutoCommit(true); // auto save change

            //Make new table if not have
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(SQL_CREATE_TABLE);
            }

            try (PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert)) {
                pstmtInsert.setString(1, playerName);
                pstmtInsert.executeUpdate();
            }

            //Get maxLevelUnlocked
            try (PreparedStatement pstmtSelect = conn.prepareStatement(sqlSelect)) {
                pstmtSelect.setString(1, playerName);
                ResultSet rs = pstmtSelect.executeQuery();
                if (rs.next()) {
                    return rs.getInt("maxLevelUnlocked");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Gdx.app.error("Database", "Get Max Level Error", e);
        }
        return 1;
    }

    public static void updatePlayerScore(String playerName, int levelNumber, double newScore, boolean Win) {

        if (playerName == null || playerName.isEmpty() || playerName.equals("Enter name") || levelNumber < 1 || levelNumber > 5) {
            return;
        }

        loadDriverIfNeeded();
        new File("leaderBoard").mkdirs();

        String sqlInsert = "INSERT OR IGNORE INTO level_scores (playerName) VALUES (?);";

        String sqlUpdateScore = "UPDATE level_scores SET level" + levelNumber + " = ? "
            + "WHERE playerName = ? AND level" + levelNumber + " < ?;";

        String sqlUpdateTotal = "UPDATE level_scores SET total = (level1 + level2 + level3 + level4 + level5) "
            + "WHERE playerName = ?;";

        // Update level unlocked
        String sqlUpdateMaxLevel = "UPDATE level_scores SET maxLevelUnlocked = ? "
            + "WHERE playerName = ? AND maxLevelUnlocked = ?";


        try (Connection conn = DriverManager.getConnection(URL_DATABASE)) {
            conn.setAutoCommit(false);

            //Make new table if not have
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(SQL_CREATE_TABLE);
            }

            //Insert player if not have
            try (PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert)) {
                pstmtInsert.setString(1, playerName);
                pstmtInsert.executeUpdate();
            }

            //Update score
            try (PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdateScore)) {
                pstmtUpdate.setDouble(1, newScore);
                pstmtUpdate.setString(2, playerName);
                pstmtUpdate.setDouble(3, newScore);
                pstmtUpdate.executeUpdate();
            }

            //Update total score
            try (PreparedStatement pstmtUpdateTotal = conn.prepareStatement(sqlUpdateTotal)) {
                pstmtUpdateTotal.setString(1, playerName);
                pstmtUpdateTotal.executeUpdate();
            }

            //Unlocked new level if win
            if (Win && levelNumber < 5) {
                int nextLevel = levelNumber + 1;
                int currentLevel = levelNumber;

                try (PreparedStatement pstmtUpdateMax = conn.prepareStatement(sqlUpdateMaxLevel)) {
                    pstmtUpdateMax.setInt(1, nextLevel);
                    pstmtUpdateMax.setString(2, playerName);
                    pstmtUpdateMax.setInt(3, currentLevel);
                    pstmtUpdateMax.executeUpdate();
                }
            }

            conn.commit(); //Save change

        } catch (SQLException e) {
            e.printStackTrace();
            Gdx.app.error("Database", "Update Score/Level Error", e);
        }
    }
}
