package entity;

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
