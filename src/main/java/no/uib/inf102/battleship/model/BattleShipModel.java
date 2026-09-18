package no.uib.inf102.battleship.model;

import no.uib.inf102.battleship.controller.ControllableBattleshipModel;
import no.uib.inf102.battleship.model.ship.Direction;
import no.uib.inf102.battleship.model.shot.ShotResult;
import no.uib.inf102.grid.CellPosition;
import no.uib.inf102.battleship.view.gameView.ViewableBattleshipModel;
import no.uib.inf102.battleship.controller.AI.AIShipPlacer;

/**
 * Represents the main model for a Battleship game.
 *
 * The model keeps track of the player's board, the opponent's board,
 * and the current state of the game.
 *
 * The model also controls whose turn it is and delegates ship placement
 * and shooting to the appropriate boards.
 */
public class BattleShipModel implements ControllableBattleshipModel, ViewableBattleshipModel {

    private final BattleShipBoard playerBoard;
    private final BattleShipBoard opponentBoard;

    private GameState gameState;

    /**
     * Creates a new Battleship game.
     *
     * The player's board starts empty, while the opponent's ships
     * are placed automatically using the provided baseline ship placer.
     *
     * The game starts in the ship placement phase.
     */
    public BattleShipModel() {
        playerBoard = new BattleShipBoard();
        opponentBoard = new BattleShipBoard();

        AIShipPlacer shipPlacer = new AIShipPlacer();
        shipPlacer.placeShips(opponentBoard);

        gameState = GameState.PLACING_SHIPS;
    }

    /**
     * Gets the player's board.
     *
     * @return the player's board
     */
    public BattleShipBoard getPlayerBoard() {
        return playerBoard;
    }

    /**
     * Gets the opponent's board.
     *
     * @return the opponent's board
     */
    public BattleShipBoard getOpponentBoard() {
        return opponentBoard;
    }

    /**
     * Gets the result of a shot fired at the opponent's board.
     *
     * @param position the position to check
     * @return the result of the shot at the given position
     */
    public ShotResult getOpponentShotResult(CellPosition position) {
        return opponentBoard.getShotResult(position);
    }

    /**
     * Gets the current state of the game.
     *
     * @return the current game state
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Checks whether it is currently the player's turn.
     *
     * @return true if it is the player's turn, otherwise false
     */
    public boolean isPlayerTurn() {
        return gameState == GameState.PLAYER_TURN;
    }

    /**
     * Places a ship on the player's board.
     *
     * Ships can only be placed while the game is in the ship placement
     * phase. Once all required ships have been placed, the game changes
     * to the player's turn.
     *
     * @param position the starting position of the ship
     * @param direction the direction of the ship
     * @return true if the ship was successfully placed, otherwise false
     */
    @Override
    public boolean placePlayerShip(CellPosition position, Direction direction) {
        if (gameState != GameState.PLACING_SHIPS) {
            return false;
        }

        boolean placed = playerBoard.placeShip(position, direction);

        if (placed
                && playerBoard.getShips().size() == BattleShipBoard.NUMBER_OF_SHIPS) {
            gameState = GameState.PLAYER_TURN;
        }

        return placed;
    }

    /**
     * Places a ship on the opponent's board.
     *
     * This method is used when setting up the game. In the normal
     * Human vs AI game, the provided AI ship placer handles this.
     *
     * @param position the starting position of the ship
     * @param direction the direction of the ship
     * @return true if the ship was successfully placed, otherwise false
     */
    @Override
    public boolean placeOpponentShip(CellPosition position, Direction direction) {
        if (gameState != GameState.PLACING_SHIPS) {
            return false;
        }

        return opponentBoard.placeShip(position, direction);
    }

    /**
     * Shoots at the opponent's board.
     *
     * The player can only shoot during the player's turn.
     * After a valid shot, the turn changes to the AI unless
     * the game has ended.
     *
     * @param position the position to shoot at
     * @return the result of the shot
     * @throws IllegalStateException if it is not the player's turn
     */
    @Override
    public ShotResult shootOpponent(CellPosition position) {
        if (gameState != GameState.PLAYER_TURN) {
            throw new IllegalStateException("It is not the player's turn");
        }

        ShotResult result = opponentBoard.shoot(position);

        if (opponentBoard.allShipsSunk()) {
            gameState = GameState.GAME_OVER;
        } else if (result != ShotResult.ALREADY_SHOT) {
            gameState = GameState.AI_TURN;
        }

        return result;
    }

    /**
     * Shoots at the player's board.
     *
     * The AI can only shoot during the AI's turn.
     * After a valid shot, the turn changes back to the player unless
     * the game has ended.
     *
     * @param position the position to shoot at
     * @return the result of the shot
     * @throws IllegalStateException if it is not the AI's turn
     */
    @Override
    public ShotResult shootPlayer(CellPosition position) {
        if (gameState != GameState.AI_TURN) {
            throw new IllegalStateException("It is not the AI's turn");
        }

        ShotResult result = playerBoard.shoot(position);

        if (playerBoard.allShipsSunk()) {
            gameState = GameState.GAME_OVER;
        } else if (result != ShotResult.ALREADY_SHOT) {
            gameState = GameState.PLAYER_TURN;
        }

        return result;
    }

    /**
     * Checks whether the game has ended.
     *
     * @return true if the game is over, otherwise false
     */
    @Override
    public boolean isGameOver() {
        return gameState == GameState.GAME_OVER;
    }

    /**
     * Checks whether the player has won the game.
     *
     * The player wins when all ships on the opponent's board
     * have been sunk.
     *
     * @return true if the player has won, otherwise false
     */
    @Override
    public boolean playerWon() {
        return opponentBoard.allShipsSunk();
    }

    /**
     * Checks whether the opponent has won the game.
     *
     * The opponent wins when all ships on the player's board
     * have been sunk.
     *
     * @return true if the opponent has won, otherwise false
     */
    @Override
    public boolean opponentWon() {
        return playerBoard.allShipsSunk();
    }

    /**
     * Checks whether a position on the player's board has been shot at.
     *
     * @param position the position to check
     * @return true if the position has been shot at, otherwise false
     */
    @Override
    public boolean hasPlayerBeenShot(CellPosition position) {
        return playerBoard.hasBeenShot(position);
    }

    /**
     * Checks whether a position on the opponent's board has been shot at.
     *
     * @param position the position to check
     * @return true if the position has been shot at, otherwise false
     */
    @Override
    public boolean hasOpponentBeenShot(CellPosition position) {
        return opponentBoard.hasBeenShot(position);
    }
}