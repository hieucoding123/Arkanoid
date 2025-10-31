package table;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;

public class DataHandler {
//    private static final String URL_DATABASE = "jdbc:sqlite:leaderBoard/level_scores.db";
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
    public static ArrayList<String> getLeaderboardData() {
        return null;
    }

    public static boolean isLoaded() {
        return isLoaded;
    }

    public static void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }

    public static String getUrlDatabase() {
        return null;
    }

}
