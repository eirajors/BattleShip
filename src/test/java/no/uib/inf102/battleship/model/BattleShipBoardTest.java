package no.uib.inf102.battleship.model;

import no.uib.inf102.battleship.model.ship.Direction;
import no.uib.inf102.battleship.model.shot.ShotResult;
import no.uib.inf102.grid.CellPosition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BattleShipBoardTest {

    @Test
    void boardStartsWithNoShips() {

        BattleShipBoard board = new BattleShipBoard();

        assertEquals(0, board.getShips().size());
    }

    @Test
    void canPlaceHorizontalShip() {

        BattleShipBoard board = new BattleShipBoard();

        boolean placed = board.placeShip(
                new CellPosition(2, 2),
                Direction.HORIZONTAL
        );

        assertTrue(placed);
        assertEquals(1, board.getShips().size());
    }

    @Test
    void canPlaceVerticalShip() {

        BattleShipBoard board = new BattleShipBoard();

        boolean placed = board.placeShip(
                new CellPosition(2, 2),
                Direction.VERTICAL
        );

        assertTrue(placed);
        assertEquals(1, board.getShips().size());
    }

    @Test
    void cannotPlaceHorizontalShipOutsideBoard() {

        BattleShipBoard board = new BattleShipBoard();

        boolean placed = board.placeShip(
                new CellPosition(2, 9),
                Direction.HORIZONTAL
        );

        assertFalse(placed);
        assertEquals(0, board.getShips().size());
    }

    @Test
    void cannotPlaceVerticalShipOutsideBoard() {

        BattleShipBoard board = new BattleShipBoard();

        boolean placed = board.placeShip(
                new CellPosition(9, 2),
                Direction.VERTICAL
        );

        assertFalse(placed);
        assertEquals(0, board.getShips().size());
    }

    @Test
    void cannotPlaceOverlappingShips() {

        BattleShipBoard board = new BattleShipBoard();

        assertTrue(
                board.placeShip(
                        new CellPosition(2, 2),
                        Direction.HORIZONTAL
                )
        );

        assertFalse(
                board.placeShip(
                        new CellPosition(2, 3),
                        Direction.VERTICAL
                )
        );

        assertEquals(1, board.getShips().size());
    }

    @Test
    void canPlaceThreeShips() {

        BattleShipBoard board = new BattleShipBoard();

        assertTrue(
                board.placeShip(
                        new CellPosition(0, 0),
                        Direction.HORIZONTAL
                )
        );

        assertTrue(
                board.placeShip(
                        new CellPosition(2, 0),
                        Direction.HORIZONTAL
                )
        );

        assertTrue(
                board.placeShip(
                        new CellPosition(4, 0),
                        Direction.HORIZONTAL
                )
        );

        assertEquals(
                BattleShipBoard.NUMBER_OF_SHIPS,
                board.getShips().size()
        );
    }

    @Test
    void cannotPlaceMoreThanThreeShips() {

        BattleShipBoard board = new BattleShipBoard();

        assertTrue(
                board.placeShip(
                        new CellPosition(0, 0),
                        Direction.HORIZONTAL
                )
        );

        assertTrue(
                board.placeShip(
                        new CellPosition(2, 0),
                        Direction.HORIZONTAL
                )
        );

        assertTrue(
                board.placeShip(
                        new CellPosition(4, 0),
                        Direction.HORIZONTAL
                )
        );

        assertFalse(
                board.placeShip(
                        new CellPosition(6, 0),
                        Direction.HORIZONTAL
                )
        );

        assertEquals(3, board.getShips().size());
    }

    @Test
    void shootingShipReturnsHit() {

        BattleShipBoard board = new BattleShipBoard();

        board.placeShip(
                new CellPosition(2, 2),
                Direction.HORIZONTAL
        );

        ShotResult result = board.shoot(
                new CellPosition(2, 3)
        );

        assertEquals(
                ShotResult.HIT,
                result
        );
    }

    @Test
    void shootingWaterReturnsMiss() {

        BattleShipBoard board = new BattleShipBoard();

        board.placeShip(
                new CellPosition(2, 2),
                Direction.HORIZONTAL
        );

        ShotResult result = board.shoot(
                new CellPosition(5, 5)
        );

        assertEquals(
                ShotResult.MISS,
                result
        );
    }

    @Test
    void shootingSamePositionTwiceReturnsAlreadyShot() {

        BattleShipBoard board = new BattleShipBoard();

        board.placeShip(
                new CellPosition(2, 2),
                Direction.HORIZONTAL
        );

        CellPosition position = new CellPosition(2, 3);

        board.shoot(position);

        ShotResult result = board.shoot(position);

        assertEquals(
                ShotResult.ALREADY_SHOT,
                result
        );
    }

    @Test
    void shootingAllThreeCellsReturnsSunk() {

        BattleShipBoard board = new BattleShipBoard();

        board.placeShip(
                new CellPosition(2, 2),
                Direction.HORIZONTAL
        );

        board.shoot(new CellPosition(2, 2));
        board.shoot(new CellPosition(2, 3));

        ShotResult result =
                board.shoot(new CellPosition(2, 4));

        assertEquals(
                ShotResult.SUNK,
                result
        );
    }

    @Test
    void shipIsNotSunkAfterOneHit() {

        BattleShipBoard board = new BattleShipBoard();

        board.placeShip(
                new CellPosition(2, 2),
                Direction.HORIZONTAL
        );

        board.shoot(
                new CellPosition(2, 2)
        );

        assertFalse(board.allShipsSunk());
    }

    @Test
    void allShipsSunkAfterAllThreeShipsAreDestroyed() {

        BattleShipBoard board = new BattleShipBoard();

        board.placeShip(
                new CellPosition(0, 0),
                Direction.HORIZONTAL
        );

        board.placeShip(
                new CellPosition(2, 0),
                Direction.HORIZONTAL
        );

        board.placeShip(
                new CellPosition(4, 0),
                Direction.HORIZONTAL
        );

        for (int row : new int[]{0, 2, 4}) {

            board.shoot(
                    new CellPosition(row, 0)
            );

            board.shoot(
                    new CellPosition(row, 1)
            );

            board.shoot(
                    new CellPosition(row, 2)
            );
        }

        assertTrue(board.allShipsSunk());
    }

    @Test
    void allShipsSunkIsFalseBeforeAllShipsAreDestroyed() {

        BattleShipBoard board = new BattleShipBoard();

        board.placeShip(
                new CellPosition(0, 0),
                Direction.HORIZONTAL
        );

        board.placeShip(
                new CellPosition(2, 0),
                Direction.HORIZONTAL
        );

        board.placeShip(
                new CellPosition(4, 0),
                Direction.HORIZONTAL
        );

        board.shoot(new CellPosition(0, 0));
        board.shoot(new CellPosition(0, 1));
        board.shoot(new CellPosition(0, 2));

        assertFalse(board.allShipsSunk());
    }

    @Test
    void shootingOutsideBoardThrowsException() {

        BattleShipBoard board = new BattleShipBoard();

        assertThrows(
                IllegalArgumentException.class,
                () -> board.shoot(
                        new CellPosition(-1, 0)
                )
        );
    }

    @Test
    void hasBeenShotReturnsTrueAfterShot() {

        BattleShipBoard board = new BattleShipBoard();

        CellPosition position =
                new CellPosition(5, 5);

        board.shoot(position);

        assertTrue(
                board.hasBeenShot(position)
        );
    }

    @Test
    void hasBeenShotReturnsFalseBeforeShot() {

        BattleShipBoard board = new BattleShipBoard();

        CellPosition position =
                new CellPosition(5, 5);

        assertFalse(
                board.hasBeenShot(position)
        );
    }

    @Test
    void getShotResultReturnsResultOfShot() {

        BattleShipBoard board = new BattleShipBoard();

        CellPosition position =
                new CellPosition(5, 5);

        board.shoot(position);

        assertEquals(
                ShotResult.MISS,
                board.getShotResult(position)
        );
    }
}