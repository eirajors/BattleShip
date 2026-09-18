package no.uib.inf102.battleship;

import no.uib.inf102.battleship.model.BattleShipModel;
import no.uib.inf102.battleship.view.gameSelect.GameModeSelection;
import no.uib.inf102.battleship.view.gameView.BattleshipView;
import no.uib.inf102.battleship.view.gameView.AIBattleView;

import javax.swing.*;

public class BattleShipMain {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame("Battleship");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            showGameModeSelection(frame);

            frame.setSize(500, 400);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static void showGameModeSelection(JFrame frame) {

        GameModeSelection selection = new GameModeSelection(

            // Human vs AI
            () -> {
                BattleShipModel model = new BattleShipModel();
                BattleshipView view = new BattleshipView(model);

                frame.setContentPane(view);
                frame.pack();
                frame.setLocationRelativeTo(null);
            },

            // AI vs AI
            () -> {
                AIBattleView view = new AIBattleView();

                frame.setContentPane(view);
                frame.pack();
                frame.setLocationRelativeTo(null);
            }
        );

        frame.setContentPane(selection);
    }
}