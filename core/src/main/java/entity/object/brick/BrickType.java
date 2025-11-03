package entity.object.brick;

/**
 * Enumeration of brick types in Arkanoid with different durability and behaviors.
 *
 * <p>This enum defines the various types of bricks that can appear in the game.
 * Each type has unique properties that affect gameplay mechanics, such as how many
 * hits are required to destroy the brick or special behaviors when destroyed.</p>
 *
 * @see Brick The brick class that uses this enum
 * @see BricksMap Level map that defines brick layouts
 * @see entity.object.Ball Ball class that interacts with bricks
 */
public enum BrickType {
    /** Single-hit brick - destroyed in one hit (easiest) */
    T1HIT,

    /** Two-hit brick - requires 2 hits to destroy (medium difficulty) */
    T2HIT,

    /** Three-hit brick - requires 3 hits to destroy (hard) */
    T3HIT,

    /** Four-hit brick - requires 4 hits to destroy (very hard) */
    T4HIT,

    /** No-hit brick - decorative only, cannot be destroyed or interacted with */
    NOHIT,

    /** Explosive brick - triggers explosion that damages surrounding bricks */
    EXPLO,

    /** Unbreakable brick - permanent obstacle that cannot be destroyed */
    UNBREAK,
}
