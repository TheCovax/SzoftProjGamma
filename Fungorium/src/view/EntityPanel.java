package Fungorium.src.view;

import Fungorium.src.model.Entity; // Your base Entity class
import Fungorium.src.model.Rovar;   // Your Rovar class
import Fungorium.src.model.GombaTest; // Your GombaTest class
import Fungorium.src.model.Game;      // Your Game model class

import javax.swing.*;
import java.awt.*;

public class EntityPanel extends JPanel {

    private JLabel entityTypeLabel;
    private JLabel idLabel;
    private JLabel attribute1Label; // e.g., Speed / Level
    private JLabel attribute2Label; // e.g., Remaining Actions / Spores or Shots
    private JLabel attribute3Label; // e.g., Effect / -
    private JLabel attribute4Label; // e.g., Collected Nutrition / -

    public EntityPanel() {
        setBackground(new Color(245, 245, 220)); // Beige card background
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Stack labels vertically

        Font labelFont = new Font("Arial", Font.PLAIN, 12);
        Font boldFont = new Font("Arial", Font.BOLD, 14);

        entityTypeLabel = new JLabel("Entity: N/A");
        entityTypeLabel.setFont(boldFont);
        idLabel = new JLabel("ID: N/A");
        idLabel.setFont(labelFont);
        attribute1Label = new JLabel("Attr1: N/A");
        attribute1Label.setFont(labelFont);
        attribute2Label = new JLabel("Attr2: N/A");
        attribute2Label.setFont(labelFont);
        attribute3Label = new JLabel("Attr3: N/A");
        attribute3Label.setFont(labelFont);
        attribute4Label = new JLabel("Attr4: N/A");
        attribute4Label.setFont(labelFont);

        // Add labels with some spacing and left alignment
        add(entityTypeLabel);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(idLabel);
        add(Box.createRigidArea(new Dimension(0, 3)));
        add(attribute1Label);
        add(Box.createRigidArea(new Dimension(0, 3)));
        add(attribute2Label);
        add(Box.createRigidArea(new Dimension(0, 3)));
        add(attribute3Label);
        add(Box.createRigidArea(new Dimension(0, 3)));
        add(attribute4Label);

        // Align all labels to the left within the BoxLayout
        entityTypeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        idLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        attribute1Label.setAlignmentX(Component.LEFT_ALIGNMENT);
        attribute2Label.setAlignmentX(Component.LEFT_ALIGNMENT);
        attribute3Label.setAlignmentX(Component.LEFT_ALIGNMENT);
        attribute4Label.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Set preferred size based on the screenshot's appearance
        // This width should be less than or equal to SIDE_PANEL_WIDTH in GameView
        setPreferredSize(new Dimension(220, 150)); // Adjust as needed
    }

    public void updateContents(Entity selectedEntity, Game gameModel) {
        if (selectedEntity == null) {
            entityTypeLabel.setText("Entity: None Selected");
            idLabel.setText("ID: -");
            attribute1Label.setText(" "); // Clear other attributes or set to "-"
            attribute2Label.setText(" ");
            attribute3Label.setText(" ");
            attribute4Label.setText(" ");
            return;
        }

        idLabel.setText("ID: " + selectedEntity.getID());

        if (selectedEntity instanceof Rovar) {
            Rovar rovar = (Rovar) selectedEntity;
            entityTypeLabel.setText("Entity: Rovar");
            attribute1Label.setText("Speed: " + rovar.getSpeed());
            attribute2Label.setText("Remaining Act: " + rovar.getRemainingActions());
            // For 'Effect', you might need a method in Rovar or check its state
            String effect = "None";
            if (rovar.isParalyzed()) {
                effect = "PARALYZED (" + rovar.getDuration() + ")";
            } else if (rovar.getSpeed() < Rovar.DEFAULT_SPEED) { // Example for SLOW
                effect = "SLOW";
            } else if (rovar.getSpeed() > Rovar.DEFAULT_SPEED) { // Example for FAST
                effect = "FAST";
            }
            // TODO: Add logic in Rovar to get a string for current "effect" based on its state
            attribute3Label.setText("Effect: " + effect);
            attribute4Label.setText("Collected Nut.: " + rovar.getCollectedNutrition());

        } else if (selectedEntity instanceof GombaTest) {
            GombaTest gombaTest = (GombaTest) selectedEntity;
            entityTypeLabel.setText("Entity: GombaTest");
            attribute1Label.setText("Level: " + gombaTest.getLevel());
            if (gombaTest.getTekton() != null) {
                attribute2Label.setText("Tekton Spores: " + gombaTest.getTekton().getSporak().size());
            } else {
                attribute2Label.setText("Tekton Spores: N/A");
            }
            attribute3Label.setText("Shots Left: " + gombaTest.getShotCounter());
            attribute4Label.setText("Owner: " + (gombaTest.getOwner() != null ? gombaTest.getOwner().getName() : "None"));
        } else {
            // Handle other entity types or clear fields
            entityTypeLabel.setText("Entity: Unknown");
            attribute1Label.setText(" ");
            attribute2Label.setText(" ");
            attribute3Label.setText(" ");
            attribute4Label.setText(" ");
        }
    }
}