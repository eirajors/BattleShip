package no.uib.inf102.battleship.controller.AI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;

import no.uib.inf102.battleship.model.BattleShipBoard;
import no.uib.inf102.battleship.model.ship.Direction;
import no.uib.inf102.grid.CellPosition;

/**
 * Student implementation of a ship placement strategy.
 *
 * Design your own strategy for placing all ships on the board.
 *
 * Your placement should aim to make it difficult for the opponent
 * to find and sink your ships.
 */
public class MyPlacer implements IPlacer {
    private static final int BOARD_SIZE = BattleShipBoard.SIZE; 
    private static final int MAX_ATTEMPTS = 10_000; 
    private static final int CENTRE_LOW = 5; 
    private static final int CENTRE_HIGH = 6; 
    private int low; 
    private int high; 
    private int horizontal; 
    private int vertical; 
    private List<CellPosition> positions; 
    // private int[] illegalPlacements; 

    private final Random random; 


    public MyPlacer() {
        random = new Random(); 
        low = 1; 
        high = 10; 
        horizontal = 0; 
        vertical = 0; 
        positions = new ArrayList<>(); 

    }
    /**
     * Places all ships on the given board.
     *
     * You may choose the positions and directions of the ships.
     *
     * The board will reject invalid placements, such as placements
     * outside the board or overlapping an existing ship.
     *
     * @param board the board where the ships should be placed
     * @throws IllegalStateException if all ships could not be placed
     *         within the maximum number of attempts
     */
    @Override
    public void placeShips(BattleShipBoard board) {
        // board.placeShip(new CellPosition(1, 1), Direction.HORIZONTAL);
        // board.placeShip(new CellPosition(4, 5), Direction.HORIZONTAL);
        // board.placeShip(new CellPosition(6, 3), Direction.VERTICAL);

        int attempts = 0; 

        while (board.getShips().size() < BattleShipBoard.NUMBER_OF_SHIPS) {
            if (attempts++ >= MAX_ATTEMPTS) {
                throw new IllegalStateException(
                        "Could not place all ships. "
                        + "Check BattleShipBoard.placeShip()."
                );
            }

            Direction direction = chooseDirection();
            CellPosition position = choosePosition(direction);

            if (board.placeShip(position, direction)) {
                if (direction == Direction.HORIZONTAL) {
                    horizontal ++; 
                }
                else {
                    vertical ++;
                }
                positions.add(position); 
            }
        }

    }


    /**
     * Chooses a random direction. Cannot place more than 2 ships of the same direction
     * 
     * @return the direction of a ship
     */
    private Direction chooseDirection() {
        if (horizontal == 2) {
            return Direction.VERTICAL;
        }
        if (vertical == 2) {
            return Direction.HORIZONTAL;
        }
        if (random.nextBoolean()) {
            return Direction.HORIZONTAL; 
        }

        return Direction.VERTICAL;
    }

    /**
     * Chooses the position of a ship, but can not place ships in the centre 
     * as that is more likely for a human opponent to shoot at
     * Also does not choose a start position at the edges of the board
     * @return the position of the ships
     */
    private CellPosition choosePosition(Direction dir) {
        
        int row = random.nextInt(high - low) + low; 
        int col = random.nextInt(high - low) + low; 

        while (true) {
            row = random.nextInt(high - low) + low; 
            col = random.nextInt(high - low) + low; 
            if (!overlapsCentre(row, col, dir)) {
                break; 
            }
        }

        return new CellPosition(row, col); 
    }

    /**
     * Checks if the ship that is going to be placed overlaps the centre
     * 
     * @param row what row is the start position
     * @param col what column is the start position
     * @param dir the direction of the ship placement 
     * @return true if it overlaps the centre, false otherwise
     */
    private boolean overlapsCentre(int row, int col, Direction dir) {
        for (int i = 0; i < 3; i ++) {
            if (dir == Direction.VERTICAL) {
                row ++;
            }
            else {
                col ++;
            }
            if (row >= CENTRE_LOW && row <= CENTRE_HIGH &&
                    col >= CENTRE_LOW && col <= CENTRE_HIGH) {
                        return true; 
                }
        }
        return false; 
    }
}