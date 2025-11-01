package table;

import com.badlogic.gdx.Gdx;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class BaseLevelDataHandler extends DataHandler {

    public static int getPlayerMaxLevel(String playerName, String dbUrl, String createTableSql, String tableName) {
        if (playerName == null || playerName.isEmpty() || playerName.equals("Enter name")) {
            return 1;
        }

        loadDriverIfNeeded();

        String sqlInsert = "INSERT OR IGNORE INTO " + tableName + " (playerName) VALUES (?);";
        String sqlSelect = "SELECT maxLevelUnlocked FROM " + tableName + " WHERE playerName = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            conn.setAutoCommit(true);

            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createTableSql);
            }

            try (PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert)) {
                pstmtInsert.setString(1, playerName);
                pstmtInsert.executeUpdate();
            }

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

    public static void updatePlayerScore(String playerName, int levelNumber, double newScore, boolean Win, String dbUrl, String createTableSql, String tableName) {
        if (playerName == null || playerName.isEmpty() || playerName.equals("Enter name") || levelNumber < 1 || levelNumber > 5) {
            return;
        }

        loadDriverIfNeeded();

        String sqlInsert = "INSERT OR IGNORE INTO " + tableName + " (playerName) VALUES (?);";
        String sqlUpdateScore = "UPDATE " + tableName + " SET level" + levelNumber + " = ? "
            + "WHERE playerName = ? AND level" + levelNumber + " < ?;";
        String sqlUpdateTotal = "UPDATE " + tableName + " SET total = (level1 + level2 + level3 + level4 + level5) "
            + "WHERE playerName = ?;";
        String sqlUpdateMaxLevel = "UPDATE " + tableName + " SET maxLevelUnlocked = ? "
            + "WHERE playerName = ? AND maxLevelUnlocked = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            conn.setAutoCommit(false);

            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createTableSql);
            }

            try (PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert)) {
                pstmtInsert.setString(1, playerName);
                pstmtInsert.executeUpdate();
            }

            try (PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdateScore)) {
                pstmtUpdate.setDouble(1, newScore);
                pstmtUpdate.setString(2, playerName);
                pstmtUpdate.setDouble(3, newScore);
                pstmtUpdate.executeUpdate();
            }

            try (PreparedStatement pstmtUpdateTotal = conn.prepareStatement(sqlUpdateTotal)) {
                pstmtUpdateTotal.setString(1, playerName);
                pstmtUpdateTotal.executeUpdate();
            }

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
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            Gdx.app.error("Database", "Update Score/Level Error", e);
        }
    }

    public static double getPlayerTotalScore(String playerName, String dbUrl, String tableName) {
        if (playerName == null || playerName.isEmpty() || playerName.equals("Enter name")) {
            return 0;
        }
        loadDriverIfNeeded();
        String sqlSelect = "SELECT total FROM " + tableName + " WHERE playerName = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl);
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

    public static double getPlayerScoreForLevel(String playerName, int levelNumber, String dbUrl, String tableName) {
        if (playerName == null || playerName.isEmpty() || playerName.equals("Enter name") || levelNumber < 1 || levelNumber > 5) {
            return 0;
        }
        loadDriverIfNeeded();

        String levelColumn = "level" + levelNumber;
        String sqlSelect = "SELECT " + levelColumn + " FROM " + tableName + " WHERE playerName = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl);
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

    public static ArrayList<String> getLeaderboardData(String dbUrl, String createTableSql, String tableName) {
        ArrayList<String> leaderboard = new ArrayList<>();
        loadDriverIfNeeded();

        String sqlSelect = "SELECT playerName, maxLevelUnlocked, total FROM " + tableName + " ORDER BY total DESC LIMIT 20";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSql);
            ResultSet rs = stmt.executeQuery(sqlSelect);
            while (rs.next()) {
                String name = rs.getString("playerName");
                int maxLevel = rs.getInt("maxLevelUnlocked");
                double totalScore = rs.getDouble("total");
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
