package table;

import com.badlogic.gdx.Gdx;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class LevelDatabase {

    private static final String URL_DATABASE = "jdbc:sqlite:leaderBoard/level_scores.db";
    private static boolean isLoaded = false;


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

    public static void updatePlayerScore(String playerName, int levelNumber, double newScore) {

        if (playerName == null || playerName.isEmpty() || playerName.equals("Enter name") || levelNumber < 1 || levelNumber > 5) {
            return;
        }

        loadDriverIfNeeded();
        new File("leaderBoard").mkdirs();

        String sqlCreateTable = "CREATE TABLE IF NOT EXISTS level_scores ("
            + " playerName TEXT PRIMARY KEY NOT NULL,"
            + " level1 REAL DEFAULT 0,"
            + " level2 REAL DEFAULT 0,"
            + " level3 REAL DEFAULT 0,"
            + " level4 REAL DEFAULT 0,"
            + " level5 REAL DEFAULT 0"
            + ");";

        String sqlInsert = "INSERT OR IGNORE INTO level_scores (playerName) VALUES (?);";

        String sqlUpdate = "UPDATE level_scores SET level" + levelNumber + " = ? "
            + "WHERE playerName = ? AND level" + levelNumber + " < ?;";

        try (Connection conn = DriverManager.getConnection(URL_DATABASE)) {

            conn.setAutoCommit(false);

            //Make table if not have
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sqlCreateTable);
            }

            //Insert player if not have
            try (PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert)) {
                pstmtInsert.setString(1, playerName);
                pstmtInsert.executeUpdate();
            }

            //Update if have higher score
            try (PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate)) {
                pstmtUpdate.setDouble(1, newScore);
                pstmtUpdate.setString(2, playerName);
                pstmtUpdate.setDouble(3, newScore);
                pstmtUpdate.executeUpdate();
            }

            conn.commit(); //Save change

        } catch (SQLException e) {
            e.printStackTrace();
            Gdx.app.error("Database", "Input Error", e);
        }
    }
}
