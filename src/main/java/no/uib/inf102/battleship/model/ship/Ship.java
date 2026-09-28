package no.uib.inf102.battleship.model.ship;

import no.uib.inf102.grid.CellPosition;
import no.uib.inf102.grid.GridDirection;

import java.util.ArrayList;
import java.util.List;

public class Ship {

    private static final int SIZE = 3;

    private final CellPosition startPosition;
    private final Direction direction;
    private final List<CellPosition> positions;
    private final boolean[] hits;

    /**
     * Creates a new ship starting at the given position and extending
     * in the given direction.
     *
     * @param startPosition the first position occupied by the ship
     * @param direction the direction in which the ship extends
     */
    public Ship(CellPosition startPosition, Direction direction) {
        this.startPosition = startPosition;
        this.direction = direction;
        this.positions = calculatePositions();
        this.hits = new boolean[SIZE];
    }

    /**
     * Gets the position where the ship starts.
     *
     * @return the starting position of the ship
     */
    public CellPosition getStartPosition() {
        return startPosition;
    }

    /**
     * Gets the direction in which the ship extends.
     *
     * @return the direction of the ship
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Gets all positions occupied by the ship.
     *
     * @return a list containing the ship's positions
     */
    public List<CellPosition> getPositions() {
        return positions;
    }

    /**
     * Checks whether the ship occupies the given position.
     *
     * @param position the position to check
     * @return true if the ship occupies the position, otherwise false
     */
    public boolean contains(CellPosition position) {
        return positions.contains(position);
    }

    /**
     * Registers a hit on the ship.
     *
     * A position can only be hit once. A hit is only registered if
     * the given position belongs to this ship.
     *
     * @param position the position that was hit
     * @return true if a new hit was registered, otherwise false
     */
    public boolean hit(CellPosition position) {
        // Register the hit if the position belongs to the ship
        // and has not already been hit.

        if (positions.contains(position)) {
            int hitPosition = findIndexHit(position);

            if (!hits[hitPosition]) {
                hits[hitPosition] = true; 
                return true;
            }
        }
        return false;
    }

    /**
     * Given the position, finds the index of the hit in the hits array
     * 
     * @param position the position to check
     * @return the index of the position in hits
     */
    private int findIndexHit(CellPosition position) {
        int indexShip = 0; 

        for (int i = 0; i < SIZE; i++ ){
            if (positions.get(i).equals(position)) {
                indexShip = i; 
                break; 
            }
        }

        return indexShip; 
    }

    /**
     * Checks whether the ship has been completely hit.
     *
     * @return true if every position of the ship has been hit,
     *         otherwise false
     */
    public boolean isSunk() {
        //  Check whether all positions of the ship have been hit.
        for (int i = 0; i < hits.length; i++) {
            if (hits[i] == false) {
                return false; 
            }
        }
        return true;
    }

    /**
     * Calculates the positions occupied by the ship based on its
     * starting position and direction.
     *
     * @return a list containing all positions occupied by the ship
     */
    private List<CellPosition> calculatePositions() {
        List<CellPosition> positions = new ArrayList<>();

        GridDirection gridDirection;

        if (direction == Direction.HORIZONTAL) {
            gridDirection = GridDirection.EAST;
        } else {
            gridDirection = GridDirection.SOUTH;
        }

        CellPosition current = startPosition;

        for (int i = 0; i < SIZE; i++) {
            positions.add(current);
            current = current.shiftedBy(gridDirection);
        }

        return positions;
    }
}