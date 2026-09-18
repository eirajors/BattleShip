package no.uib.inf102.battleship.model.ship;

import no.uib.inf102.grid.CellPosition;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {

    @Test
    void horizontalShipHasThreePositions() {
        Ship ship = new Ship(
                new CellPosition(2, 3),
                Direction.HORIZONTAL
        );

        assertEquals(3, ship.getPositions().size());
    }

    @Test
    void verticalShipHasThreePositions() {
        Ship ship = new Ship(
                new CellPosition(2, 3),
                Direction.VERTICAL
        );

        assertEquals(3, ship.getPositions().size());
    }

    @Test
    void horizontalShipHasCorrectPositions() {
        Ship ship = new Ship(
                new CellPosition(2, 3),
                Direction.HORIZONTAL
        );

        List<CellPosition> positions = ship.getPositions();

        assertTrue(positions.contains(new CellPosition(2, 3)));
        assertTrue(positions.contains(new CellPosition(2, 4)));
        assertTrue(positions.contains(new CellPosition(2, 5)));
    }

    @Test
    void verticalShipHasCorrectPositions() {
        Ship ship = new Ship(
                new CellPosition(2, 3),
                Direction.VERTICAL
        );

        List<CellPosition> positions = ship.getPositions();

        assertTrue(positions.contains(new CellPosition(2, 3)));
        assertTrue(positions.contains(new CellPosition(3, 3)));
        assertTrue(positions.contains(new CellPosition(4, 3)));
    }

    @Test
    void containsReturnsTrueForShipPosition() {
        Ship ship = new Ship(
                new CellPosition(2, 3),
                Direction.HORIZONTAL
        );

        assertTrue(
                ship.contains(new CellPosition(2, 4))
        );
    }

    @Test
    void containsReturnsFalseForOtherPosition() {
        Ship ship = new Ship(
                new CellPosition(2, 3),
                Direction.HORIZONTAL
        );

        assertFalse(
                ship.contains(new CellPosition(5, 5))
        );
    }

    @Test
    void shipIsNotSunkInitially() {
        Ship ship = new Ship(
                new CellPosition(2, 3),
                Direction.HORIZONTAL
        );

        assertFalse(ship.isSunk());
    }

    @Test
    void shipIsNotSunkAfterOneHit() {
        Ship ship = new Ship(
                new CellPosition(2, 3),
                Direction.HORIZONTAL
        );

        ship.hit(new CellPosition(2, 3));

        assertFalse(ship.isSunk());
    }

    @Test
    void shipIsNotSunkAfterTwoHits() {
        Ship ship = new Ship(
                new CellPosition(2, 3),
                Direction.HORIZONTAL
        );

        ship.hit(new CellPosition(2, 3));
        ship.hit(new CellPosition(2, 4));

        assertFalse(ship.isSunk());
    }

    @Test
    void shipIsSunkAfterThreeHits() {
        Ship ship = new Ship(
                new CellPosition(2, 3),
                Direction.HORIZONTAL
        );

        ship.hit(new CellPosition(2, 3));
        ship.hit(new CellPosition(2, 4));
        ship.hit(new CellPosition(2, 5));

        assertTrue(ship.isSunk());
    }

    @Test
    void hittingPositionOutsideShipReturnsFalse() {
        Ship ship = new Ship(
                new CellPosition(2, 3),
                Direction.HORIZONTAL
        );

        assertFalse(
                ship.hit(new CellPosition(5, 5))
        );
    }

    @Test
    void hittingSamePositionTwiceOnlyRegistersOnce() {
        Ship ship = new Ship(
                new CellPosition(2, 3),
                Direction.HORIZONTAL
        );

        assertTrue(
                ship.hit(new CellPosition(2, 3))
        );

        assertFalse(
                ship.hit(new CellPosition(2, 3))
        );

        assertFalse(ship.isSunk());
    }
}