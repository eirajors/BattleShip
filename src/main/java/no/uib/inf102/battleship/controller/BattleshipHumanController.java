package no.uib.inf102.battleship.controller;

import no.uib.inf102.battleship.model.ship.Direction;
import no.uib.inf102.grid.CellPosition;

public class BattleshipHumanController implements BattleshipController {

    private final ControllableBattleshipModel model;

    public BattleshipHumanController(ControllableBattleshipModel model) {
        this.model = model;
    }

    @Override
    public boolean placeShip(CellPosition position, Direction direction) {
        return model.placePlayerShip(position, direction);
    }

    @Override
    public void shoot(CellPosition position) {
        model.shootOpponent(position);
    }
}