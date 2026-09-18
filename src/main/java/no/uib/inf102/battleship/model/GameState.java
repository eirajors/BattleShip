package no.uib.inf102.battleship.model;

/**
 * Represents the current state of a Battleship game.
 */
public enum GameState {

    /**
     * Players are placing their ships on the board.
     */
    PLACING_SHIPS,

    /**
     * It is the human player's turn to shoot.
     */
    PLAYER_TURN,

    /**
     * It is the AI player's turn to shoot.
     */
    AI_TURN,

    /**
     * The game has ended because all ships belonging to one player
     * have been sunk.
     */
    GAME_OVER
}