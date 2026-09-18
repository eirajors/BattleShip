package no.uib.inf102.battleship.controller;

import no.uib.inf102.battleship.controller.AI.IStrategy;
import no.uib.inf102.battleship.model.GameState;
import no.uib.inf102.battleship.model.shot.ShotResult;
import no.uib.inf102.grid.CellPosition;

public class BattleshipAIController {

    private final ControllableBattleshipModel model;
    private final IStrategy strategy;

    public BattleshipAIController(
            ControllableBattleshipModel model,
            IStrategy strategy) {
        this.model = model;
        this.strategy = strategy;
    }

    public void takeTurn() {
        if (model.getGameState() != GameState.AI_TURN) {
            return;
        }

        CellPosition position = strategy.chooseShot();

        ShotResult result = model.shootPlayer(position);

        boolean hit = result == ShotResult.HIT
                || result == ShotResult.SUNK;

        strategy.registerShot(position, hit);
    }
}