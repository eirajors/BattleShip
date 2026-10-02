package no.uib.inf102.battleship.controller.AI;

import java.util.ArrayList;
import java.util.List;

import no.uib.inf102.battleship.model.shot.ShotResult;
import no.uib.inf102.grid.CellPosition;

/**
 * Student implementation of a shooting strategy.
 *
 * Design your own strategy for choosing which cells to shoot at.
 * Your strategy should use the information from previous shots
 * to improve its future choices.
 */
public class MyStrategy implements IStrategy {


    private static final int BOARD_SIZE = 10; 

    private final ShotResult[][] hitsChart; 
    private CellPosition orgHitPos; 
    private boolean isHit; 
    private List<CellPosition> hitPositions; 
    private int patternRow; 


    public  MyStrategy() {
        hitsChart = new ShotResult[BOARD_SIZE][BOARD_SIZE]; 
        this.orgHitPos = null; 
        this.isHit = false; 
        this.hitPositions = new ArrayList<>(); 
        this.patternRow = 0; 
    }

    /**
     * Chooses the next position to shoot.
     *
     * @return the position to shoot at
     */
    @Override
    public CellPosition chooseShot() {

        if (!isHit) {
            CellPosition pos = shootEveryThird(); 
            if (pos == null) { 
                pos = checkAroundHitPositions(); 
            }
            if (pos == null) {
                pos = shootUnshot(); 
            }
            return pos; 
        }
        else {
            CellPosition pos = shootAroundHitsChart(orgHitPos);
            return pos;
        }

    }

    /**
     * Receives information about the result of the previous shot.
     *
     * Use this information to update your strategy.
     *
     * @param position the position that was shot
     * @param hit whether the shot was a hit
     */
    @Override
    public void registerShot(CellPosition position, boolean hit) {

        if (hit) {
            hitsChart[position.row()][position.col()] = ShotResult.HIT; 
            
            if (!isHit) {
                orgHitPos = position;
            }
            isHit = true; 
            hitPositions.add(position);

        }
        else {
            hitsChart[position.row()][position.col()] = ShotResult.MISS; 
        }

    }


    /**
     * Shoots around the given cellposition. It will shoot in every direction until it gets 
     * a Shotresult.MISS from both checkcolumns and checkrows. It will then return the default shooting
     * method. 
     * Has a side effect of changing {@code isHit} to false if there are no available shots around the hitposition
     * 
     * @param position the cellposition which was hit and is going to be shot around
     * @return the next available cellposition to shoot
     */
    private CellPosition shootAroundHitsChart (CellPosition position) {
        
        CellPosition next = lookAroundPosition(position); 

        if (next == null) {
            isHit = false; 
            next = shootEveryThird(); 
        }
        if (next == null) {
            next = checkAroundHitPositions(); 
        }
        if (next == null) {
            next = shootUnshot(); 
        }

        return next; 
    }

    /**
     * Looks around a hitposition for any other hits.
     * 
     * @param position a hitposition
     * @return an empty cellposition next to a hitposition. If all directions are checked, it will return null
     */
    private CellPosition lookAroundPosition(CellPosition position) {

        CellPosition next = checkDirections(position, 0, 1); 

        if (next == null) {
            next = checkDirections(position, 0, -1); 
        }
        if (next == null) { 
            next = checkDirections(position, 1, 0); 
        }
        if (next == null) {
            next = checkDirections(position, -1, 0); 
        }
        
        return next; 
    }

    /**
     * If not all hitpositions are found before the end of the board, this
     * will check around all hitpositions found until the remainding hitpositions are found
     * If all positions around a hitposition is a miss, it will continue to the next hitposition 
     * 
     * @return next available cellposition next to a hitposition, if all positions around a hitposition are checked
     *         null will be returned
     */
    private CellPosition checkAroundHitPositions () {
        CellPosition next = null; 

        for (CellPosition pos : hitPositions) {
            next = lookAroundPosition(pos);
            if (next != null) {
                return next; 
            }
        }

        return null;

    }

    /**
     * Checks the columns or rows around a cellposition depending on the direction
     * 
     * @param position the original hit position 
     * @param rDir the direction in which to shoot for the row
     * @param cDir the direction in which to shoot for the column
     * @return a new cellposition to shoot if the next cellposition is empty, if it is already a miss
     *         it will return null
     */
    private CellPosition checkDirections (CellPosition position, int rDir, int cDir) {

        int col = position.col() + cDir;
        int row = position.row() + rDir; 

        while (row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE ) {
            if (hitsChart[row][col] == null) {
                    return new CellPosition(row, col);
            }
            if (hitsChart[row][col] == ShotResult.MISS) {
                return null; 
            }
            col += cDir; 
            row += rDir; 
        }
        
        return null; 

    }
    
    /**
     * Default shooting method. Shoots every third column in a row 
     * Has a side effect of changing the {@code patternRow} by which row it is currently searching
     * 
     * @return the cellposition to shoot 
     */
    private CellPosition shootEveryThird () {

        for (; patternRow < BOARD_SIZE; patternRow++) {

            int start = patternRow % 3; 

            for (int col = start; col < BOARD_SIZE; col += 3) {

                if (hitsChart[patternRow][col] == null) {
                    return new CellPosition(patternRow, col); 
                }
                
            }
        }

        return null; 
    }

    /**
     * Shoots all the missing cellpositions if every other method fails to shoot all the ships
     * 
     * @return the cellposition to shoot, null if there are no more cellpositions available
     */
    private CellPosition shootUnshot() {

        for (int row = 0; row < BOARD_SIZE; row ++) {

            for (int col = 0; col < BOARD_SIZE; col ++) {
                if (hitsChart[row][col] == null) {
                    return new CellPosition(row, col);
                }
            }
        }
        return null; 
    }

 
}



