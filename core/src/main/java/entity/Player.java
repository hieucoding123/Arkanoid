package entity;

/**
 * Player data model containing player identity, score, and playtime information.
 *
 * <p>This class represents a player's session data including their name,
 * current score, and total time played. Used throughout the game to track
 * player progress and display statistics.</p>
 *
 * @see ScoreManager Score calculation and management
 * @see datahandler.LevelDataHandler Player data persistence
 */
public class Player {
    String name;
    double score;
    float timePlayed;

    public Player() {
        this.name = "";
        this.score = 0;
        this.timePlayed = 0;
    }
    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.timePlayed = 0;
    }

    public Player(String name, double score,  float timePlayed) {
        this.name = name;
        this.score = score;
        this.timePlayed = timePlayed;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public double getScore() {
        return score;
    }
    public void setScore(double score) {
        this.score = score;
    }

    public float getTimePlayed() {
        return this.timePlayed;
    }
    public void setTimePlayed(float timePlayed) {
        this.timePlayed = timePlayed;
    }

    /**
     * Replace by another player.
     * @param player other player
     */
    public void replace(Player player) {
        this.name = player.name;
        this.score = player.score;
    }
}
