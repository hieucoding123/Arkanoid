package entity;

import entity.object.brick.Brick;

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

    public void addScore(Brick brick) {
        if (brick.getColor() == 1) {
            score += 100.0d;
        } else if (brick.getColor() == 0) {
            score += 200.0d;
        }
    }

    public void deduction() {
        score = Math.max(0, score - 500);
    }


    public void clearedLevel() {
        score += 1000.0d;
    }
}
