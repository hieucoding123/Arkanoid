package table;

import com.badlogic.gdx.Gdx;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class InfiDataHandler extends DataHandler {
    private static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS infinite_scores ("
        + " playerName TEXT PRIMARY KEY NOT NULL,"
        + " score INTEGER DEFAULT 0,"
        + " time REAL DEFAULT 0"
        + ");";

    private static void loadDriverIfNeeded() {
        if (isLoaded()) return;

        try {
            Class.forName("org.sqlite.JDBC");
            setLoaded(true);
        } catch (ClassNotFoundException e) {
            Gdx.app.error("Database", "Not find SQLite JDBC Driver! Check build.gradle.", e);
            throw new RuntimeException(e);
        }
    }

    public static void addScore(String pName, double score, double time) {
        loadDriverIfNeeded();
        new File("leaderBoard").mkdirs();

//        String sqlUpsert = "INSERT INTO infinite_scores (playerName, score, time) VALUES (?, ?, ?);";
        String sqlUpsert = "INSERT INTO infinite_scores (playerName, score, time) VALUES (?, ?, ?) "
            + "ON CONFLICT(playerName) DO UPDATE SET " // Nếu playerName đã tồn tại...
            + "  score = excluded.score, "              // ...cập nhật điểm...
            + "  time = excluded.time "                 // ...và thời gian...
            + "WHERE " // ...NHƯNG CHỈ KHI...
            + "  excluded.score > infinite_scores.score " // ...điểm mới > điểm cũ
            + "  OR (excluded.score = infinite_scores.score AND excluded.time < infinite_scores.time);"; // ...hoặc điểm bằng, thời gian mới < thời gian cũ

        try (Connection conn = DriverManager.getConnection(getUrlDatabase())) {
            conn.setAutoCommit(true); // Tự động lưu cho mỗi lệnh

            // 1. Đảm bảo bảng đã được tạo
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(SQL_CREATE_TABLE);
            }

            // 2. Chèn dữ liệu mới
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
    public static ArrayList<String> getLeaderboardData() {
        ArrayList<String> leaderboard = new ArrayList<>();

        loadDriverIfNeeded();
        new File("leaderBoard").mkdirs();

        // Sắp xếp theo điểm (DESC - Giảm dần) và thời gian (ASC - Tăng dần)
        String sqlSelect = "SELECT playerName, score, time FROM infinite_scores "
            + "ORDER BY score DESC, time ASC LIMIT 20";

        try (Connection conn = DriverManager.getConnection(getUrlDatabase());
             Statement stmt = conn.createStatement()) {

            // Đảm bảo bảng tồn tại trước khi truy vấn
            stmt.execute(SQL_CREATE_TABLE);

            // Thực thi truy vấn
            ResultSet rs = stmt.executeQuery(sqlSelect);

            while (rs.next()) {
                String name = rs.getString("playerName");
                int score = rs.getInt("score");
                double time = rs.getDouble("time");

                // Định dạng chuỗi trả về, làm tròn thời gian 2 chữ số
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
}
