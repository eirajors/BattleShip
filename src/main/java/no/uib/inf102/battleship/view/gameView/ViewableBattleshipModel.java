package no.uib.inf102.battleship.view.gameView;

import no.uib.inf102.battleship.model.BattleShipBoard;
import no.uib.inf102.battleship.model.GameState;
import no.uib.inf102.grid.CellPosition;
import no.uib.inf102.battleship.model.ship.Direction;
import no.uib.inf102.battleship.model.shot.ShotResult;

public interface ViewableBattleshipModel {

    BattleShipBoard getPlayerBoard();

    BattleShipBoard getOpponentBoard();

    GameState getGameState();

    boolean isPlayerTurn();

    boolean isGameOver();

    boolean playerWon();

    boolean opponentWon();

    boolean hasPlayerBeenShot(CellPosition position);

    boolean hasOpponentBeenShot(CellPosition position);

    ShotResult getOpponentShotResult(CellPosition position);

    boolean placePlayerShip(CellPosition position, Direction direction);

    ShotResult shootOpponent(CellPosition position);
}