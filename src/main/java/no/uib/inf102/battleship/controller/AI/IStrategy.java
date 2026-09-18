package no.uib.inf102.battleship.controller.AI;

import no.uib.inf102.grid.CellPosition;

public interface IStrategy {

    /**
     * Chooses the next position to shoot at.
     *
     * @return the position the AI wants to shoot
     */
    CellPosition chooseShot();

    /**
     * Informs the strategy about the result of a shot.
     *
     * @param position the position that was shot
     * @param hit whether the shot was a hit
     */
    void registerShot(CellPosition position, boolean hit);
}