package no.uib.inf102.battleship.model;

import no.uib.inf102.battleship.model.ship.Direction;
import no.uib.inf102.battleship.model.ship.Ship;
import no.uib.inf102.battleship.model.shot.ShotResult;
import no.uib.inf102.grid.CellPosition;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Battleship game board.
 *
 * The board keeps track of placed ships and the result of shots
 * fired at each position.
 */
public class BattleShipBoard {

    public static final int SIZE = 10;
    public static final int NUMBER_OF_SHIPS = 3;

    private final List<Ship> ships;
    private final ShotResult[][] shots;

    /**
     * Creates an empty Battleship board.
     *
     * No ships have been placed and no positions have been shot at.
     */
    public BattleShipBoard() {
        ships = new ArrayList<>();
        shots = new ShotResult[SIZE][SIZE];
    }

    /**
     * Places a ship on the board.
     *
     * A ship cannot be placed if the board already contains the maximum
     * number of ships, if any part of the ship is outside the board,
     * or if the ship overlaps another ship.
     *
     * @param startPosition the starting position of the ship
     * @param direction the direction in which the ship extends
     * @return true if the ship was successfully placed, otherwise false
     */
    public boolean placeShip(CellPosition startPosition, Direction direction) {
        // Implement ship placement.
        if (ships.size() >= NUMBER_OF_SHIPS) {
            return false;
        }
        Ship newShip = new Ship(startPosition, direction);
        if (isValidPlacement(newShip)) {
            ships.add(newShip); 
            return true; 
        }
        //ships.add(newShip);

        return false;
        //return false;
    }

    /**
     * Shoots at a position on the board.
     *
     * The result should be:
     * - ALREADY_SHOT if the position has already been shot at
     * - HIT if a ship is hit but not sunk
     * - SUNK if the shot sinks a ship
     * - MISS if there is no ship at the position
     *
     * @param position the position to shoot at
     * @return the result of the shot
     * @throws IllegalArgumentException if the position is outside the board
     */
    public ShotResult shoot(CellPosition position) {
        // Implement shooting.
        if (!isInsideBoard(position)) {
            throw new IllegalArgumentException("Shot is outside board"); 
        }

        if (hasBeenShot(position)) {
            shots[position.row][position.col] = ShotResult.ALREADY_SHOT; 
            return ShotResult.ALREADY_SHOT; 
        }
        else {
            for (Ship ship : ships) {
                if (ship.hit(position)) {
                    if (ship.isSunk()) {
                        shots[position.row][position.col] = ShotResult.SUNK; 
                        return ShotResult.SUNK;
                    }
                    shots[position.row][position.col] = ShotResult.HIT; 
                    return ShotResult.HIT; 
                }
            }
            shots[position.row][position.col] = ShotResult.MISS; 
            return ShotResult.MISS; 
        }
    }

    /**
     * Checks whether all ships on the board have been sunk.
     *
     * The board is only considered to have all ships sunk when all
     * required ships have been placed and every ship has been sunk.
     *
     * @return true if all ships have been placed and sunk,
     *         otherwise false
     */
    public boolean allShipsSunk() {
        // Check whether all ships have been sunk.
        for (Ship ship : ships) {
            if (!ship.isSunk()) {
                return false; 
            }
        }
        return true;
    }

    /**
     * Gets all ships currently placed on the board.
     *
     * A copy of the list is returned so that the internal list
     * cannot be modified directly.
     *
     * @return a list containing the placed ships
     */
    public List<Ship> getShips() {
        return new ArrayList<>(ships);
    }

    /**
     * Checks whether a position has already been shot at.
     *
     * @param position the position to check
     * @return true if the position has been shot at, otherwise false
     */
    public boolean hasBeenShot(CellPosition position) {
        return shots[position.row()][position.col()] != null;
    }

    /**
     * Gets the result of a previous shot at a position.
     *
     * @param position the position to check
     * @return the shot result, or null if the position has not been shot at
     */
    public ShotResult getShotResult(CellPosition position) {
        return shots[position.row()][position.col()];
    }

    /**
     * Checks whether a position is inside the board.
     *
     * @param position the position to check
     * @return true if the position is inside the board, otherwise false
     */
    public boolean isInsideBoard(CellPosition position) {
        return position.row() >= 0
                && position.row() < SIZE
                && position.col() >= 0
                && position.col() < SIZE;
    }

    /**
     * Checks whether a ship can legally be placed on the board.
     *
     * A valid placement must keep every part of the ship inside the board
     * and must not overlap an existing ship.
     *
     * @param ship the ship to check
     * @return true if the placement is valid, otherwise false
     */
    private boolean isValidPlacement(Ship ship) {
        for (CellPosition position : ship.getPositions()) {

            if (!isInsideBoard(position)) {
                return false;
            }

            for (Ship existingShip : ships) {
                if (existingShip.contains(position)) {
                    return false;
                }
            }
        }

        return true;
    }
}