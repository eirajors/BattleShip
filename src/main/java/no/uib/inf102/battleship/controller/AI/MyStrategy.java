package no.uib.inf102.battleship.controller.AI;

import java.util.HashMap;
import java.util.Map;

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
    private boolean wasHit; 
    private static int hits; 
    private static Map<CellPosition, Boolean> hitsMap;

    public  MyStrategy() {
        shotAt = new boolean[BOARD_SIZE][BOARD_SIZE];
        hitsMap = new HashMap<CellPosition, Boolean>(); 
        hits = 0;  
    }

    /**
     * Chooses the next position to shoot.
     *
     * @return the position to shoot at
     */
    @Override
    public CellPosition chooseShot() {

    //int hit = 0; 

        // gå hver tredje, hvis den registrerer hit, tar den alle rundt den før den går videre til neste
        // hvis forrige hits er 0 kaller den på funksjon for å gå videre, hvis den er mellom 0 og 3 så 
        // kaller den på funksjon for å skyte rundt 
        if (hits == 0) {
            return shootEveryThird(); 
        }


        return null;
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
        //wasHit = hit; 

        if (hit) {
            hitsMap.put(position, hit);
            hits ++; 
            if (hits >= 3) {
                hits = 0; 
                hitsMap.clear();
            } 
        }
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

    /**
     * shoots around a cellposition if a hit was found 
     * 
     * @param position the position where the original hit was
     * @param direction the direction in which to shoot
     */
    private CellPosition shootAround (CellPosition position, int direction) {

        // rekusivt kall til shootaround slik at alle blir truffet?
        // finner forrige posisjon lagt til i mapet som har hit true, og legger til +1 enten col eller row 
        CellPosition shootPos = new CellPosition(position.row(), position.col() + direction);

        return null; 
    }   
}