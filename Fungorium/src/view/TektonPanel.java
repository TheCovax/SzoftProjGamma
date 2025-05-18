package Fungorium.src.view;

import javax.swing.*;
import java.awt.*;

public class TektonPanel extends JPanel {
    public TektonPanel(){
        setBackground(new Color(220, 220, 220));
        setPreferredSize(new Dimension(200, 300));

        JLabel placeholderLabel = new JLabel("TektonPanel Area");
        add(placeholderLabel);
    }
}
