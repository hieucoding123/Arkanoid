package entity;

import java.util.ArrayList;

public class Level_game {
    private static int currentlevel = 0;
    private static ArrayList<BricksMap> levels = new ArrayList<>();

    public static void loadLevels() {
        if (!levels.isEmpty()) {
            levels.clear();
        }

        for (int i = 1; i <= 1; i++) {
            String mapPath = "/maps/map" + i + ".txt";
            levels.add(new BricksMap(mapPath));
        }
    }

    public static BricksMap getCurrentLevel() {
        if (currentlevel < levels.size() && currentlevel >= 0) {
            return levels.get(currentlevel);
        }
        return null;
    }

    public static void nextLevel() {
        if (hasNextLevel()) {
            currentlevel++;
        }
    }

    public static boolean hasNextLevel() {
        return currentlevel < levels.size() - 1;
    }
}
