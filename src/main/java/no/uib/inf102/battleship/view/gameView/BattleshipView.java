package no.uib.inf102.battleship.view.gameView;

import no.uib.inf102.battleship.controller.BattleshipAIController;
import no.uib.inf102.battleship.controller.BattleshipHumanController;
import no.uib.inf102.battleship.controller.AI.HuntTargetStrategy;
import no.uib.inf102.battleship.model.BattleShipBoard;
import no.uib.inf102.battleship.model.GameState;
import no.uib.inf102.battleship.model.ship.Direction;
import no.uib.inf102.battleship.model.ship.Ship;
import no.uib.inf102.battleship.model.shot.ShotResult;
import no.uib.inf102.grid.CellPosition;

import javax.swing.*;
import java.awt.*;

public class BattleshipView extends JPanel {

    private static final int CELL_SIZE = 45;

    private final ViewableBattleshipModel model;
    private final BattleshipHumanController humanController;
    private final BattleshipAIController aiController;

    private final JPanel playerBoardPanel;
    private final JPanel opponentBoardPanel;

    private final JLabel statusLabel;

    private Direction selectedDirection = Direction.HORIZONTAL;

    public BattleshipView(ViewableBattleshipModel model) {

        this.model = model;

        humanController = new BattleshipHumanController(
                (no.uib.inf102.battleship.controller.ControllableBattleshipModel) model
        );

        aiController = new BattleshipAIController(
                (no.uib.inf102.battleship.controller.ControllableBattleshipModel) model,
                new HuntTargetStrategy()
        );

        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(20, 35, 60));

        setBorder(
                BorderFactory.createEmptyBorder(
                        20, 20, 20, 20
                )
        );

        // TITLE

        JLabel titleLabel = new JLabel(
                "BATTLESHIP",
                SwingConstants.CENTER
        );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 30)
        );

        titleLabel.setForeground(Color.WHITE);

        // STATUS

        statusLabel = new JLabel(
                "Place your ships",
                SwingConstants.CENTER
        );

        statusLabel.setFont(
                new Font("Arial", Font.BOLD, 18)
        );

        statusLabel.setForeground(Color.WHITE);

        JPanel topPanel = new JPanel(
                new BorderLayout(5, 10)
        );

        topPanel.setOpaque(false);

        topPanel.add(
                titleLabel,
                BorderLayout.NORTH
        );

        topPanel.add(
                statusLabel,
                BorderLayout.CENTER
        );

        add(
                topPanel,
                BorderLayout.NORTH
        );

        // BOARDS

        playerBoardPanel = createGridPanel();
        opponentBoardPanel = createGridPanel();

        JPanel boardsPanel = new JPanel(
                new GridLayout(1, 2, 35, 0)
        );

        boardsPanel.setOpaque(false);

        JPanel playerPanel = createBoardContainer(
                "YOUR BOARD",
                playerBoardPanel
        );

        JPanel opponentPanel = createBoardContainer(
                "OPPONENT BOARD",
                opponentBoardPanel
        );

        boardsPanel.add(playerPanel);
        boardsPanel.add(opponentPanel);

        add(
                boardsPanel,
                BorderLayout.CENTER
        );

        // CONTROLS

        JPanel controls = createControls();

        add(
                controls,
                BorderLayout.SOUTH
        );

        refresh();
    }

    private JPanel createGridPanel() {

        JPanel panel = new JPanel(
                new GridLayout(
                        BattleShipBoard.SIZE,
                        BattleShipBoard.SIZE,
                        2,
                        2
                )
        );

        panel.setBackground(
                new Color(10, 25, 45)
        );

        return panel;
    }

    private JPanel createBoardContainer(
            String title,
            JPanel boardPanel) {

        JPanel container = new JPanel(
                new BorderLayout(5, 8)
        );

        container.setBackground(
                new Color(30, 50, 80)
        );

        container.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                Color.WHITE,
                                2
                        ),
                        BorderFactory.createEmptyBorder(
                                10, 10, 10, 10
                        )
                )
        );

        JLabel label = new JLabel(
                title,
                SwingConstants.CENTER
        );

        label.setFont(
                new Font("Arial", Font.BOLD, 18)
        );

        label.setForeground(Color.WHITE);

        container.add(
                label,
                BorderLayout.NORTH
        );

        container.add(
                boardPanel,
                BorderLayout.CENTER
        );

        return container;
    }

    private JPanel createControls() {

        JPanel controls = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER,
                        15,
                        5
                )
        );

        controls.setOpaque(false);

        JLabel directionLabel = new JLabel(
                "Ship direction:"
        );

        directionLabel.setForeground(Color.WHITE);

        directionLabel.setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        JButton horizontalButton =
                createControlButton("Horizontal");

        JButton verticalButton =
                createControlButton("Vertical");

        horizontalButton.addActionListener(e -> {

            selectedDirection =
                    Direction.HORIZONTAL;

            statusLabel.setText(
                    "Direction selected: Horizontal"
            );
        });

        verticalButton.addActionListener(e -> {

            selectedDirection =
                    Direction.VERTICAL;

            statusLabel.setText(
                    "Direction selected: Vertical"
            );
        });

        controls.add(directionLabel);
        controls.add(horizontalButton);
        controls.add(verticalButton);

        return controls;
    }

    private JButton createControlButton(
            String text) {

        JButton button = new JButton(text);

        button.setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        button.setFocusPainted(false);

        button.setBackground(
                new Color(65, 145, 190)
        );

        button.setForeground(Color.WHITE);

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        8, 15, 8, 15
                )
        );

        return button;
    }

    private void refresh() {

        playerBoardPanel.removeAll();
        opponentBoardPanel.removeAll();

        createPlayerBoard();
        createOpponentBoard();

        statusLabel.setText(
                getStatusText()
        );

        playerBoardPanel.revalidate();
        playerBoardPanel.repaint();

        opponentBoardPanel.revalidate();
        opponentBoardPanel.repaint();
    }

    private void createPlayerBoard() {

        for (int row = 0;
             row < BattleShipBoard.SIZE;
             row++) {

            for (int col = 0;
                 col < BattleShipBoard.SIZE;
                 col++) {

                CellPosition position =
                        new CellPosition(row, col);

                JButton cell =
                        createCell();

                updatePlayerCell(
                        cell,
                        position
                );

                cell.addActionListener(
                        e -> placeShip(position)
                );

                playerBoardPanel.add(cell);
            }
        }
    }

    private void createOpponentBoard() {

        for (int row = 0;
             row < BattleShipBoard.SIZE;
             row++) {

            for (int col = 0;
                 col < BattleShipBoard.SIZE;
                 col++) {

                CellPosition position =
                        new CellPosition(row, col);

                JButton cell =
                        createCell();

                updateOpponentCell(
                        cell,
                        position
                );

                cell.addActionListener(
                        e -> shoot(position)
                );

                opponentBoardPanel.add(cell);
            }
        }
    }

    private JButton createCell() {

        JButton cell = new JButton();

        cell.setPreferredSize(
                new Dimension(
                        CELL_SIZE,
                        CELL_SIZE
                )
        );

        cell.setMinimumSize(
                new Dimension(
                        CELL_SIZE,
                        CELL_SIZE
                )
        );

        cell.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        cell.setMargin(
                new Insets(0, 0, 0, 0)
        );

        cell.setFocusable(false);

        cell.setBorderPainted(false);

        cell.setOpaque(true);

        return cell;
    }

    private void updatePlayerCell(
            JButton cell,
            CellPosition position) {

        ShotResult result =
                model.getPlayerBoard()
                        .getShotResult(position);

        // AI HIT

        if (result == ShotResult.HIT
                || result == ShotResult.SUNK) {

            cell.setText("X");

            cell.setBackground(
                    new Color(220, 70, 70)
            );

            cell.setForeground(Color.WHITE);

            return;
        }

        // AI MISS

        if (result == ShotResult.MISS) {

            cell.setText("•");

            cell.setBackground(
                    new Color(170, 220, 240)
            );

            cell.setForeground(
                    new Color(30, 70, 100)
            );

            return;
        }

        // PLAYER SHIP

        if (isShipPosition(position)) {

            cell.setText("■");

            cell.setBackground(
                    new Color(70, 75, 85)
            );

            cell.setForeground(Color.WHITE);

            return;
        }

        // WATER

        cell.setText("");

        cell.setBackground(
                new Color(60, 150, 200)
        );
    }

    private void updateOpponentCell(
            JButton cell,
            CellPosition position) {

        ShotResult result =
                model.getOpponentShotResult(
                        position
                );

        // HIT

        if (result == ShotResult.HIT
                || result == ShotResult.SUNK) {

            cell.setText("X");

            cell.setBackground(
                    new Color(220, 70, 70)
            );

            cell.setForeground(Color.WHITE);

            return;
        }

        // MISS

        if (result == ShotResult.MISS) {

            cell.setText("•");

            cell.setBackground(
                    new Color(170, 220, 240)
            );

            cell.setForeground(
                    new Color(30, 70, 100)
            );

            return;
        }

        // UNKNOWN ENEMY WATER

        cell.setText("");

        cell.setBackground(
                new Color(60, 150, 200)
        );
    }

    private boolean isShipPosition(
            CellPosition position) {

        for (Ship ship :
                model.getPlayerBoard().getShips()) {

            if (ship.contains(position)) {
                return true;
            }
        }

        return false;
    }

    private void placeShip(
            CellPosition position) {

        if (model.getGameState()
                != GameState.PLACING_SHIPS) {

            return;
        }

        boolean placed =
                humanController.placeShip(
                        position,
                        selectedDirection
                );

        if (!placed) {

            JOptionPane.showMessageDialog(
                    this,
                    "The ship cannot be placed there.",
                    "Invalid placement",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        refresh();
    }

    private void shoot(
            CellPosition position) {

        if (model.getGameState()
                != GameState.PLAYER_TURN) {

            return;
        }

        try {

            ShotResult result =
                    model.shootOpponent(position);

            showShotResult(result);

            refresh();

            if (!model.isGameOver()) {

                aiController.takeTurn();

                refresh();
            }

        } catch (IllegalStateException exception) {

            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Invalid move",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void showShotResult(
            ShotResult result) {

        switch (result) {

            case MISS:
                statusLabel.setText(
                        "Miss!"
                );
                break;

            case HIT:
                statusLabel.setText(
                        "Hit!"
                );
                break;

            case SUNK:
                statusLabel.setText(
                        "You sunk a ship!"
                );
                break;

            case ALREADY_SHOT:
                statusLabel.setText(
                        "You already fired there."
                );
                break;

            default:
                statusLabel.setText("");
        }
    }

    private String getStatusText() {

        GameState state =
                model.getGameState();

        if (state == GameState.PLACING_SHIPS) {

            return "Place your three ships";
        }

        if (state == GameState.PLAYER_TURN) {

            return "Your turn";
        }

        if (state == GameState.AI_TURN) {

            return "Enemy is thinking...";
        }

        if (state == GameState.GAME_OVER) {

            if (model.playerWon()) {

                return "YOU WON!";
            }

            return "YOU LOST!";
        }

        return "";
    }
}