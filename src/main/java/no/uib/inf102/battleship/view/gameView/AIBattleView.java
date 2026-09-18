package no.uib.inf102.battleship.view.gameView;

import no.uib.inf102.battleship.controller.AI.AIShipPlacer;
import no.uib.inf102.battleship.controller.AI.HuntTargetStrategy;
import no.uib.inf102.battleship.controller.AI.IPlacer;
import no.uib.inf102.battleship.controller.AI.IStrategy;
import no.uib.inf102.battleship.controller.AI.MyPlacer;
import no.uib.inf102.battleship.controller.AI.MyStrategy;
import no.uib.inf102.battleship.model.BattleShipBoard;
import no.uib.inf102.battleship.model.ship.Ship;
import no.uib.inf102.battleship.model.shot.ShotResult;
import no.uib.inf102.grid.CellPosition;

import javax.swing.*;
import java.awt.*;

public class AIBattleView extends JPanel {

    private final BattleShipBoard board1;
    private final BattleShipBoard board2;

    private final IStrategy strategy1;
    private final IStrategy strategy2;

    private final JButton[][] board1Cells;
    private final JButton[][] board2Cells;

    private final JLabel statusLabel;
    private final JLabel ai1Info;
    private final JLabel ai2Info;

    private boolean ai1Turn;
    private boolean gameOver;

    private final Timer timer;

    /**
     * Creates the AI vs AI game view.
     *
     * AI 1 uses the provided baseline placement and shooting strategies.
     * AI 2 uses the student's placement and shooting strategies.
     */
    public AIBattleView() {

        board1 = new BattleShipBoard();
        board2 = new BattleShipBoard();

        // Create the shooting strategies for the two AIs.
        strategy1 = new HuntTargetStrategy();
        strategy2 = new MyStrategy();

        // Create the placement strategies for the two AIs.
        IPlacer placer1 = new AIShipPlacer();
        IPlacer placer2 = new MyPlacer();

        // Place the ships before the game starts.
        placer1.placeShips(board1);
        placer2.placeShips(board2);

        board1Cells = new JButton[BattleShipBoard.SIZE][BattleShipBoard.SIZE];
        board2Cells = new JButton[BattleShipBoard.SIZE][BattleShipBoard.SIZE];

        ai1Turn = true;
        gameOver = false;

        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(20, 35, 60));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title shown at the top of the window.
        JLabel titleLabel = new JLabel(
                "BATTLESHIP - AI VS AI",
                SwingConstants.CENTER
        );

        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);

        // Displays information about the current game state.
        statusLabel = new JLabel(
                "AI 1 is thinking...",
                SwingConstants.CENTER
        );

        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        statusLabel.setForeground(Color.WHITE);

        JPanel topPanel = new JPanel(new BorderLayout(5, 10));
        topPanel.setOpaque(false);

        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(statusLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Labels describing the two AI players.
        ai1Info = new JLabel(
                "AI 1 - Random's Board",
                SwingConstants.CENTER
        );

        ai2Info = new JLabel(
                "AI 2 - My Strategy Board",
                SwingConstants.CENTER
        );

        ai1Info.setFont(new Font("Arial", Font.BOLD, 18));
        ai2Info.setFont(new Font("Arial", Font.BOLD, 18));

        ai1Info.setForeground(Color.WHITE);
        ai2Info.setForeground(Color.WHITE);

        // Create a panel containing both game boards.
        JPanel boardsPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        boardsPanel.setOpaque(false);

        boardsPanel.add(createBoardPanel(
                ai1Info,
                board1,
                board1Cells
        ));

        boardsPanel.add(createBoardPanel(
                ai2Info,
                board2,
                board2Cells
        ));

        add(boardsPanel, BorderLayout.CENTER);

        // Automatically plays one turn every 700 milliseconds.
        timer = new Timer(700, e -> playTurn());
        timer.start();
    }

    /**
     * Creates the graphical representation of one Battleship board.
     *
     * @param titleLabel the label displayed above the board
     * @param board the board represented by the panel
     * @param cells the buttons used to display the board cells
     * @return a panel containing the board
     */
    private JPanel createBoardPanel(
            JLabel titleLabel,
            BattleShipBoard board,
            JButton[][] cells) {

        JPanel panel = new JPanel(new BorderLayout(5, 5));

        panel.setBackground(new Color(30, 50, 80));

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.WHITE, 2),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                )
        );

        JPanel gridPanel = new JPanel(
                new GridLayout(
                        BattleShipBoard.SIZE,
                        BattleShipBoard.SIZE,
                        2,
                        2
                )
        );

        gridPanel.setBackground(new Color(10, 25, 45));

        // Create one button for every cell on the board.
        for (int row = 0; row < BattleShipBoard.SIZE; row++) {
            for (int col = 0; col < BattleShipBoard.SIZE; col++) {

                JButton cell = new JButton();

                cell.setPreferredSize(new Dimension(52, 52));
                cell.setMinimumSize(new Dimension(52, 52));
                cell.setMaximumSize(new Dimension(52, 52));

                cell.setFont(
                        new Font("Arial", Font.BOLD, 22)
                );

                cell.setMargin(new Insets(0, 0, 0, 0));
                cell.setFocusable(false);
                cell.setBorderPainted(false);
                cell.setOpaque(true);

                CellPosition position =
                        new CellPosition(row, col);

                updateCell(
                        cell,
                        board,
                        position
                );

                cells[row][col] = cell;
                gridPanel.add(cell);
            }
        }

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(gridPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Plays one turn for the current AI.
     *
     * The AI chooses a position, the shot is performed on the opponent's
     * board, and the result is sent back to the AI strategy.
     */
    private void playTurn() {

        if (gameOver) {
            return;
        }

        if (ai1Turn) {

            // AI 1 chooses and performs a shot.
            CellPosition position =
                    strategy1.chooseShot();

            ShotResult result =
                    board2.shoot(position);

            boolean hit =
                    result == ShotResult.HIT
                            || result == ShotResult.SUNK;

            strategy1.registerShot(
                    position,
                    hit
            );

            statusLabel.setText(
                    "AI 1 shoots at "
                            + getCoordinate(position)
                            + " -> "
                            + result
            );

            refreshBoards();

            // Check whether AI 1 has sunk all ships.
            if (board2.allShipsSunk()) {

                gameOver = true;
                timer.stop();

                statusLabel.setText(
                        "AI 1 WINS!"
                );

                return;
            }

        } else {

            // AI 2 chooses and performs a shot.
            CellPosition position =
                    strategy2.chooseShot();

            ShotResult result =
                    board1.shoot(position);

            boolean hit =
                    result == ShotResult.HIT
                            || result == ShotResult.SUNK;

            strategy2.registerShot(
                    position,
                    hit
            );

            statusLabel.setText(
                    "AI 2 shoots at "
                            + getCoordinate(position)
                            + " -> "
                            + result
            );

            refreshBoards();

            // Check whether AI 2 has sunk all ships.
            if (board1.allShipsSunk()) {

                gameOver = true;
                timer.stop();

                statusLabel.setText(
                        "AI 2 WINS!"
                );

                return;
            }
        }

        // Switch to the other AI.
        ai1Turn = !ai1Turn;

        updateTurnIndicator();
    }

    /**
     * Updates the labels showing which strategy each AI uses.
     */
    private void updateTurnIndicator() {

        ai1Info.setText(
                "AI 1 - Random"
        );

        ai2Info.setText(
                "AI 2 - My Strategy"
        );
    }

    /**
     * Updates the graphical representation of both boards.
     */
    private void refreshBoards() {

        for (int row = 0; row < BattleShipBoard.SIZE; row++) {
            for (int col = 0; col < BattleShipBoard.SIZE; col++) {

                CellPosition position =
                        new CellPosition(row, col);

                updateCell(
                        board1Cells[row][col],
                        board1,
                        position
                );

                updateCell(
                        board2Cells[row][col],
                        board2,
                        position
                );
            }
        }

        revalidate();
        repaint();
    }

    /**
     * Updates one cell based on its current state.
     *
     * The cell can represent a hit, miss, ship, or empty water.
     *
     * @param cell the Swing button representing the cell
     * @param board the board containing the cell
     * @param position the position of the cell
     */
    private void updateCell(
            JButton cell,
            BattleShipBoard board,
            CellPosition position) {

        ShotResult result =
                board.getShotResult(position);

        cell.setText("");
        cell.setForeground(Color.BLACK);

        // A hit or sunk ship.
        if (result == ShotResult.HIT
                || result == ShotResult.SUNK) {

            cell.setBackground(
                    new Color(220, 70, 70)
            );

            cell.setForeground(Color.WHITE);
            cell.setText("X");

        // A missed shot.
        } else if (result == ShotResult.MISS) {

            cell.setBackground(
                    new Color(170, 220, 240)
            );

            cell.setForeground(
                    new Color(30, 70, 100)
            );

            cell.setText("•");

        // A ship that has not been hit.
        } else if (containsShip(board, position)) {

            cell.setBackground(
                    new Color(70, 75, 85)
            );

            cell.setForeground(Color.WHITE);
            cell.setText("■");

        // An empty cell.
        } else {

            cell.setBackground(
                    new Color(60, 150, 200)
            );
        }
    }

    /**
     * Checks whether a ship occupies the given position.
     *
     * @param board the board to search
     * @param position the position to check
     * @return true if a ship occupies the position
     */
    private boolean containsShip(
            BattleShipBoard board,
            CellPosition position) {

        for (Ship ship : board.getShips()) {
            if (ship.contains(position)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Converts a board position to a human-readable coordinate.
     *
     * For example, (0, 0) becomes A1 and (2, 4) becomes E3.
     *
     * @param position the board position
     * @return the position as a letter-number coordinate
     */
    private String getCoordinate(
            CellPosition position) {

        char column =
                (char) ('A' + position.col());

        int row =
                position.row() + 1;

        return column + String.valueOf(row);
    }
}