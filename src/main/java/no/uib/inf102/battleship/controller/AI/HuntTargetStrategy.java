package no.uib.inf102.battleship.controller.AI;

import no.uib.inf102.grid.CellPosition;

import java.util.Random;

/**
 * A simple baseline shooting strategy.
 *
 * Students should not modify this class. It is used as a baseline
 * for comparing the strategy implemented in MyStrategy.
 */
public class HuntTargetStrategy implements IStrategy {

    private static final int BOARD_SIZE = 10;

    private final Random random;
    private final boolean[][] shotAt;

    public HuntTargetStrategy() {
        random = new Random();
        shotAt = new boolean[BOARD_SIZE][BOARD_SIZE];
    }

    /**
     * Chooses a random cell that has not been shot at before.
     *
     * @return the next position to shoot
     */
    @Override
    public CellPosition chooseShot() {
        while (true) {
            int row = random.nextInt(BOARD_SIZE);
            int col = random.nextInt(BOARD_SIZE);

            if (!shotAt[row][col]) {
                return new CellPosition(row, col);
            }
        }
    }

    /**
     * Records that a cell has been shot at.
     *
     * The baseline strategy does not use the result of the shot.
     *
     * @param position the position that was shot
     * @param hit whether the shot was a hit
     */
    @Override
    public void registerShot(CellPosition position, boolean hit) {
        shotAt[position.row()][position.col()] = true;
    }
}