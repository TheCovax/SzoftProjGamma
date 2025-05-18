package Fungorium.src.view;

import javax.swing.*;
import java.awt.*;

public class TektonPanel extends JPanel {
    public TektonPanel(){
        setBackground(new Color(220, 220, 220)); // 浅灰色背景
        setPreferredSize(new Dimension(200, 300)); // 给一个初始的建议大小

        JLabel placeholderLabel = new JLabel("TektonPanel Area");
        add(placeholderLabel);
    }
}
