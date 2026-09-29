package no.uib.inf102.battleship.controller.AI;

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
    // private static final int BOARD_SIZE = BattleShipBoard.SIZE; 

    // public MyPlacer() {
    // }
    
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
        board.placeShip(new CellPosition(1, 1), Direction.HORIZONTAL);
        board.placeShip(new CellPosition(4, 5), Direction.HORIZONTAL);
        board.placeShip(new CellPosition(6, 3), Direction.VERTICAL);


    }
}