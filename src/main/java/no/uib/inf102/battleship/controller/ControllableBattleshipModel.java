package no.uib.inf102.battleship.controller;

import no.uib.inf102.battleship.model.BattleShipBoard;
import no.uib.inf102.battleship.model.GameState;
import no.uib.inf102.battleship.model.ship.Direction;
import no.uib.inf102.battleship.model.shot.ShotResult;
import no.uib.inf102.grid.CellPosition;

public interface ControllableBattleshipModel {

    BattleShipBoard getPlayerBoard();

    BattleShipBoard getOpponentBoard();

    GameState getGameState();

    boolean isPlayerTurn();

    boolean placePlayerShip(CellPosition position, Direction direction);

    boolean placeOpponentShip(CellPosition position, Direction direction);

    ShotResult shootOpponent(CellPosition position);

    ShotResult shootPlayer(CellPosition position);

    boolean isGameOver();

    boolean playerWon();

    boolean opponentWon();
}