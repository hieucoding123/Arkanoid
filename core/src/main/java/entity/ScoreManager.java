package entity;

public class ScoreManager {
    private double score;

    public ScoreManager() {
        score = 0;
    }

    public ScoreManager(double score) {
        this.score = score;
    }

    public double getScore() {
        return this.score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void addScore() {
        score += 100.0d;
        System.out.println(score);
    }

    public void clearedLevel() {
        score += 1000.0d;
    }
}
