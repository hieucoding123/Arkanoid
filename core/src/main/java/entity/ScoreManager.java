package entity;

import entity.object.brick.Brick;

/**
 * Scoring system for the game.
 */
public class ScoreManager {
    private double score;
    private int comboCount;
    private long lastHitTime;

    private static final int COMBO_TIMEOUT_MS = 2000;

    /**
     * Default constructor.
     */
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

    /**
     * Get current score.
     * @return current score
     */
    public double getScore() {
        return this.score;
    }

    /**
     * Set score with new value.
     * @param score new score instance
     */
    public void setScore(double score) {
        this.score = score;
    }

    /**
     * Scoring logic for explosion effect.
     * @param brick brick got exploded
     */
    public void addScore(Brick brick) {
        if (brick.getColor() == 1 || brick.getColor() == 2 || brick.getColor() == 31 ||  brick.getColor() == 41) {
            score += 100.0d;
        } else if (brick.getColor() == 0 ||  brick.getColor() == 32 || brick.getColor() == 33 ||  brick.getColor() == 42) {
            score += 200.0d;
        }
    }

    /**
     * Scoring logic for ball collision.
     * @param brick brick collided with ball
     */
    public void comboScore(Brick brick) {
        addScore(brick);

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

    /**
     * Score deduction when ball fell out of screen.
     */
    public void deduction() {
        score = Math.max(0, score - 500);
    }

    /**
     * Reset score to 0.
     */
    public void resetScore() {
        score = 0;
    }

    /**
     * Get current combo count.
     * @return current combo count
     */
    public int getComboCount() {
        return comboCount;
    }

    /**
     * Get last time ball hit brick.
     * @return last hit time in milliseconds
     */
    public long getLastHitTime() {
        return lastHitTime;
    }

    /**
     * Set current combo count.
     * @param cntCombo new combo count
     */
    public void setComboCount(int cntCombo) {
        this.comboCount = cntCombo;
    }

    /**
     * Set current last hit time.
     * @param timeLastHit new last hit time
     */
    public void setLastHitTime(long timeLastHit) {
        this.lastHitTime = timeLastHit;
    }
}
