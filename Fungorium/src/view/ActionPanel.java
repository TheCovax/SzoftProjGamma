// In ActionPanel.java
package Fungorium.src.view;

import Fungorium.src.model.Entity;
import Fungorium.src.model.Game; // Only needed if updateContents needs more context from Game
import Fungorium.src.model.player.Player;
import Fungorium.src.model.player.Gombasz;
import Fungorium.src.model.player.Rovarasz;
import Fungorium.src.model.Rovar;
import Fungorium.src.model.GombaTest;

import javax.swing.*;
import java.awt.*;

public class ActionPanel extends JPanel { // Ensure it extends JPanel

    private JLabel instructionLabel; // For messages like "Select target..." or action feedback.
    private JPanel availableActionsDisplayPanel; // To show "M: Move", "E: Eat" etc.
    private Font actionFont;
    private Font instructionFont;

    public ActionPanel() {
        setBackground(new Color(245, 245, 220)); // Beige card background (match other panels)
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 1), // Consistent border
                BorderFactory.createEmptyBorder(10, 15, 10, 15)    // Padding
        ));
        setLayout(new BorderLayout(0, 8)); // Use BorderLayout; vertical gap between instruction and actions.
        setPreferredSize(new Dimension(220, 200)); // Adjust height as needed for content

        actionFont = new Font("Arial", Font.PLAIN, 13);
        instructionFont = new Font("Arial", Font.ITALIC, 13);

        instructionLabel = new JLabel(" ", SwingConstants.CENTER); // Start with empty or default message
        instructionLabel.setFont(instructionFont);
        instructionLabel.setForeground(new Color(0, 100, 0)); // Dark Green for instructions
        add(instructionLabel, BorderLayout.NORTH); // Instruction text at the top

        availableActionsDisplayPanel = new JPanel();
        // Use BoxLayout for a vertical list of actions. Could also use GridLayout.
        availableActionsDisplayPanel.setLayout(new BoxLayout(availableActionsDisplayPanel, BoxLayout.Y_AXIS));
        availableActionsDisplayPanel.setOpaque(false); // Inherit ActionPanel's background
        // Add a JScrollPane if the list of actions might become too long
        // JScrollPane scrollPane = new JScrollPane(availableActionsDisplayPanel);
        // scrollPane.setOpaque(false);
        // scrollPane.getViewport().setOpaque(false);
        // scrollPane.setBorder(null);
        // add(scrollPane, BorderLayout.CENTER);
        add(availableActionsDisplayPanel, BorderLayout.CENTER); // Actions list in the center/main part

        updateContents(null, null); // Initialize with no specific player actions shown
    }

    /**
     * Updates the list of generally available actions based on the current player
     * and potentially the selected entity.
     * This is typically called when GameView's currentViewState is NORMAL.
     *
     * @param currentPlayer The player whose turn it is.
     * @param selectedEntity The currently selected entity (can be null).
     */
    public void updateContents(Player currentPlayer, Entity selectedEntity) {
        availableActionsDisplayPanel.removeAll(); // Clear previous action labels

        if (currentPlayer == null) {
            addStyledLabel("No active player.", availableActionsDisplayPanel);
            revalidateAndRepaintPanel();
            return;
        }

        // Common actions (could be applicable to both or general game functions)
        addStyledLabel("N: Next Player", availableActionsDisplayPanel);
        // addStyledLabel("R: Cycle Entity", availableActionsDisplayPanel); // If you implement this

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setForeground(Color.GRAY);
        availableActionsDisplayPanel.add(Box.createRigidArea(new Dimension(0,5)));
        availableActionsDisplayPanel.add(separator);
        availableActionsDisplayPanel.add(Box.createRigidArea(new Dimension(0,5)));


        if (currentPlayer instanceof Rovarasz) {
            addStyledLabel("ROVARASZ Actions:", availableActionsDisplayPanel, Font.BOLD);
            addStyledLabel("M: Move Rovar", availableActionsDisplayPanel);
            addStyledLabel("E: Eat Spora", availableActionsDisplayPanel);
            addStyledLabel("C: Cut Fonal", availableActionsDisplayPanel);
            // Highlight which are available if selectedEntity is a Rovar and canAct()
            if (selectedEntity instanceof Rovar && selectedEntity.getOwner() == currentPlayer) {
                // Could add visual cue (e.g. enable/disable, color change) based on rovar.canAct()
            }
        } else if (currentPlayer instanceof Gombasz) {
            addStyledLabel("GOMBASZ Actions:", availableActionsDisplayPanel, Font.BOLD);
            addStyledLabel("S: Shoot Spora", availableActionsDisplayPanel);
            addStyledLabel("U: Upgrade GombaTest", availableActionsDisplayPanel);
            addStyledLabel("G: Grow Fonal", availableActionsDisplayPanel);
            // Highlight based on selectedEntity if it's a GombaTest
        } else {
            addStyledLabel("Unknown player type.", availableActionsDisplayPanel);
        }

        revalidateAndRepaintPanel();
    }

    private void addStyledLabel(String text, Container parent) {
        addStyledLabel(text, parent, Font.PLAIN);
    }

    private void addStyledLabel(String text, Container parent, int style) {
        JLabel actionLabel = new JLabel(text);
        actionLabel.setFont(new Font(actionFont.getName(), style, actionFont.getSize()));
        actionLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // For BoxLayout
        parent.add(actionLabel);
    }


    /**
     * Sets specific instructional text, e.g., when awaiting target selection
     * or providing feedback on an action.
     * @param text The message to display. If null or empty, instruction is cleared.
     */
    public void setInstructionText(String text) {
        instructionLabel.setText((text == null || text.trim().isEmpty()) ? " " : text);
        // If instruction text is set, perhaps clear the general actions list or hide it?
        // For now, both will be visible.
    }

    private void revalidateAndRepaintPanel() {
        availableActionsDisplayPanel.revalidate();
        availableActionsDisplayPanel.repaint();
        revalidate(); // Revalidate the ActionPanel itself
        repaint();  // Repaint the ActionPanel itself
    }
}