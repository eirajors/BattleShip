package no.uib.inf102.battleship.controller;

import no.uib.inf102.battleship.model.ship.Direction;
import no.uib.inf102.grid.CellPosition;

public interface BattleshipController {

    boolean placeShip(CellPosition position, Direction direction);

    void shoot(CellPosition position);
}