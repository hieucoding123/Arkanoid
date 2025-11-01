package table;

import com.badlogic.gdx.Gdx;
import java.io.File;


/**
 * Handles the loading of the SQLite JDBC driver.
 * This class ensures that the driver is loaded only once.
 */
public class DataHandler {
    /**
     * Flag to track whether the SQLite JDBC driver has been successfully loaded.
     */
    private static boolean isLoaded = false;
    /**
     * Checks if the SQLite JDBC driver is loaded.
     *
     * @return true if the driver is loaded, false otherwise.
     */
    public static boolean isLoaded() {
        return isLoaded;
    }

    /**
     * Sets the loaded status of the SQLite JDBC driver.
     *
     * @param loaded The new loaded status.
     */
    public static void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }

    /**
     * Loads the SQLite JDBC driver ("org.sqlite.JDBC") if it has not been loaded already.
     * If the driver is not found, it logs an error using Gdx.app.error and
     * throws a RuntimeException.
     *
     * @throws RuntimeException if the SQLite JDBC driver class cannot be found.
     */
    public static void loadDriverIfNeeded() {
        if (isLoaded()) {
            return;
        }
        else {
            try {
                // Attempts to load the SQLite JDBC driver class
                Class.forName("org.sqlite.JDBC");
                setLoaded(true);

                new File("leaderBoard").mkdirs();
            } catch (ClassNotFoundException e) {
                // Logs an error if the driver is not found in the classpath
                Gdx.app.error("Database", "Not find SQLite JDBC Driver! Check build.gradle.", e);
                throw new RuntimeException(e);
            }
        }
    }

}
