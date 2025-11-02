package com.main.components;

import com.main.Game;
import entity.Effect.ShieldEffect;
import entity.object.GameObject;
import entity.object.Ball;
import entity.object.Paddle;
import entity.object.brick.Brick;
import entity.object.brick.BricksMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import org.mockito.MockedStatic;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CollisionManagerTest {

    private Ball mockBall1;
    private Ball mockBall2;
    private Paddle mockPaddle;
    private GameObject mockRect;
    private Brick mockBrick;
    private BricksMap mockBricksMap;
    private MockedStatic<Game> mockedGame;
    private MockedStatic<ShieldEffect> mockedShield;

    @Before
    public void setUp() {
        mockBall1 = mock(Ball.class);
        mockBall2 = mock(Ball.class);
        mockPaddle = mock(Paddle.class);
        mockRect = mock(GameObject.class);
        mockBrick = mock(Brick.class);
        mockBricksMap = mock(BricksMap.class);

        // Mock static classes
        mockedGame = mockStatic(Game.class);
        mockedShield = mockStatic(ShieldEffect.class);

        // Set static fields
        Game.padding_left_right = (int) 10f;
        Game.padding_top = (int) 10f;
        Game.SCREEN_WIDTH = (int) 800f;
    }

    @After
    public void tearDown() {
        if (mockedGame != null) {
            mockedGame.close();
        }
        if (mockedShield != null) {
            mockedShield.close();
        }
    }

    // ===== TEST checkCircleCircle =====

    @Test
    public void testCheckCircleCircle_Overlapping() {
        // Collision ball and ball
        when(mockBall1.getCenterX()).thenReturn(100f);
        when(mockBall1.getCenterY()).thenReturn(100f);
        when(mockBall1.getRadius()).thenReturn(10f);

        when(mockBall2.getCenterX()).thenReturn(105f);
        when(mockBall2.getCenterY()).thenReturn(105f);
        when(mockBall2.getRadius()).thenReturn(10f);

        boolean result = CollisionManager.checkCircleCircle(mockBall1, mockBall2);

        assertTrue("Balls should be overlapping", result);
    }

    @Test
    public void testCheckCircleCircle_NotOverlapping() {
        // No collision ball and ball
        when(mockBall1.getCenterX()).thenReturn(100f);
        when(mockBall1.getCenterY()).thenReturn(100f);
        when(mockBall1.getRadius()).thenReturn(10f);

        when(mockBall2.getCenterX()).thenReturn(150f);
        when(mockBall2.getCenterY()).thenReturn(150f);
        when(mockBall2.getRadius()).thenReturn(10f);

        boolean result = CollisionManager.checkCircleCircle(mockBall1, mockBall2);

        assertFalse("Balls should not be overlapping", result);
    }

    @Test
    public void testCheckCircleCircle_SamePosition() {
        // Two balls at same position
        when(mockBall1.getCenterX()).thenReturn(100f);
        when(mockBall1.getCenterY()).thenReturn(100f);
        when(mockBall1.getRadius()).thenReturn(10f);

        when(mockBall2.getCenterX()).thenReturn(100f);
        when(mockBall2.getCenterY()).thenReturn(100f);
        when(mockBall2.getRadius()).thenReturn(10f);

        boolean result = CollisionManager.checkCircleCircle(mockBall1, mockBall2);

        assertFalse("Balls at exact same position should return false", result);
    }

    @Test
    public void testCheckCircleCircle_EdgeCase_JustTouching() {
        // Two balls just touching at the edge
        when(mockBall1.getCenterX()).thenReturn(100f);
        when(mockBall1.getCenterY()).thenReturn(100f);
        when(mockBall1.getRadius()).thenReturn(10f);

        when(mockBall2.getCenterX()).thenReturn(120f);
        when(mockBall2.getCenterY()).thenReturn(100f);
        when(mockBall2.getRadius()).thenReturn(10f);

        boolean result = CollisionManager.checkCircleCircle(mockBall1, mockBall2);

        assertFalse("Balls just touching should not overlap (distance = sum of radii)", result);
    }

    // ===== TEST checkCircleRect =====

    @Test
    public void testCheckCircleRect_BallInsideRect() {
        // Ball in rectangle
        when(mockBall1.getCenterX()).thenReturn(50f);
        when(mockBall1.getCenterY()).thenReturn(50f);
        when(mockBall1.getRadius()).thenReturn(5f);

        when(mockRect.getX()).thenReturn(30f);
        when(mockRect.getY()).thenReturn(30f);
        when(mockRect.getWidth()).thenReturn(40f);
        when(mockRect.getHeight()).thenReturn(40f);

        boolean result = CollisionManager.checkCircleRect(mockBall1, mockRect);

        assertTrue("Ball should collide with rectangle", result);
    }

    @Test
    public void testCheckCircleRect_BallOutsideRect() {
        // No collision ball and rectangle
        when(mockBall1.getCenterX()).thenReturn(100f);
        when(mockBall1.getCenterY()).thenReturn(100f);
        when(mockBall1.getRadius()).thenReturn(5f);

        when(mockRect.getX()).thenReturn(30f);
        when(mockRect.getY()).thenReturn(30f);
        when(mockRect.getWidth()).thenReturn(40f);
        when(mockRect.getHeight()).thenReturn(40f);

        boolean result = CollisionManager.checkCircleRect(mockBall1, mockRect);

        assertFalse("Ball should not collide with rectangle", result);
    }

    @Test
    public void testCheckCircleRect_BallTouchingEdge() {
        // Collision ball and edge
        when(mockBall1.getCenterX()).thenReturn(75f);
        when(mockBall1.getCenterY()).thenReturn(50f);
        when(mockBall1.getRadius()).thenReturn(5f);

        when(mockRect.getX()).thenReturn(30f);
        when(mockRect.getY()).thenReturn(30f);
        when(mockRect.getWidth()).thenReturn(40f);
        when(mockRect.getHeight()).thenReturn(40f);

        boolean result = CollisionManager.checkCircleRect(mockBall1, mockRect);

        assertTrue("Ball touching edge should collide", result);
    }

    @Test
    public void testCheckCircleRect_BallTouchingCorner() {
        // Ball touching corner of rectangle
        when(mockBall1.getCenterX()).thenReturn(75f);
        when(mockBall1.getCenterY()).thenReturn(75f);
        when(mockBall1.getRadius()).thenReturn(7.1f); // Just enough to touch corner

        when(mockRect.getX()).thenReturn(30f);
        when(mockRect.getY()).thenReturn(30f);
        when(mockRect.getWidth()).thenReturn(40f);
        when(mockRect.getHeight()).thenReturn(40f);

        boolean result = CollisionManager.checkCircleRect(mockBall1, mockRect);

        assertTrue("Ball touching corner should collide", result);
    }

    // ===== TEST checkRectRect =====

    @Test
    public void testCheckRectRect_Overlapping() {
        // Overlapping rectangles
        GameObject rect1 = mock(GameObject.class);
        GameObject rect2 = mock(GameObject.class);

        when(rect1.getX()).thenReturn(10f);
        when(rect1.getY()).thenReturn(10f);
        when(rect1.getWidth()).thenReturn(30f);
        when(rect1.getHeight()).thenReturn(30f);

        when(rect2.getX()).thenReturn(20f);
        when(rect2.getY()).thenReturn(20f);
        when(rect2.getWidth()).thenReturn(30f);
        when(rect2.getHeight()).thenReturn(30f);

        boolean result = CollisionManager.checkRectRect(rect1, rect2);

        assertTrue("Rectangles should be overlapping", result);
    }

    @Test
    public void testCheckRectRect_NotOverlapping() {
        // No rectangles overlapping
        GameObject rect1 = mock(GameObject.class);
        GameObject rect2 = mock(GameObject.class);

        when(rect1.getX()).thenReturn(10f);
        when(rect1.getY()).thenReturn(10f);
        when(rect1.getWidth()).thenReturn(20f);
        when(rect1.getHeight()).thenReturn(20f);

        when(rect2.getX()).thenReturn(100f);
        when(rect2.getY()).thenReturn(100f);
        when(rect2.getWidth()).thenReturn(20f);
        when(rect2.getHeight()).thenReturn(20f);

        boolean result = CollisionManager.checkRectRect(rect1, rect2);

        assertFalse("Rectangles should not be overlapping", result);
    }

    @Test
    public void testCheckRectRect_TouchingEdges() {
        // Rectangles touching at edges
        GameObject rect1 = mock(GameObject.class);
        GameObject rect2 = mock(GameObject.class);

        when(rect1.getX()).thenReturn(10f);
        when(rect1.getY()).thenReturn(10f);
        when(rect1.getWidth()).thenReturn(20f);
        when(rect1.getHeight()).thenReturn(20f);

        when(rect2.getX()).thenReturn(30f);
        when(rect2.getY()).thenReturn(10f);
        when(rect2.getWidth()).thenReturn(20f);
        when(rect2.getHeight()).thenReturn(20f);

        boolean result = CollisionManager.checkRectRect(rect1, rect2);

        assertFalse("Rectangles just touching should not overlap", result);
    }

    // ===== TEST handleBallBoundaryCollision =====

    @Test
    public void testHandleBallBoundaryCollision_LeftWall() {
        // Collision with left wall
        when(mockBall1.getX()).thenReturn(5f);
        when(mockBall1.getY()).thenReturn(200f);
        when(mockBall1.getWidth()).thenReturn(10f);
        when(mockBall1.getHeight()).thenReturn(10f);

        CollisionManager.handleBallBoundaryCollision(mockBall1);

        verify(mockBall1).reverseX();
        verify(mockBall1).setX(anyFloat()); // Just verify setX was called
    }

    @Test
    public void testHandleBallBoundaryCollision_RightWall() {
        // Collision with right wall
        when(mockBall1.getX()).thenReturn(790f);
        when(mockBall1.getY()).thenReturn(200f);
        when(mockBall1.getWidth()).thenReturn(10f);
        when(mockBall1.getHeight()).thenReturn(10f);

        CollisionManager.handleBallBoundaryCollision(mockBall1);

        verify(mockBall1).reverseX();
        verify(mockBall1).setX(anyFloat()); // Just verify setX was called
    }

    @Test
    public void testHandleBallBoundaryCollision_TopWall() {
        // Collision with upper wall (ball.getY() + ball.getHeight() >= Game.padding_top)
        when(mockBall1.getX()).thenReturn(400f);
        when(mockBall1.getY()).thenReturn(1f); // Y + height = 1 + 10 = 11 >= 10 (triggers)
        when(mockBall1.getWidth()).thenReturn(10f);
        when(mockBall1.getHeight()).thenReturn(10f);

        CollisionManager.handleBallBoundaryCollision(mockBall1);

        verify(mockBall1).reverseY();
        verify(mockBall1).setY(anyFloat()); // Just verify setY was called
    }

    @Test
    public void testHandleBallBoundaryCollision_BottomWithoutShield() {
        // Ball drop without shield
        when(mockBall1.getX()).thenReturn(400f);
        when(mockBall1.getY()).thenReturn(-5f);
        when(mockBall1.getWidth()).thenReturn(10f);
        when(mockBall1.getHeight()).thenReturn(10f);

        // Mock ShieldEffect.isShield() to return false
        when(ShieldEffect.isShield()).thenReturn(false);

        CollisionManager.handleBallBoundaryCollision(mockBall1);

        verify(mockBall1).setDestroyed(true);
        verify(mockBall1, never()).reverseY();
    }

    @Test
    public void testHandleBallBoundaryCollision_BottomWithShield() {
        // Ball bounce with shield active
        when(mockBall1.getX()).thenReturn(400f);
        when(mockBall1.getY()).thenReturn(-5f);
        when(mockBall1.getWidth()).thenReturn(10f);
        when(mockBall1.getHeight()).thenReturn(10f);

        // Mock ShieldEffect.isShield() to return true
        when(ShieldEffect.isShield()).thenReturn(true);

        CollisionManager.handleBallBoundaryCollision(mockBall1);

        verify(mockBall1).reverseY();
        verify(mockBall1).setY(0.1f);
        verify(mockBall1, never()).setDestroyed(true);
    }

    // ===== TEST handleBallPaddleCollision (VOID METHOD) =====

    @Test
    public void testHandleBallPaddleCollision_NoCollision() {
        // No collision ball and paddle
        when(mockBall1.getCenterX()).thenReturn(100f);
        when(mockBall1.getCenterY()).thenReturn(100f);
        when(mockBall1.getRadius()).thenReturn(5f);

        when(mockPaddle.getX()).thenReturn(200f);
        when(mockPaddle.getY()).thenReturn(200f);
        when(mockPaddle.getWidth()).thenReturn(50f);
        when(mockPaddle.getHeight()).thenReturn(10f);

        CollisionManager.handleBallPaddleCollision(mockBall1, mockPaddle);

        // Verify no collision response methods were called
        verify(mockBall1, never()).setAngle(anyFloat());
        verify(mockBall1, never()).updateVelocity();
    }

    @Test
    public void testHandleBallPaddleCollision_NormalPaddle() {
        // Ball paddle collision (normal orientation)
        when(mockBall1.getCenterX()).thenReturn(100f);
        when(mockBall1.getCenterY()).thenReturn(105f);
        when(mockBall1.getRadius()).thenReturn(5f);
        when(mockBall1.getX()).thenReturn(95f);
        when(mockBall1.getWidth()).thenReturn(10f);
        when(mockBall1.getHeight()).thenReturn(10f);
        when(mockBall1.getDy()).thenReturn(-10f); // Moving up towards paddle

        when(mockPaddle.getX()).thenReturn(80f);
        when(mockPaddle.getY()).thenReturn(100f);
        when(mockPaddle.getWidth()).thenReturn(50f);
        when(mockPaddle.getHeight()).thenReturn(10f);
        when(mockPaddle.isFlipped()).thenReturn(false);

        CollisionManager.handleBallPaddleCollision(mockBall1, mockPaddle);

        verify(mockBall1).setAngle(anyFloat());
        verify(mockBall1).updateVelocity();
        verify(mockBall1).setY(110f); // paddle.getY() + paddle.getHeight()
    }

    @Test
    public void testHandleBallPaddleCollision_FlippedPaddle() {
        // Ball paddle collision (flipped orientation)
        when(mockBall1.getCenterX()).thenReturn(100f);
        when(mockBall1.getCenterY()).thenReturn(95f);
        when(mockBall1.getRadius()).thenReturn(5f);
        when(mockBall1.getX()).thenReturn(95f);
        when(mockBall1.getWidth()).thenReturn(10f);
        when(mockBall1.getHeight()).thenReturn(10f);
        when(mockBall1.getDy()).thenReturn(10f); // Moving down towards flipped paddle

        when(mockPaddle.getX()).thenReturn(80f);
        when(mockPaddle.getY()).thenReturn(100f);
        when(mockPaddle.getWidth()).thenReturn(50f);
        when(mockPaddle.getHeight()).thenReturn(10f);
        when(mockPaddle.isFlipped()).thenReturn(true);

        CollisionManager.handleBallPaddleCollision(mockBall1, mockPaddle);

        verify(mockBall1).setAngle(anyFloat());
        verify(mockBall1).updateVelocity();
        verify(mockBall1).setY(90f); // paddle.getY() - ball.getHeight()
    }

    @Test
    public void testHandleBallPaddleCollision_WrongDirection() {
        // Ball moving away from paddle - should not trigger response
        when(mockBall1.getCenterX()).thenReturn(100f);
        when(mockBall1.getCenterY()).thenReturn(105f);
        when(mockBall1.getRadius()).thenReturn(5f);
        when(mockBall1.getDy()).thenReturn(10f); // Moving down, away from paddle

        when(mockPaddle.getX()).thenReturn(80f);
        when(mockPaddle.getY()).thenReturn(100f);
        when(mockPaddle.getWidth()).thenReturn(50f);
        when(mockPaddle.getHeight()).thenReturn(10f);
        when(mockPaddle.isFlipped()).thenReturn(false);

        CollisionManager.handleBallPaddleCollision(mockBall1, mockPaddle);

        // No response should occur
        verify(mockBall1, never()).setAngle(anyFloat());
        verify(mockBall1, never()).updateVelocity();
    }

    // ===== TEST handleBallBrickHit =====

    @Test
    public void testHandleBallBrickHit_Hit() {
        // Ball and brick collision
        when(mockBall1.getCenterX()).thenReturn(100f);
        when(mockBall1.getCenterY()).thenReturn(100f);
        when(mockBall1.getRadius()).thenReturn(5f);
        when(mockBall1.getX()).thenReturn(95f);
        when(mockBall1.getY()).thenReturn(95f);
        when(mockBall1.getWidth()).thenReturn(10f);
        when(mockBall1.getHeight()).thenReturn(10f);
        when(mockBall1.isBig()).thenReturn(false);

        when(mockBrick.getX()).thenReturn(95f);
        when(mockBrick.getY()).thenReturn(95f);
        when(mockBrick.getWidth()).thenReturn(40f);
        when(mockBrick.getHeight()).thenReturn(20f);

        ArrayList<Brick> bricks = new ArrayList<>();
        bricks.add(mockBrick);
        when(mockBricksMap.getBricks()).thenReturn(bricks);

        Brick result = CollisionManager.handleBallBrickHit(mockBall1, mockBricksMap);

        assertNotNull("Should return the hit brick", result);
        assertEquals("Should return the correct brick", mockBrick, result);
        verify(mockBrick).takeHit();
    }

    @Test
    public void testHandleBallBrickHit_BigBallDestroysBrick() {
        // Big ball destroys unbreakable brick
        when(mockBall1.getCenterX()).thenReturn(100f);
        when(mockBall1.getCenterY()).thenReturn(100f);
        when(mockBall1.getRadius()).thenReturn(10f);
        when(mockBall1.getX()).thenReturn(90f);
        when(mockBall1.getY()).thenReturn(90f);
        when(mockBall1.getWidth()).thenReturn(20f);
        when(mockBall1.getHeight()).thenReturn(20f);
        when(mockBall1.isBig()).thenReturn(true);

        when(mockBrick.getX()).thenReturn(95f);
        when(mockBrick.getY()).thenReturn(95f);
        when(mockBrick.getWidth()).thenReturn(40f);
        when(mockBrick.getHeight()).thenReturn(20f);
        when(mockBrick.isUnbreak()).thenReturn(false);

        ArrayList<Brick> bricks = new ArrayList<>();
        bricks.add(mockBrick);
        when(mockBricksMap.getBricks()).thenReturn(bricks);

        Brick result = CollisionManager.handleBallBrickHit(mockBall1, mockBricksMap);

        assertNotNull("Should return the hit brick", result);
        verify(mockBrick).takeHit();
        verify(mockBrick).setHitPoints(0);
    }

    @Test
    public void testHandleBallBrickHit_NoHit() {
        // No collision ball and any bricks
        when(mockBall1.getCenterX()).thenReturn(100f);
        when(mockBall1.getCenterY()).thenReturn(100f);
        when(mockBall1.getRadius()).thenReturn(5f);

        when(mockBrick.getX()).thenReturn(200f);
        when(mockBrick.getY()).thenReturn(200f);
        when(mockBrick.getWidth()).thenReturn(40f);
        when(mockBrick.getHeight()).thenReturn(20f);

        ArrayList<Brick> bricks = new ArrayList<>();
        bricks.add(mockBrick);
        when(mockBricksMap.getBricks()).thenReturn(bricks);

        Brick result = CollisionManager.handleBallBrickHit(mockBall1, mockBricksMap);

        assertNull("Should return null when no brick hit", result);
        verify(mockBrick, never()).takeHit();
    }

    // ===== TEST handleBallBallCollision (VOID METHOD) =====

    @Test
    public void testHandleBallBallCollision_NoCollision() {
        // No collision ball-ball
        when(mockBall1.getCenterX()).thenReturn(100f);
        when(mockBall1.getCenterY()).thenReturn(100f);
        when(mockBall1.getRadius()).thenReturn(10f);

        when(mockBall2.getCenterX()).thenReturn(200f);
        when(mockBall2.getCenterY()).thenReturn(200f);
        when(mockBall2.getRadius()).thenReturn(10f);

        CollisionManager.handleBallBallCollision(mockBall1, mockBall2);

        // No response should occur
        verify(mockBall1, never()).setVelocityAndUpdateAngle(anyFloat(), anyFloat());
        verify(mockBall2, never()).setVelocityAndUpdateAngle(anyFloat(), anyFloat());
    }

    @Test
    public void testHandleBallBallCollision_WithCollision() {
        // Ball-ball collision
        when(mockBall1.getCenterX()).thenReturn(100f);
        when(mockBall1.getCenterY()).thenReturn(100f);
        when(mockBall1.getRadius()).thenReturn(10f);
        when(mockBall1.getDx()).thenReturn(5f);
        when(mockBall1.getDy()).thenReturn(0f);
        when(mockBall1.getSpeed()).thenReturn(5f);
        when(mockBall1.getX()).thenReturn(90f);
        when(mockBall1.getY()).thenReturn(90f);

        when(mockBall2.getCenterX()).thenReturn(115f);
        when(mockBall2.getCenterY()).thenReturn(100f);
        when(mockBall2.getRadius()).thenReturn(10f);
        when(mockBall2.getDx()).thenReturn(-5f);
        when(mockBall2.getDy()).thenReturn(0f);
        when(mockBall2.getSpeed()).thenReturn(5f);
        when(mockBall2.getX()).thenReturn(105f);
        when(mockBall2.getY()).thenReturn(90f);

        CollisionManager.handleBallBallCollision(mockBall1, mockBall2);

        verify(mockBall1).setVelocityAndUpdateAngle(anyFloat(), anyFloat());
        verify(mockBall2).setVelocityAndUpdateAngle(anyFloat(), anyFloat());
        verify(mockBall1).setSpeed(5f);
        verify(mockBall2).setSpeed(5f);
        verify(mockBall1).updateVelocity();
        verify(mockBall2).updateVelocity();
    }

    @Test
    public void testHandleBallBallCollision_MovingApart() {
        // Balls moving apart (dot product < 0)
        when(mockBall1.getCenterX()).thenReturn(100f);
        when(mockBall1.getCenterY()).thenReturn(100f);
        when(mockBall1.getRadius()).thenReturn(10f);
        when(mockBall1.getDx()).thenReturn(-5f);
        when(mockBall1.getDy()).thenReturn(0f);

        when(mockBall2.getCenterX()).thenReturn(115f);
        when(mockBall2.getCenterY()).thenReturn(100f);
        when(mockBall2.getRadius()).thenReturn(10f);
        when(mockBall2.getDx()).thenReturn(5f);
        when(mockBall2.getDy()).thenReturn(0f);

        CollisionManager.handleBallBallCollision(mockBall1, mockBall2);

        // No velocity change should occur
        verify(mockBall1, never()).setVelocityAndUpdateAngle(anyFloat(), anyFloat());
        verify(mockBall2, never()).setVelocityAndUpdateAngle(anyFloat(), anyFloat());
    }

    @Test
    public void testHandleBallBallCollision_PositionUpdate() {
        // Test that balls are separated after collision
        when(mockBall1.getCenterX()).thenReturn(100f);
        when(mockBall1.getCenterY()).thenReturn(100f);
        when(mockBall1.getRadius()).thenReturn(10f);
        when(mockBall1.getDx()).thenReturn(5f);
        when(mockBall1.getDy()).thenReturn(0f);
        when(mockBall1.getSpeed()).thenReturn(5f);
        when(mockBall1.getX()).thenReturn(90f);
        when(mockBall1.getY()).thenReturn(90f);

        when(mockBall2.getCenterX()).thenReturn(115f);
        when(mockBall2.getCenterY()).thenReturn(100f);
        when(mockBall2.getRadius()).thenReturn(10f);
        when(mockBall2.getDx()).thenReturn(-5f);
        when(mockBall2.getDy()).thenReturn(0f);
        when(mockBall2.getSpeed()).thenReturn(5f);
        when(mockBall2.getX()).thenReturn(105f);
        when(mockBall2.getY()).thenReturn(90f);

        CollisionManager.handleBallBallCollision(mockBall1, mockBall2);

        // Verify positions were updated to prevent sticking
        verify(mockBall1, atLeastOnce()).setX(anyFloat());
        verify(mockBall1, atLeastOnce()).setY(anyFloat());
        verify(mockBall2, atLeastOnce()).setX(anyFloat());
        verify(mockBall2, atLeastOnce()).setY(anyFloat());
    }
}
