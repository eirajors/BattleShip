package no.uib.inf102.battleship.view.gameSelect;

import javax.swing.*;
import java.awt.*;

public class GameModeSelection extends JPanel {

    /**
     * Creates the game mode selection screen.
     *
     * @param humanVsAI action to run when the player chooses Human vs AI
     * @param aiVsAI action to run when the player chooses AI vs AI
     */
    public GameModeSelection(Runnable humanVsAI, Runnable aiVsAI) {

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Title shown at the top of the screen.
        JLabel title = new JLabel("BATTLESHIP", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));

        // Text asking the player to choose a game mode.
        JLabel question = new JLabel("Choose game mode", SwingConstants.CENTER);
        question.setFont(new Font("Arial", Font.PLAIN, 20));

        // Buttons for the two available game modes.
        JButton humanVsAIButton = new JButton("Human vs AI");
        JButton aiVsAIButton = new JButton("AI vs AI");

        humanVsAIButton.setFont(new Font("Arial", Font.BOLD, 18));
        aiVsAIButton.setFont(new Font("Arial", Font.BOLD, 18));

        // Run the corresponding action when a button is pressed.
        humanVsAIButton.addActionListener(e -> humanVsAI.run());
        aiVsAIButton.addActionListener(e -> aiVsAI.run());

        // Panel containing the question and game mode buttons.
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);

        question.setAlignmentX(Component.CENTER_ALIGNMENT);
        humanVsAIButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        aiVsAIButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(question);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(humanVsAIButton);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(aiVsAIButton);

        // Add the title and the game mode selection to the main panel.
        add(title, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
}