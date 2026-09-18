package no.uib.inf102.battleship.model.shot;

/**
 * Represents the possible results of shooting at a position on a board.
 */
public enum ShotResult {

    /**
     * The shot did not hit a ship.
     */
    MISS,

    /**
     * The shot hit a ship, but the ship is not completely destroyed.
     */
    HIT,

    /**
     * The shot hit the final remaining position of a ship,
     * causing the ship to sink.
     */
    SUNK,

    /**
     * The position has already been shot at.
     */
    ALREADY_SHOT
}