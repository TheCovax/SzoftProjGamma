package Fungorium.src.view;

import Fungorium.src.model.Game;
import Fungorium.src.model.player.Player;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private JLabel titleLabel;
    private JLabel roundLabel;
    private JLabel playerLabel;
    private JLabel scoreLabel;

    public GamePanel() {
        setBackground(new Color(245, 245, 220));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        titleLabel = new JLabel("GamePanel");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // Changed to LEFT for consistency with data

        roundLabel = new JLabel("ROUNDS: -");
        playerLabel = new JLabel("Current Player: -");
        scoreLabel = new JLabel("Points: -");

        // Align data labels to the left
        roundLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        playerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        scoreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(roundLabel);
        add(playerLabel);
        add(scoreLabel);

        setPreferredSize(new Dimension(220, 120));
    }

    // Method to update the panel's content from the Game model
    public void updateContents(Game gameModel) {
        if (gameModel == null) {
            roundLabel.setText("ROUNDS: N/A");
            playerLabel.setText("Current Player: N/A");
            scoreLabel.setText("Points: N/A");
            return;
        }

        roundLabel.setText("ROUNDS: " + gameModel.getRoundCounter());
        Player currentPlayer = gameModel.getCurrentPlayer();
        if (currentPlayer != null) {
            playerLabel.setText("Current Player: " + currentPlayer.getName());
            scoreLabel.setText(currentPlayer.getClass().getSimpleName() + " Score: " + currentPlayer.getScore()); // Clarify score type
        } else {
            playerLabel.setText("Current Player: None");
            scoreLabel.setText("Score: N/A");
        }
    }
}