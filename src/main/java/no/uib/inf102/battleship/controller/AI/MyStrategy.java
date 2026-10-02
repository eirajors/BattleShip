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

    private final boolean[][] shotAt; 
    private final ShotResult[][] hitsChart; 
    private static CellPosition orgHitPos; 
    private static boolean isHit; 
    private static List<CellPosition> hitPositions; 


    public  MyStrategy() {
        shotAt = new boolean[BOARD_SIZE][BOARD_SIZE];
        hitsChart = new ShotResult[BOARD_SIZE][BOARD_SIZE]; 
        orgHitPos = null; 
        isHit = false; 
        hitPositions = new ArrayList<>(); 

    }

    /**
     * Chooses the next position to shoot.
     *
     * @return the position to shoot at
     */
    @Override
    public CellPosition chooseShot() {

        // gå hver tredje, hvis den registrerer hit, tar den alle rundt den før den går videre til neste
        // hvis forrige hits er 0 kaller den på funksjon for å gå videre, hvis den er mellom 0 og 3 så 
        // kaller den på funksjon for å skyte rundt 
        if (!isHit) {
            CellPosition pos = shootEveryThird(); 
            if (pos != null) {
                return pos; 
            }
            return checkAroundHitPositions(); 
        }
        else {
            return shootAroundHitsChart(orgHitPos);
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
        shotAt[position.row()][position.col()] = true;  

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

        return next; 
    }

    private CellPosition lookAroundPosition(CellPosition position) {
        int col = position.col(); 
        int row = position.row(); 
        CellPosition next = null; 

        if (col + 1 < BOARD_SIZE && hitsChart[row][col + 1] != ShotResult.MISS) {
            next = checkColumns(position, 1); 
        }
        if (next == null && col - 1 >= 0 && hitsChart[row][col - 1] != ShotResult.MISS) {
            next = checkColumns(position, -1);
        }
        if (next == null && row + 1 < BOARD_SIZE && hitsChart[row + 1][col] != ShotResult.MISS) { 
            next = checkRows(position, 1);
        }
        if (next == null && row - 1 >= 0 && hitsChart[row - 1][col] != ShotResult.MISS) {
            next = checkRows(position, -1);
        }
        
        return next; 
    }


    // lagre alle hit positions i en liste, og deretter sjekk rundt alle - failsafe
    /**
     * 
     * @return
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

    private CellPosition checkDirections (CellPosition position, int cDir, int rDir) {

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
     * checks the columns either below or above the given cellposition depending on the direction
     * 
     * @param position the original hit position 
     * @param direction the direction in which to shoot
     * @return a new cellposition to shoot if the next cellposition is empty, if it is already a miss
     *         it will return null
     */
    private CellPosition checkColumns (CellPosition position, int direction) {

        int row = position.row();

        for (int col = position.col() + direction; col >= 0 && col < BOARD_SIZE; col += direction) {
                if (hitsChart[row][col] == null) {
                    return new CellPosition(row, col);
                }
                if (hitsChart[row][col] == ShotResult.MISS) {
                    return null; 
                }
            
        }
        return null; 
    }

    
    /**
     * checks the rows either below or above the given cellposition depending on the direction
     * 
     * @param position the original hit position 
     * @param direction the direction in which to shoot
     * @return a new cellposition to shoot if the next cellposition is empty, if it is already a miss
     *         it will return null
     */
    private CellPosition checkRows(CellPosition position, int direction) {

        int col = position.col();

        for (int row = position.row() + direction; row >= 0 && row < BOARD_SIZE; row += direction) {
                if (hitsChart[row][col] == null) {
                    return new CellPosition(row, col);
                }
                if (hitsChart[row][col] == ShotResult.MISS) {
                    return null; 
                }
            
        }
        return null; 
    }
    

    /**
     * Default shooting method. Shoots every third column in a row 
     * 
     * @return the cellposition to shoot 
     */
    private CellPosition shootEveryThird () {
        
        for (int row = 0; row < BOARD_SIZE; row++) {

            int start = row % 3; 

            for (int col = start; col < BOARD_SIZE; col += 3) {

                if (!shotAt[row][col]) {
                    return new CellPosition(row, col); 
                }
                
            }
        }

        return null; 
    }
 
}




   // private CellPosition doubleCheckRows(CellPosition position) {
    //     System.out.println("inne i double check rows"); 

    //     int row = position.row(); 
    //     int col = position.col(); 
        
    //     CellPosition next = null; 

    //     if (row + 2 < BOARD_SIZE && hitsChart[row + 1][col] != ShotResult.MISS) { 
    //         next = checkRows(position, 1);
    //     }
    //     if (next == null && row - 1 > 0 && hitsChart[row - 1][col] != ShotResult.MISS) {
    //         next = checkRows(position, - 1);
    //     }
    //     if (next == null) {
    //         hits = 0; 
    //         return shootEveryThird(); 
    //     }

    //     return next; 

    // }
      //sjekke kolonner først, hvis de ikke er skutt sjekk dem, om de er false gå og sjekk rader
        // skal gå til venstre helt til den treffer false -> lagre forrige posisjon i en curr funksjon? 



        // System.out.println("inne i shoothitschart");
        // if (position.col() - 1 < 0 && hitsChart[lastShot.row()][lastShot.col()] == ShotResult.HIT) {
        //     System.out.println("inne i pos.col < 0");
        //     return checkColumns(position, 1);
        // }

        // if (position.row() - 1 < 0)  {
        //     System.out.println("inne i pos.row < 0");
        //     return checkRows(position, 1);
        // }

        // if (position.col() + 2 > BOARD_SIZE && hitsChart[lastShot.row()][lastShot.col()] == ShotResult.HIT) {
        //     System.out.println("inne i pos.col >> ");
        //     return checkColumns(position, -1);
        // }

        // if (position.row() + 2 > BOARD_SIZE && hitsChart[lastShot.row()][lastShot.col()] == ShotResult.HIT) {
        //     System.out.println("inne i pos.row >> ");
        //     return checkRows(position, -1);
        // }

        // if (hitsChart[position.row()][position.col() - 1] == null || hitsChart[position.row()][position.col() - 1] == ShotResult.HIT) {
        //     System.out.println("inne i col 1");
        //     return checkColumns(position, -1);
        // }
        // else if (hitsChart[position.row()][position.col() + 1] == null ) {
        //     System.out.println("inne i col 2");
        //     return checkColumns(position, 1);
        // }

        // if (hitsChart[position.row() - 1][position.col()] == null ) {
        //     System.out.println("inne i col 3");
        //     return checkRows(position, -1);
        // }
        // else {
        //     System.out.println("inne i col 4");
        //     return checkRows(position, 1);
        // }

                    // if (hits == 0) {
            //     // System.out.println("inne i hits == 0");
            //     // orgHitPos = position; 
            //     hitPos = position;
 
            // }
            //hitPos = position; 

            // hits ++; 
            
            // if (hits >= 3) {
            //     //doubleCheckRows(orgHitPos);
            //     hits = 0; 
            // } 



