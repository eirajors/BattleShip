package no.uib.inf102.battleship.controller.AI;

import no.uib.inf102.battleship.model.BattleShipBoard;

public interface IPlacer {

    /**
     * Places all ships on the given board.
     *
     * @param board the board where the ships should be placed
     */
    void placeShips(BattleShipBoard board);
}