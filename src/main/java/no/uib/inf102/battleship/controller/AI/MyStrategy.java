package no.uib.inf102.battleship.controller.AI;

import no.uib.inf102.grid.CellPosition;

/**
 * Student implementation of a shooting strategy.
 *
 * Design your own strategy for choosing which cells to shoot at.
 * Your strategy should use the information from previous shots
 * to improve its future choices.
 */
public class MyStrategy implements IStrategy {

    /**
     * Chooses the next position to shoot.
     *
     * @return the position to shoot at
     */
    @Override
    public CellPosition chooseShot() {
        // TODO: Implement your shooting strategy.
        return null;
    }

    /**
     * Receives information about the result of the previous shot.
     *
     * Use this information to update your strategy.
     *
     * @param position the position that was shot
     * @param hit whether the shot was a hit
     */
    @Override
    public void registerShot(CellPosition position, boolean hit) {
        // TODO: Update your strategy based on the shot result.
    }
}