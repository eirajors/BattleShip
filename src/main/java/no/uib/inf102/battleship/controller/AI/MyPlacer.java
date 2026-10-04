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
    private static final int CENTRE_LOW = 4; 
    private static final int CENTRE_HIGH = 5; 
    private static final int SHIP_LENGTH = 3; 

    private final boolean[][] occupied; 
    private final Random random; 
    private int horizontal; 
    private int vertical; 


    public MyPlacer() {
        random = new Random(); 
        occupied = new boolean[BOARD_SIZE][BOARD_SIZE]; 
        horizontal = 0; 
        vertical = 0; 
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

            if (touchesAnotherShip(position.row(), position.col(), direction)) {
                continue; 
            }
            
            if (board.placeShip(position, direction)) {
                markOccupied(position, direction);
                if (direction == Direction.HORIZONTAL) {
                    horizontal ++; 
                    }
                else {
                    vertical ++;
                }
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
     * @return the position of the ships
     */
    private CellPosition choosePosition(Direction dir) {
        
        int row = random.nextInt(BOARD_SIZE); 
        int col = random.nextInt(BOARD_SIZE); 

        while (overlapsCentre(row, col, dir)) {
            row = random.nextInt(BOARD_SIZE); 
            col = random.nextInt(BOARD_SIZE); 
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
        for (int i = 0; i < SHIP_LENGTH; i ++) {
            
            if (row >= CENTRE_LOW && row <= CENTRE_HIGH &&
                    col >= CENTRE_LOW && col <= CENTRE_HIGH) {
                        return true; 
            }
            
            if (dir == Direction.VERTICAL) {
                row ++;
            }
            else {
                col ++;
            }
        }
        return false; 
    }

    /**
     * Checks if a ship will be next to another ship 
     * 
     * @param row the start row of the ship
     * @param col the start column of the ship
     * @param dir the direction of the ship
     * @return true if it has at least one position next to another ship, false otherwise
     */
    private boolean touchesAnotherShip(int row, int col, Direction dir) {

        for (int i = 0; i < SHIP_LENGTH; i ++) {
            
            for (int dr = -1; dr <= 1; dr ++) {
                for (int dc = -1; dc <= 1; dc ++) {
                    if (isOccupied(row + dr, col + dc)) {
                        return true; 
                    }
                }
            }
            if (dir == Direction.VERTICAL) {
                row ++;
            }
            else {
                col ++;
            }
        }
        return false; 
    }

    /**
     * Checks if a position is occupied
     * @param row the row in the position to check  
     * @param col the column in the position to check
     * @return false if the position is outside the board or if a position is available
     *         true otherwise
     */
    private boolean isOccupied(int row, int col) {
        if (row < 0 || col < 0 || row >= BOARD_SIZE || col >= BOARD_SIZE) {
            return false; 
        }
        return occupied[row][col]; 
    }

    /**
     * Marks the positions of a ship in the occupied matrix
     * 
     * @param pos the starting position of the ship
     * @param dir the direction of the ship
     */
    private void markOccupied (CellPosition pos, Direction dir) {
        int row = pos.row(); 
        int col = pos.col(); 
        
        for (int i = 0; i < SHIP_LENGTH; i ++) {
            occupied[row][col] = true; 

            if (dir == Direction.HORIZONTAL) {
                col ++;  

            }
            else {
                row ++;
            }
            
        }
    }

}