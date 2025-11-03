package com.main;

import com.main.gamemode.GameMode;

/**
 * Enumeration of all possible game states in Arkanoid.
 *
 * <p>This enum defines the finite state machine (FSM) for the game's UI flow.
 * Each state represents a distinct screen or mode that the game can be in.
 * The game transitions between these states based on user actions (button clicks,
 * game events, network events, etc.).</p>
 *
 * @see Game Main game class that manages state transitions
 * @see Menu.UserInterface Base class for menu states
 * @see GameMode Base class for gameplay states
 */
public enum GameState {
    /** Main menu - entry point of the game */
    MAIN_MENU,

    /** Versus mode submenu for local multiplayer options */
    VS_MENU,

    /** Mode selection screen (Single Player, Multiplayer, Network, etc.) */
    SELECT_MODE,

    /** Leaderboard/High scores display screen */
    LEADER_BOARD,

    /** Infinite/Endless mode - play until game over */
    INFI_MODE,

    /** Campaign levels mode - structured level progression */
    LEVELS_MODE,

    /** Local versus mode - 1v1 on same device */
    VS_MODE,

    /** Level selection screen for campaign mode */
    LEVELS_SELECTION,

    /** Campaign Level 1 - active gameplay */
    LEVEL1,

    /** Campaign Level 2 - active gameplay */
    LEVEL2,

    /** Campaign Level 3 - active gameplay */
    LEVEL3,

    /** Campaign Level 4 - active gameplay */
    LEVEL4,

    /** Campaign Level 5 - active gameplay */
    LEVEL5,

    /** Generic playing state - active gameplay */
    PLAYING,

    /** Settings menu - game configuration and options */
    SETTINGS,

    /** Network connection menu - host or join multiplayer game */
    NETWORK_CONNECTION_MENU,

    /** Network lobby - waiting room before online match starts */
    NETWORK_LOBBY,

    /** Network versus mode - online 1v1 gameplay */
    NETWORK_VS,

    /** Game over screen - displayed when player loses */
    GAME_OVER,

    /** Game summary screen - post-game statistics and results */
    GAME_SUMMARY
}
