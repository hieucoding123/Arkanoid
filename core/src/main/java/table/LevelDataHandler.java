package table;

import com.badlogic.gdx.Gdx;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class LevelDataHandler extends DataHandler {
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
        if (isLoaded()) {
            return;
        }
        else {
            try {
                Class.forName("org.sqlite.JDBC");
                setLoaded(true);
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

        try (Connection conn = DriverManager.getConnection(getUrlDatabase())) {
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


        try (Connection conn = DriverManager.getConnection(getUrlDatabase())) {
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

    public static double getPlayerTotalScore(String playerName) {
        if (playerName == null || playerName.isEmpty() || playerName.equals("Enter name")) {
            return 0;
        }
        loadDriverIfNeeded();
        String sqlSelect = "SELECT total FROM level_scores WHERE playerName = ?";
        try (Connection conn = DriverManager.getConnection(URL_DATABASE);
             PreparedStatement pstmtSelect = conn.prepareStatement(sqlSelect)) {
            pstmtSelect.setString(1, playerName);
            ResultSet rs = pstmtSelect.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Gdx.app.error("Database", "Get Total Score Error", e);
        }
        return 0;
    }

    public static double getPlayerScoreForLevel(String playerName, int levelNumber) {
        if (playerName == null || playerName.isEmpty() || playerName.equals("Enter name") || levelNumber < 1 || levelNumber > 5) {
            return 0;
        }
        loadDriverIfNeeded();

        String levelColumn = "level" + levelNumber;
        String sqlSelect = "SELECT " + levelColumn + " FROM level_scores WHERE playerName = ?";

        try (Connection conn = DriverManager.getConnection(URL_DATABASE);
             PreparedStatement pstmtSelect = conn.prepareStatement(sqlSelect)) {

            pstmtSelect.setString(1, playerName);
            ResultSet rs = pstmtSelect.executeQuery();

            if (rs.next()) {
                return rs.getDouble(levelColumn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Gdx.app.error("Database", "Get Score for Level " + levelNumber + " Error", e);
        }
        return 0;
    }

    public static ArrayList<String> getLeaderboardData() {
        ArrayList<String> leaderboard = new ArrayList<>();

        loadDriverIfNeeded();
        new File("leaderBoard").mkdirs();

        String sqlSelect = "SELECT playerName, maxLevelUnlocked, total FROM level_scores ORDER BY total DESC LIMIT 20";

        try (Connection conn = DriverManager.getConnection(getUrlDatabase());
             Statement stmt = conn.createStatement()) {

            stmt.execute(SQL_CREATE_TABLE);

            ResultSet rs = stmt.executeQuery(sqlSelect);

            while (rs.next()) {
                String name = rs.getString("playerName");
                int maxLevel = rs.getInt("maxLevelUnlocked");
                double totalScore = rs.getDouble("total");
                //Format save
                String entry = name + "," + maxLevel + "," + totalScore;
                leaderboard.add(entry);
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
            Gdx.app.error("Database", "Get Leaderboard Data Error", e);
        }
        return leaderboard;
    }
}
