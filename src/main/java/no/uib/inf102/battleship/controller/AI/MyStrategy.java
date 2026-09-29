package no.uib.inf102.battleship.controller.AI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import no.uib.inf102.battleship.model.ship.Direction;
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
    private static int hits; 
    private static CellPosition hitPos; 
    private static CellPosition lastShot; 
    private static CellPosition orgHitPos; 


    public  MyStrategy() {
        shotAt = new boolean[BOARD_SIZE][BOARD_SIZE];
        hitsChart = new ShotResult[BOARD_SIZE][BOARD_SIZE]; 
        hits = 0;  
        MyStrategy.hitPos = null; 
        MyStrategy.lastShot = null; 

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
        if (hits == 0) {
            return shootEveryThird(); 
        }

        else {
            System.out.println("her kommer hitpoS:");
            System.out.println(hitPos);
            return shootAroundHitsChart(hitPos);
            // return shootAround(hitPos); 
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
            //isLooking = true; 
            System.out.println("inne i hit");
            hitsChart[position.row()][position.col()] = ShotResult.HIT; 

            if (hits == 0) {
                System.out.println("inne i hits == 0");
                orgHitPos = position; 
                hitPos = position;
 
            }
            hitPos = position; 

            hits ++; 
            
            if (hits >= 3) {
                hits = 0; 
            } 
        }
        else {
            System.out.println("endrer hitpos fra " + hitPos + " til " + orgHitPos);
            hitsChart[position.row()][position.col()] = ShotResult.MISS; 
            hitPos = orgHitPos; 
        }
        // lagrer dette shottet slik at man kan bruke det i neste shot for å beregne hvor man skal skyte
        lastShot = new CellPosition(position.row(), position.col());

    }

    // lagre neste man skal skyte i en next, regn deretter ut next. skyt next. om den er false lag en annen next

    // private CellPosition getNext(CellPosition currPos) {
    //     return new CellPosition(currPos.row(), currPos.col());
    // }

    private CellPosition shootAroundHitsChart (CellPosition position) {
        //sjekke kolonner først, hvis de ikke er skutt sjekk dem, om de er false gå og sjekk rader
        // skal gå til venstre helt til den treffer false -> lagre forrige posisjon i en curr funksjon? 

        System.out.println("inne i shoothitschart");
        if (position.col() - 1 < 0 && hitsChart[lastShot.row()][lastShot.col()] == ShotResult.HIT) {
            System.out.println("inne i pos.col < 0");
            return checkColumns(position, 1);
        }

        if (hitsChart[position.row()][position.col() - 1] == null && hitsChart[lastShot.row()][lastShot.col()] == ShotResult.HIT ) {
            System.out.println("inne i col 1");
            return checkColumns(position, -1);
        }
        else if (hitsChart[position.row()][position.col() + 1] == null ) {
            System.out.println("inne i col 2");
            return checkColumns(position, 1);
        }

        if (position.row() - 1 < 0 && hitsChart[lastShot.row()][lastShot.col()] == ShotResult.HIT)  {
            return checkRows(position, 1);
        }


        if (hitsChart[position.row() - 1][position.col()] == null ) {
            System.out.println("inne i col 3");
            return checkRows(position, -1);
        }
        else {
            System.out.println("inne i col 4");
            return checkRows(position, 1);
        }
        

    }


    private CellPosition checkColumns (CellPosition position, int direction) {
        System.out.println("inne i check columns");

        int row = position.row();

        System.out.println("row = position.row()");
        System.out.println(position);

        if (direction < 0) {
            for (int col = position.col(); col >= 0; col --) {
                if (!shotAt[row][col]) {
                    return new CellPosition(row, col);
                }
            }
        }
        else {
            for (int col = position.col(); col < BOARD_SIZE; col ++) {
                if (!shotAt[row][col]) {
                    return new CellPosition(row, col);
                }
            }
        }
        
        return null; 
    }

    private CellPosition checkRows(CellPosition position, int direction) {

        int col = position.col(); 

        if (direction < 0) {
            for (int row = position.row(); row >= 0; row --) {
                if (!shotAt[row][col]) {
                    return new CellPosition(row, col);
                }
            }
        }

        else {
            for (int row = position.row(); row < BOARD_SIZE; row ++) {
                if (!shotAt[row][col]) {
                    return new CellPosition(row, col);
                }
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