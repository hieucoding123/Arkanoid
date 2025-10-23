package entity;

public class Player {
    String name;
    double score;

    public Player() {
        this.name = "";
        this.score = 0;
    }
    public Player(String name) {
        this.name = name;
        this.score = 0;
    }

    public Player(String name, double score) {
        this.name = name;
        this.score = score;
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

    /**
     * Compare with another player.
     * @param other other Player
     * @return true if have more score than another Player
     */
    public boolean less(Player other) {
        if (this.score != other.score) {
            return this.score < other.score;
        }
        if (!this.name.equals(other.name)) {
            return this.name.compareTo(other.name) < 0;
        }
        return true;
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
