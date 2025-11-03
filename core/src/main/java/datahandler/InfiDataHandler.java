package datahandler;

import com.badlogic.gdx.Gdx;
import jdk.internal.vm.annotation.Hidden;
import java.sql.*;
import java.util.ArrayList;

/**
 * Handle data from Infinite Mode.
 */
public class InfiDataHandler extends DataHandler {
    private static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS infinite_scores ("
        + " playerName TEXT PRIMARY KEY NOT NULL,"
        + " score INTEGER DEFAULT 0,"
        + " time REAL DEFAULT 0"
        + ");";


    public static void addScore(String pName, double score, double time) {
        if (pName == null || pName.isEmpty() || pName.equals("Enter name")) {
            pName = "Guest";
        }

        loadDriverIfNeeded();

        // Insert new row to table
        String sqlUpsert = "INSERT INTO infinite_scores (playerName, score, time) VALUES (?, ?, ?) "
            + "ON CONFLICT(playerName) DO UPDATE SET "
            + "  score = excluded.score, "
            + "  time = excluded.time "
            + "WHERE "
            + "  excluded.score > infinite_scores.score "
            + "  OR (excluded.score = infinite_scores.score AND excluded.time < infinite_scores.time);";

        try (Connection conn = DriverManager.getConnection(getUrlDatabase())) {
            conn.setAutoCommit(true);
            // Make new table
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(SQL_CREATE_TABLE);
            }

            // Insert new data
            try (PreparedStatement pstmt = conn.prepareStatement(sqlUpsert)) {
                pstmt.setString(1, pName);
                pstmt.setInt(2, (int) score);
                pstmt.setDouble(3, time);
                pstmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Gdx.app.error("Database", "Add Game Score Error", e);
        }
    }

    /**
     * Get data from table in database.
     * Query table to get data.
     * @return data
     */
    public static ArrayList<String> getLeaderboardData() {
        ArrayList<String> leaderboard = new ArrayList<>();

        loadDriverIfNeeded();

        // Select top20 highest of table
        String sqlSelect = "SELECT playerName, score, time FROM infinite_scores "
            + "ORDER BY score DESC, time ASC LIMIT 20";

        try (Connection conn = DriverManager.getConnection(getUrlDatabase());
             Statement stmt = conn.createStatement()) {

            stmt.execute(SQL_CREATE_TABLE);

            ResultSet rs = stmt.executeQuery(sqlSelect);

            while (rs.next()) {
                String name = rs.getString("playerName");
                int score = rs.getInt("score");
                double time = rs.getDouble("time");

                String entry = String.format("%s,%d,%.2f", name, score, time);
                leaderboard.add(entry);
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
            Gdx.app.error("Database", "Get Game Leaderboard Data Error", e);
        }
        return leaderboard;
    }

    @Hidden
    public static String getUrlDatabase() {
        return "jdbc:sqlite:leaderBoard/infinite_scores.db";
    }
}
