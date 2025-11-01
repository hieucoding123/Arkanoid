package com.main.components;

import com.main.Game;
import entity.Effect.ShieldEffect;
import entity.GameObject; // Make sure to import the base GameObject
import entity.object.Ball;
import entity.object.Paddle;
import entity.object.brick.Brick;
import entity.object.brick.BricksMap;

public class CollisionManager {

    /**
     * Checks if two circles (Balls) are overlapping.
     */
    public static boolean checkCircleCircle(Ball ball1, Ball ball2) {
        float dx = ball2.getCenterX() - ball1.getCenterX();
        float dy = ball2.getCenterY() - ball1.getCenterY();
        float distanceSq = dx * dx + dy * dy;

        float sumRadii = ball1.getRadius() + ball2.getRadius();
        float sumRadiiSq = sumRadii * sumRadii;

        // Check for overlap and also ensure they aren't in the exact same spot (distanceSq > 0.001f)
        return (distanceSq < sumRadiiSq && distanceSq > 0.001f);
    }

    /**
     * Checks if a circle (Ball) and a rectangle (GameObject) are overlapping.
     */
    public static boolean checkCircleRect(Ball ball, GameObject rect) {
        float testX = ball.getCenterX();
        float testY = ball.getCenterY();

        if (ball.getCenterX() < rect.getX()) {
            testX = rect.getX();
        } else if (ball.getCenterX() > rect.getX() + rect.getWidth()) {
            testX = rect.getX() + rect.getWidth();
        }

        if (ball.getCenterY() < rect.getY()) {
            testY = rect.getY();
        } else if (ball.getCenterY() > rect.getY() + rect.getHeight()) {
            testY = rect.getY() + rect.getHeight();
        }

        float distX = ball.getCenterX() - testX;
        float distY = ball.getCenterY() - testY;
        float distanceSq = (distX * distX) + (distY * distY);

        return distanceSq <= (ball.getRadius() * ball.getRadius());
    }

    /**
     * Checks if two Axis-Aligned Bounding Boxes (Rectangles) are overlapping.
     */
    public static boolean checkRectRect(GameObject rect1, GameObject rect2) {
        return rect1.getX() < rect2.getX() + rect2.getWidth() &&
            rect1.getX() + rect1.getWidth() > rect2.getX() &&
            rect1.getY() < rect2.getY() + rect2.getHeight() &&
            rect1.getY() + rect1.getHeight() > rect2.getY();
    }



    public static void handleBallBoundaryCollision(Ball ball) {
        // Left wall
        if (ball.getX() <= Game.padding_left_right) {
            ball.reverseX();
            ball.setX(Game.padding_left_right + 0.1f); // Reposition
            Game.playSfx(Game.sfx_touchpaddle, 1.2f);
        }
        // Right wall
        else if (ball.getX() + ball.getWidth() >= Game.SCREEN_WIDTH - Game.padding_left_right) {
            ball.reverseX();
            ball.setX(Game.SCREEN_WIDTH - Game.padding_left_right - ball.getWidth() - 0.1f); // Reposition
            Game.playSfx(Game.sfx_touchpaddle, 1.2f);
        }

        // Top wall
        if (ball.getY() + ball.getHeight() >= Game.padding_top) {
            ball.reverseY();
            ball.setY(Game.padding_top - ball.getHeight() - 0.1f); // Reposition
            Game.playSfx(Game.sfx_touchpaddle, 1.2f);
        }

        // Bottom boundary
        else if (ball.getY() <= 0) {
            if (ShieldEffect.isShield()) {
                ShieldEffect.setShield();
                ball.reverseY();
                ball.setY(0.1f); // Reposition
                Game.playSfx(Game.sfx_touchpaddle, 1.2f);
            } else {
                ball.setDestroyed(true); // drop out of screen
            }
        }
    }

    /**
     * Handles the physics response for a ball-paddle collision.
     */
    public static boolean handleBallPaddleCollision(Ball ball, Paddle paddle) {
        if (!checkCircleRect(ball, paddle)) {
            return false;
        }

        //Respond
        if (paddle.isFlipped()) {
            if (ball.getDy() > 0) {
                float paddleCenter = paddle.getX() + paddle.getWidth() / 2f;
                float ballCenter = ball.getX() + ball.getWidth() / 2f;
                float impactPoint = (ballCenter - paddleCenter) / (paddle.getWidth() / 2f);
                impactPoint = Math.max(-1.0f, Math.min(1.0f, impactPoint));
                float newAngle = -((float)Math.PI / 2 - impactPoint * (float)Math.PI / 3f);
                ball.setAngle(newAngle);
                ball.updateVelocity();
                ball.setY(paddle.getY() - ball.getHeight());
                Game.playSfx(Game.sfx_touchpaddle, 1.2f);
                return true;
            }
        } else {
            if (ball.getDy() < 0) {
                float paddleCenter = paddle.getX() + paddle.getWidth() / 2f;
                float ballCenter = ball.getX() + ball.getWidth() / 2f;
                float impactPoint = (ballCenter - paddleCenter) / (paddle.getWidth() / 2f);
                impactPoint = Math.max(-1.0f, Math.min(1.0f, impactPoint));
                float newAngle = (float) (Math.PI / 2 - impactPoint * (float) Math.PI / 3f);
                ball.setAngle(newAngle);
                ball.updateVelocity();
                ball.setY(paddle.getY() + paddle.getHeight());
                Game.playSfx(Game.sfx_touchpaddle, 1.2f);
                return true;
            }
        }
        return false;
    }

    /**
     * Handles the response for a ball-brick collision.
     */
    public static Brick handleBallBrickHit(Ball ball, BricksMap currentMap) {
        for (Brick brick : currentMap.getBricks()) {
            if (checkCircleRect(ball, brick)) {

                //Respond
                brick.takeHit();
                if (ball.isBig() && !brick.isUnbreak()) brick.setHitPoints(0);

                float ballCenterX = ball.getCenterX();
                float ballCenterY = ball.getCenterY();
                float brickCenterX = brick.getX() + brick.getWidth() / 2f;
                float brickCenterY = brick.getY() + brick.getHeight() / 2f;
                float diffX = ballCenterX - brickCenterX;
                float diffY = ballCenterY - brickCenterY;

                float penetrationX = (ball.getRadius() + brick.getWidth() / 2f) - Math.abs(diffX);
                float penetrationY = (ball.getRadius() + brick.getHeight() / 2f) - Math.abs(diffY);

                if (penetrationX < penetrationY) {
                    ball.reverseX();
                    if (diffX > 0) {
                        ball.setX(brick.getX() + brick.getWidth() + 0.1f);
                    } else {
                        ball.setX(brick.getX() - ball.getWidth() - 0.1f);
                    }
                } else {
                    ball.reverseY();
                    if (diffY > 0) {
                        ball.setY(brick.getY() + brick.getHeight() + 0.1f);
                    } else {
                        ball.setY(brick.getY() - ball.getHeight() - 0.1f);
                    }
                }
                return brick;
            }
        }
        return null;
    }

    /**
     * Handles the bounce physics response for a ball-ball collision using checkCircleCircle.
     */
    public static boolean handleBallBallCollision(Ball ball1, Ball ball2) {
        // detection
        if (!checkCircleCircle(ball1, ball2)) {
            return false;
        }

        // Respond

        // calc physics vars
        float dx = ball2.getCenterX() - ball1.getCenterX();
        float dy = ball2.getCenterY() - ball1.getCenterY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        float normalX = dx / distance;
        float normalY = dy / distance;
        float tangentX = -normalY;
        float tangentY = normalX;

        float relVelX = ball1.getDx() - ball2.getDx();
        float relVelY = ball1.getDy() - ball2.getDy();
        float dotProduct = relVelX * normalX + relVelY * normalY;

        // Out of collision
        if (dotProduct < 0) {
            return false;
        }

        // Store speed for recover
        float speed1 = ball1.getSpeed();
        float speed2 = ball2.getSpeed();

        // Bounce calculation
        float v1_normal_scalar = ball1.getDx() * normalX + ball1.getDy() * normalY;
        float v1_tangent_scalar = ball1.getDx() * tangentX + ball1.getDy() * tangentY;
        float v2_normal_scalar = ball2.getDx() * normalX + ball2.getDy() * normalY;
        float v2_tangent_scalar = ball2.getDx() * tangentX + ball2.getDy() * tangentY;

        float new_v1_normal_scalar = v2_normal_scalar;
        float new_v2_normal_scalar = v1_normal_scalar;

        float new_v1_dx = (new_v1_normal_scalar * normalX) + (v1_tangent_scalar * tangentX);
        float new_v1_dy = (new_v1_normal_scalar * normalY) + (v1_tangent_scalar * tangentY);
        float new_v2_dx = (new_v2_normal_scalar * normalX) + (v2_tangent_scalar * tangentX);
        float new_v2_dy = (new_v2_normal_scalar * normalY) + (v2_tangent_scalar * tangentY);

        // Apply velocity
        ball1.setVelocityAndUpdateAngle(new_v1_dx, new_v1_dy);
        ball2.setVelocityAndUpdateAngle(new_v2_dx, new_v2_dy);

        // Apply original speed
        ball1.setSpeed(speed1);
        ball1.updateVelocity();
        ball2.setSpeed(speed2);
        ball2.updateVelocity();

        // Don't stick please
        float sumRadii = ball1.getRadius() + ball2.getRadius();
        float overlap = sumRadii - distance;
        float push = overlap * 0.5f + 0.1f;

        ball1.setX(ball1.getX() - push * normalX);
        ball1.setY(ball1.getY() - push * normalY);
        ball2.setX(ball2.getX() + push * normalX);
        ball2.setY(ball2.getY() + push * normalY);

        return true;
    }
}
