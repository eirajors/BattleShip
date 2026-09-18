package no.uib.inf102.battleship.controller.AI;

import no.uib.inf102.battleship.model.BattleShipBoard;
import no.uib.inf102.battleship.model.ship.Direction;
import no.uib.inf102.grid.CellPosition;

import java.util.Random;

/**
 * Baseline ship placement strategy.
 *
 * This placer randomly chooses positions and directions until all ships
 * have been successfully placed on the board.
 *
 * Students should not modify this class. It is used as a baseline
 * for comparing their own placement strategy in MyPlacer.
 */
public class AIShipPlacer implements IPlacer {

    private static final int BOARD_SIZE = BattleShipBoard.SIZE;
    private static final int MAX_ATTEMPTS = 10_000;

    private final Random random;

    /**
     * Creates a new random ship placer.
     */
    public AIShipPlacer() {
        random = new Random();
    }

    /**
     * Places all ships randomly on the board.
     *
     * Invalid placements are rejected by the board, and the placer
     * keeps trying until all ships have been placed.
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

            CellPosition position = randomPosition();
            Direction direction = randomDirection();

            board.placeShip(position, direction);
        }
    }

    /**
     * Generates a random position on the board.
     */
    private CellPosition randomPosition() {
        int row = random.nextInt(BOARD_SIZE);
        int col = random.nextInt(BOARD_SIZE);

        return new CellPosition(row, col);
    }

    /**
     * Generates a random horizontal or vertical direction.
     */
    private Direction randomDirection() {
        if (random.nextBoolean()) {
            return Direction.HORIZONTAL;
        }

        return Direction.VERTICAL;
    }
}