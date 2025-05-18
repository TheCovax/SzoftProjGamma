// In TektonView.java
package Fungorium.src.view;

import Fungorium.src.model.tekton.Tekton;
import Fungorium.src.model.GombaTest;
import Fungorium.src.model.Rovar;
import Fungorium.src.model.spora.Spora;
import Fungorium.src.model.tekton.*; // Import specific tekton types for instanceof checks

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.List;
import java.util.Queue;

public class TektonView {

    public static final int DEFAULT_HEX_SIZE = 50; // "Radius" of the hexagon (center to vertex)
    public static final int BORDER_THICKNESS = 2;

    /**
     * Draws a single Tekton and its contents using geometric shapes.
     *
     * @param g2d The Graphics2D context to draw on.
     * @param tekton The Tekton model object to draw. Can be null (draws nothing).
     * @param x The screen x-coordinate for the center of the Tekton.
     * @param y The screen y-coordinate for the center of the Tekton.
     * @param isSelected Whether this Tekton is currently selected.
     * @param isHovered Whether this Tekton is currently hovered over.
     */
    public static void drawTekton(Graphics2D g2d, Tekton tekton, int x, int y,
                                  boolean isSelected, boolean isHovered) {
        if (tekton == null) {
            // Optionally draw an "empty slot" indicator if desired
            // For now, if tekton is null, we draw nothing for that slot.
            return;
        }

        Path2D.Double hexagon = createHexagon(x, y, DEFAULT_HEX_SIZE);

        // 1. Draw Tekton Base (Hexagon Shape)
        g2d.setColor(getColorForTektonType(tekton));
        g2d.fill(hexagon);
        g2d.setColor(Color.DARK_GRAY); // Border for the hexagon
        g2d.setStroke(new BasicStroke(BORDER_THICKNESS));
        g2d.draw(hexagon);

        // Draw Tekton ID (small, for debugging or info)
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        FontMetrics fm = g2d.getFontMetrics();
        String idText = tekton.getID();
        g2d.drawString(idText, x - fm.stringWidth(idText) / 2, y - DEFAULT_HEX_SIZE + 12);


        // --- Draw contents on the Tekton ---
        // Positions are relative to the Tekton's center (x, y)

        // 2. Draw GombaTest (e.g., a Red Circle in the center)
        GombaTest gt = tekton.getGombatest();
        if (gt != null) {
            g2d.setColor(new Color(200, 0, 0)); // Dark Red
            int gtSize = 20;
            g2d.fillOval(x - gtSize / 2, y - gtSize / 2 - 5, gtSize, gtSize); // Slightly above center
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 10));
            g2d.drawString("GT", x - 6, y - 2); // Text on GombaTest
        }

        // 3. Draw Rovars (e.g., Blue Squares at the bottom)
        List<Rovar> rovars = tekton.getRovarok();
        if (rovars != null && !rovars.isEmpty()) {
            int rovarSize = 12;
            int rovarStartX = x - (rovars.size() * (rovarSize + 2) - 2) / 2; // Centered horizontally
            int rovarY = y + DEFAULT_HEX_SIZE - rovarSize - 5; // Near bottom edge

            for (int i = 0; i < rovars.size(); i++) {
                Rovar rovar = rovars.get(i);
                g2d.setColor(new Color(0, 0, 180)); // Dark Blue
                g2d.fillRect(rovarStartX + i * (rovarSize + 2), rovarY, rovarSize, rovarSize);
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.PLAIN, 9));
                g2d.drawString(rovar.getID().substring(0, Math.min(1, rovar.getID().length())), rovarStartX + i * (rovarSize + 2) + 4, rovarY + 10);
            }
        }

        // 4. Draw Sporas (e.g., Small Green Dots/Circle with count at the top)
        Queue<Spora> sporas = tekton.getSporak();
        if (sporas != null && !sporas.isEmpty()) {
            int sporaCount = sporas.size();
            int sporaDisplayX = x;
            int sporaDisplayY = y - DEFAULT_HEX_SIZE + 25; // Near top edge

            g2d.setColor(new Color(0, 150, 0)); // Dark Green
            g2d.fillOval(sporaDisplayX - 7, sporaDisplayY - 7, 14, 14); // Circle for spora area
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 10));
            String countStr = String.valueOf(sporaCount);
            g2d.drawString(countStr, sporaDisplayX - fm.stringWidth(countStr)/2, sporaDisplayY + 4);
        }

        // 5. Draw Selection and Hover Highlights
        Stroke defaultStroke = g2d.getStroke();
        if (isSelected) {
            g2d.setColor(new Color(255, 255, 0, 180)); // More opaque Yellow for selection
            g2d.setStroke(new BasicStroke(BORDER_THICKNESS + 2)); // Thicker border for selection
            g2d.draw(hexagon);
        } else if (isHovered) {
            g2d.setColor(new Color(255, 255, 255, 100)); // Semi-transparent white for hover
            g2d.setStroke(new BasicStroke(BORDER_THICKNESS + 1));
            g2d.draw(hexagon);
        }
        g2d.setStroke(defaultStroke); // Reset stroke
    }

    // Helper to create a hexagon shape (pointy top)
    public static Path2D.Double createHexagon(int centerX, int centerY, int size) {
        Path2D.Double path = new Path2D.Double();
        for (int i = 0; i < 6; i++) {
            double angle = Math.PI / 3.0 * i + Math.PI / 6.0; // Offset by 30 deg for pointy top
            double x = centerX + size * Math.cos(angle);
            double y = centerY + size * Math.sin(angle);
            if (i == 0) path.moveTo(x, y);
            else path.lineTo(x, y);
        }
        path.closePath();
        return path;
    }

    // Helper to get a color based on Tekton type
    private static Color getColorForTektonType(Tekton tekton) {
        if (tekton instanceof ElszigeteltTekton) return new Color(190, 190, 190); // Lighter Gray
        if (tekton instanceof HosztilisTekton) return new Color(255, 130, 130); // Lighter Red
        if (tekton instanceof KoparTekton) return new Color(200, 170, 130);    // Tan
        if (tekton instanceof TermekenyTekton) return new Color(130, 210, 130); // Lighter Green
        if (tekton instanceof StabilTekton) return new Color(160, 200, 220);   // Light Blue
        return Color.LIGHT_GRAY; // Default fallback
    }
}