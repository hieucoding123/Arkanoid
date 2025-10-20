package entity;

import entity.object.brick.Brick;

public class ScoreManager {
    private double score;
    private int comboCount;
    private long lastHitTime;

    private static final int COMBO_TIMEOUT_MS = 2000;

    public ScoreManager() {
        score = 0;
        comboCount = 0;
        lastHitTime = 0;
    }

//    public ScoreManager(double score, int comboCount, long lastHitTime) {
//        this.score = score;
//        this.comboCount = comboCount;
//        this.lastHitTime = lastHitTime;
//    }

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

    public void comboScore(Brick brick) {
        if (brick.getColor() == 1) {
            score += 100.0d;
        } else if (brick.getColor() == 0) {
            score += 200.0d;
        }

        long now = System.currentTimeMillis();
        if (now - lastHitTime < COMBO_TIMEOUT_MS) {
            comboCount++;
        } else {
            comboCount = 1;
        }
        lastHitTime = now;

        if (comboCount > 1) {
            score += 25.0d * Math.pow(comboCount, 2.0d);
        }
    }

    public void deduction() {
        score = Math.max(0, score - 500);
    }

    public void resetScore() {
        score = 0;
    }

    public void clearedLevel() {
        score += 1000.0d;
    }
}
