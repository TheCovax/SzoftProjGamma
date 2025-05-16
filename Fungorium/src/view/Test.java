package Fungorium.src.view;

import Fungorium.src.model.Game;

import javax.swing.*;

public class Test {
    // main method to launch
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Game gameInstance = new Game(); // Create the model instance
            try {
                gameInstance.initializeGame(); // Initialize its state
                gameInstance.startGame();
            } catch (java.io.IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error initializing game data: " + e.getMessage(), "Initialization Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1); // Exit if game can't initialize
            }
            new GameView(gameInstance); // Create the view, passing the model
        });
    }
}
