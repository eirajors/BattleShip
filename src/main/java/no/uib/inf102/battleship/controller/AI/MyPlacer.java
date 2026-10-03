package no.uib.inf102.battleship.controller.AI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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

            CellPosition position = choosePosition();
            Direction direction = chooseDirection();

            if (board.placeShip(position, direction)) {
                positions.add(position); 
            }
        }

    }

    private Direction chooseDirection() {
        if (horizontal == 2) {
            return Direction.VERTICAL; 
        }
        if (vertical == 2) {
            return Direction.HORIZONTAL; 
        }

        if (random.nextBoolean()) {
            horizontal ++; 
            return Direction.HORIZONTAL; 
        }
        vertical ++; 

        return Direction.VERTICAL;
    }

    private CellPosition choosePosition() {
        int[] illegalPlacements = {5, 6}; 

        int row = random.nextInt(high - low) + low; 
        int col = random.nextInt(high - low) + low; 
        List illArr = Arrays.asList(illegalPlacements); 

        while (illArr.contains(row) && illArr.contains(col)) {
            row = random.nextInt(high - low) + low; 
            col = random.nextInt(high - low) + low; 
        }

        return new CellPosition(row, col); 
    }
}