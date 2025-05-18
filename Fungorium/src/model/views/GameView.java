package Fungorium.src.model.views;





import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import java.awt.*;

public class GameView extends JFrame {

    public GameView() {
        setTitle("Fungorium");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800); // képarány alapján
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Bal oldal
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.add(new GamePanel(), BorderLayout.NORTH);
        leftPanel.add(new ActionPanel(), BorderLayout.SOUTH);

        // Jobb oldal
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(new TektonPanel(), BorderLayout.NORTH);
        rightPanel.add(new EntityPanel(), BorderLayout.SOUTH);

        // Középső rész
        JPanel centerPanel = new TektonView();

        // Panelok hozzáadása az ablakhoz
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
        add(centerPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // Szegélyt generáló segédfüggvény
    private static TitledBorder createTitledBorder(String title) {
        return BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.DARK_GRAY),
            title,
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Monospaced", Font.BOLD, 18)
        );
    }

    static class GamePanel extends JPanel {
        public GamePanel() {
            setPreferredSize(new Dimension(250, 250));
            setBackground(new Color(255, 235, 205));
            setBorder(createTitledBorder("GamePanel"));
        }
    }

    static class ActionPanel extends JPanel {
        public ActionPanel() {
            setPreferredSize(new Dimension(250, 250));
            setBackground(new Color(255, 235, 205));
            setBorder(createTitledBorder("ActionPanel"));
        }
    }

    static class TektonPanel extends JPanel {
        public TektonPanel() {
            setPreferredSize(new Dimension(250, 250));
            setBackground(new Color(255, 235, 205));
            setBorder(createTitledBorder("TektonPanel"));
        }
    }

    static class EntityPanel extends JPanel {
        public EntityPanel() {
            setPreferredSize(new Dimension(250, 250));
            setBackground(new Color(255, 235, 205));
            setBorder(createTitledBorder("EntityPanel"));
        }
    }

    static class TektonView extends JPanel {
        public TektonView() {
            setBackground(Color.BLACK); // játékmező háttér
        }
    }
/*
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameView::new);
    }
*/


}
