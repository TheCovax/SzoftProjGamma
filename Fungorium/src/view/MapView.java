// In MapView.java
package Fungorium.src.view;

import Fungorium.src.model.Game;
import Fungorium.src.model.player.*;
import Fungorium.src.model.tekton.Tekton;
import Fungorium.src.model.GombaFonal; // For later fonal drawing
import Fungorium.src.model.Entity;
import Fungorium.src.model.Rovar;
import Fungorium.src.model.GombaTest;
import Fungorium.src.model.player.Player; // If needed for owner-specific visuals

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
// No need for Path2D here if TektonView handles its own shape drawing
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream; // For robust resource loading
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

public class MapView extends JPanel {
    private Game gameModel;

    // Constants for layout
    public static final int TEKTON_VISUAL_SIZE = 100; // Overall visual diameter/width for layout
    public static final double NEIGHBOR_DISTANCE_FACTOR = 2.0; // Multiplier for distance from center (adjust for overlap/spacing)
    // For hexes, sqrt(3) * size for edge to edge, or 2 * size for vertex to vertex approx.
    // If your images are ~100px, a distance of 100-120 might work.

    private HashMap<String, BufferedImage> allGameImages;
    private HashMap<Tekton, Point> tektonScreenPositions; // Stores calculated screen center points
    private Tekton hoveredTekton = null;

    // Predefined screen points for the center and 6 neighbor slots
    // These will be calculated relative to the MapView's center.
    private Point centerTektonScreenPos;
    private Point[] neighborScreenPos = new Point[6];


    public MapView(Game gameModel) {
        this.gameModel = gameModel;
        this.tektonScreenPositions = new HashMap<>();
        this.allGameImages = new HashMap<>();

        loadAllGraphicsResources();

        setBackground(new Color(30, 30, 30));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
//                if (!isGameEffectivelyOver()) {
//                    handleMouseClick(e.getPoint());
//                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
//                if (!isGameEffectivelyOver()) {
//                    handleMouseMove(e.getPoint());
//                }
            }
        });
    }

    private BufferedImage loadImage(String path) throws IOException {
        InputStream is = getClass().getResourceAsStream(path);
        if (is == null) {
            System.err.println("Warning: Could not find image resource at: " + path);
            return null; // Or throw exception
        }
        return ImageIO.read(is);
    }

    private void loadAllGraphicsResources() {
        try {
            // Tekton Images - replace with your actual Tekton class simple names
            allGameImages.put("StabilTekton", loadImage("/images/StabilTekton.png"));
            allGameImages.put("KoparTekton", loadImage("/images/KoparTekton.png"));
            allGameImages.put("ElszigeteltTekton", loadImage("/images/ElszigeteltTekton.png"));
            allGameImages.put("HosztilisTekton", loadImage("/images/HosztilisTekton.png"));
            allGameImages.put("TermekenyTekton", loadImage("/images/TermekenyTekton.png"));
            allGameImages.put("Tekton_DEFAULT", loadImage("/images/DefaultTekton.png"));

            // Entity Images
            allGameImages.put("GombaTest", loadImage("/images/GombaTest.png"));
            allGameImages.put("Rovar", loadImage("/images/Rovar.png"));
            allGameImages.put("Spora", loadImage("/images/Spora.png"));

            System.out.println("INFO: MapView graphics resources loaded (or attempted).");
        } catch (IOException e) {
            System.err.println("ERROR: IOException loading graphics for MapView: " + e.getMessage());
        }
    }

    public void updateMapView(Game gameModel) {
        this.gameModel = gameModel;
        // No need to call calculateTektonScreenPositions() every time if panel size is fixed.
        // Call it if the panel size changes or if the logical center of the map view changes.
        // For now, we'll calculate them once or when view size changes.
        // We do need to call it if the *selected* tekton changes to recenter.
        defineFixedScreenPositions(); // Define positions based on current MapView size
        calculateAndMapTektonsToSlots(); // Determine which tekton goes to which slot
        repaint();
    }

    /**
     * Defines the fixed screen positions for the center and 6 neighbor slots
     * based on the current size of the MapView panel.
     */
    private void defineFixedScreenPositions() {
        int panelCenterX = getWidth() / 2;
        int panelCenterY = getHeight() / 2;

        centerTektonScreenPos = new Point(panelCenterX, panelCenterY);

        // Distance from center to neighbors' centers. Adjust TEKTON_VISUAL_SIZE * factor.
        // If tekton images are 100x100, distance should be around 100 for hexes to touch, >100 for space.
        int neighborDistance = (int) (TEKTON_VISUAL_SIZE * NEIGHBOR_DISTANCE_FACTOR);
        if (neighborDistance < 80) neighborDistance = 80; // Minimum distance

        for (int i = 0; i < 6; i++) {
            // Hexagonal layout: 0 is right, then counter-clockwise or clockwise
            // Pointy-top hex: angles are 0, 60, 120, 180, 240, 300 degrees (0, PI/3, 2PI/3, ...)
            // Flat-top hex: angles are 30, 90, 150, 210, 270, 330 (PI/6, PI/2, 5PI/6, ...)
            // Let's use pointy-top arrangement (like screenshot implies neighbors are above/below and side-diagonal)
            // Angle 0 (i=0) to the right. Angle PI/3 (i=1) up-right.
            double angle = (Math.PI / 3.0) * i; // For 6 neighbors
            int nx = panelCenterX + (int) (neighborDistance * Math.cos(angle));
            int ny = panelCenterY + (int) (neighborDistance * Math.sin(angle));
            neighborScreenPos[i] = new Point(nx, ny);
        }
    }

    /**
     * Determines which Tekton model goes into the center slot and which actual neighbors
     * map to the 6 predefined neighbor slots. Populates tektonScreenPositions.
     */
    private void calculateAndMapTektonsToSlots() {
        tektonScreenPositions.clear(); // Clear previous mappings
        if (gameModel == null ) return;

        Tekton currentCenterModel = determineCenterTektonModel();
        if (currentCenterModel == null) return; // Nothing to draw if no center

        // Map the center tekton
        if (centerTektonScreenPos != null) {
            tektonScreenPositions.put(currentCenterModel, centerTektonScreenPos);
        }

        // Map its actual neighbors to the 6 predefined neighbor slots
        List<Tekton> actualNeighbors = currentCenterModel.getNeighbours();
        if (actualNeighbors != null) {
            for (int i = 0; i < 6; i++) { // Iterate through the 6 visual slots
                if (i < actualNeighbors.size()) {
                    Tekton neighborModel = actualNeighbors.get(i);
                    if (neighborScreenPos[i] != null) {
                        tektonScreenPositions.put(neighborModel, neighborScreenPos[i]);
                    }
                }
                // If actualNeighbors.size() < i, that slot remains empty (no Tekton model mapped)
            }
        }
    }

    private Tekton determineCenterTektonModel() {
        if (gameModel == null) return null;

        Tekton centerTekton = gameModel.getSelectedTekton();
        Entity selectedEntity = gameModel.getSelectedEntity();

        if (centerTekton == null && selectedEntity != null) {
            centerTekton = getTektonOfEntity(selectedEntity);
        }

        if (centerTekton == null && gameModel.getGameMap() != null &&
                gameModel.getGameMap().getTektonok() != null && !gameModel.getGameMap().getTektonok().isEmpty()) {
            // Fallback to current player's first entity's tekton if nothing else selected
            Player currentPlayer = gameModel.getCurrentPlayer();
            if (currentPlayer != null) {
                Entity firstEntity = null;
                if (currentPlayer instanceof Rovarasz && !((Rovarasz) currentPlayer).getRovarok().isEmpty()) {
                    firstEntity = ((Rovarasz) currentPlayer).getRovarok().get(0);
                } else if (currentPlayer instanceof Gombasz && !((Gombasz) currentPlayer).getGombak().isEmpty()) {
                    firstEntity = ((Gombasz) currentPlayer).getGombak().get(0);
                }
                if (firstEntity != null) centerTekton = getTektonOfEntity(firstEntity);
            }
            if (centerTekton == null) centerTekton = gameModel.getGameMap().getTektonok().get(0);
        }
        return centerTekton;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (gameModel == null || centerTektonScreenPos == null) {
            g2d.setColor(Color.WHITE);
            String message = "Loading Map or Game Not Started...";
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(message, (getWidth() - fm.stringWidth(message)) / 2, getHeight() / 2);
            return;
        }

        // Draw GombaFonals (needs to know which tektons are displayed and their positions)
        drawGombaFonals(g2d);


        // Draw the center Tekton
        Tekton centerTektonModel = findTektonAtScreenPos(centerTektonScreenPos); // Find which model is at center slot
        if (centerTektonModel != null) {
            boolean isSelected = isTektonEffectivelySelected(centerTektonModel);
            boolean isHovered = (centerTektonModel == hoveredTekton);
            TektonView.drawTekton(g2d, centerTektonModel, centerTektonScreenPos.x, centerTektonScreenPos.y, isSelected, isHovered);
        }


        // Draw the 6 neighbor Tektons
        for (int i = 0; i < 6; i++) {
            Point neighborPos = neighborScreenPos[i];
            if (neighborPos != null) {
                Tekton neighborModel = findTektonAtScreenPos(neighborPos); // Find which model is at this neighbor slot
                if (neighborModel != null) { // Only draw if a Tekton model is mapped to this slot
                    boolean isSelected = isTektonEffectivelySelected(neighborModel);
                    boolean isHovered = (neighborModel == hoveredTekton);
                    TektonView.drawTekton(g2d, neighborModel, neighborPos.x, neighborPos.y, isSelected, isHovered);
                } else {
                    // Optionally draw an empty slot placeholder if you want
                    // g2d.setColor(Color.DARK_GRAY.brighter());
                    // g2d.draw(TektonView.createHexagon(neighborPos.x, neighborPos.y, TektonView.DEFAULT_HEX_SIZE - 10));
                }
            }
        }
    }

    private Tekton findTektonAtScreenPos(Point screenPos) {
        if (screenPos == null) return null;
        for (java.util.Map.Entry<Tekton, Point> entry : tektonScreenPositions.entrySet()) {
            if (entry.getValue().equals(screenPos)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private boolean isTektonEffectivelySelected(Tekton tektonToCheck) {
        if (tektonToCheck == null || gameModel == null) return false;
        if (tektonToCheck == gameModel.getSelectedTekton()) return true;
        Entity selectedEntity = gameModel.getSelectedEntity();
        if (selectedEntity != null && getTektonOfEntity(selectedEntity) == tektonToCheck) return true;
        return false;
    }


    private void drawGombaFonals(Graphics2D g2d) {
        if (gameModel == null || gameModel.getAllGombaFonalak() == null || tektonScreenPositions.isEmpty()) return;

        Tekton centerModel = findTektonAtScreenPos(centerTektonScreenPos);
        if (centerModel == null) return; // No center tekton, no fonals from it to draw here

        List<GombaFonal> fonals = centerModel.getFonalak(); // Get fonals specifically for the center tekton
        // Or iterate all fonals and check if both ends are displayed
        if (fonals == null) return;

        for (GombaFonal fonal : fonals) {
            Tekton srcTekton = fonal.getSrc();
            Tekton dstTekton = fonal.getDst();

            Point srcPos = tektonScreenPositions.get(srcTekton);
            Point dstPos = tektonScreenPositions.get(dstTekton);

            if (srcPos != null && dstPos != null) { // Only draw if both ends are currently displayed
                // Use fonal.getState() to set color/stroke
                switch (fonal.getState()) {
                    case ACTIVE:
                        g2d.setColor(new Color(0, 200, 0, 200)); // Vibrant Green
                        g2d.setStroke(new BasicStroke(5));
                        break;
                    case GROWING:
                        g2d.setColor(new Color(255, 255, 0, 180)); // Yellow
                        g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{8, 8}, 0));
                        break;
                    case CUT:
                        g2d.setColor(new Color(100, 100, 100, 150)); // Dark Gray
                        g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{4, 4}, 0));
                        break;
                }
                g2d.drawLine(srcPos.x, srcPos.y, dstPos.x, dstPos.y);
            }
        }
        g2d.setStroke(new BasicStroke(1)); // Reset stroke
    }

    private void handleMouseClick(Point clickPoint) {
        if (gameModel == null) return;
        Tekton clickedTektonModel = findClickedTektonModel(clickPoint);

        if (clickedTektonModel != null) {
            gameModel.setSelectedTekton(clickedTektonModel);
            // You might want to also check if an entity icon on the tekton was clicked
            // for more precise entity selection. For now, clicking tekton selects tekton.
        } else {
            // Clicked empty space
            // gameModel.setSelectedTekton(null); // Optionally deselect if clicking empty space
            // gameModel.setSelectedEntity(null);
        }
    }

    /*
    private void handleMouseMove(Point movePoint) {
        if (gameModel == null) return;
        Tekton currentHover = findClickedTektonModel(movePoint); // find which tekton the mouse is over
        if (hoveredTekton != currentHover) {
            hoveredTekton = currentHover;
            // To update TektonPanel on hover:
            // Option 1: GameView is mediator (MapView calls gameView.updateTektonPanelForHover(hoveredTekton))
            // Option 2: Model holds hoveredTekton (gameModel.setHoveredTektonByView(hoveredTekton))
            // For now, just repaint MapView to show its own hover highlight.
            // The TektonPanel will only update based on *selected* Tekton via the normal observer pattern.
            if (gameModel.getTektonPanel() != null && gameModel.getTektonPanel() instanceof TektonPanel) { // Hypothetical direct update (not strict MVC)
                ((TektonPanel) gameModel.getTektonPanel()).updateContents(hoveredTekton, gameModel);
            } else {
                repaint(); // Repaint to show hover effect on map
            }
        }
    }*/

    /**
     * Finds which Tekton model (if any) is at the given screen point.
     * This iterates through the Tektons that have defined screen positions.
     */
    private Tekton findClickedTektonModel(Point p) {
        Tekton clicked = null;
        double closestDistanceSq = Double.MAX_VALUE;

        // Check center tekton slot
        Tekton centerModel = findTektonAtScreenPos(centerTektonScreenPos);
        if (centerModel != null && isPointOnTekton(p, centerTektonScreenPos, centerModel)) {
            return centerModel; // Exact hit on center
        }
        if(centerModel != null) { // Check distance to center even if not exact hit first
            double distSq = centerTektonScreenPos.distanceSq(p);
            if (distSq < (TEKTON_VISUAL_SIZE * TEKTON_VISUAL_SIZE * 0.7)) { // Within a radius
                closestDistanceSq = distSq;
                clicked = centerModel;
            }
        }


        // Check neighbor tekton slots
        for (int i = 0; i < 6; i++) {
            if (neighborScreenPos[i] == null) continue;
            Tekton neighborModel = findTektonAtScreenPos(neighborScreenPos[i]);
            if (neighborModel != null && isPointOnTekton(p, neighborScreenPos[i], neighborModel)) {
                return neighborModel; // Exact hit on a neighbor
            }
            if(neighborModel != null){ // Check distance to this neighbor
                double distSq = neighborScreenPos[i].distanceSq(p);
                if (distSq < (TEKTON_VISUAL_SIZE * TEKTON_VISUAL_SIZE * 0.7) && distSq < closestDistanceSq) {
                    closestDistanceSq = distSq;
                    clicked = neighborModel;
                }
            }
        }
        return clicked; // Return the closest one if no exact hit, or null
    }

    /**
     * Helper to check if a screen point is within the bounds of a drawn Tekton.
     * Uses the TektonView's hexagon shape for collision.
     */
    private boolean isPointOnTekton(Point screenPoint, Point tektonCenterPos, Tekton tektonModel) {
        if (tektonCenterPos == null || tektonModel == null) return false;

        // Use the same shape TektonView would draw for click detection
        BufferedImage tektonImg = allGameImages.get(tektonModel.getClass().getSimpleName());
        if (tektonImg == null) tektonImg = allGameImages.get("Tekton_DEFAULT");

        if (tektonImg != null) {
            Rectangle bounds = new Rectangle(
                    tektonCenterPos.x - tektonImg.getWidth() / 2,
                    tektonCenterPos.y - tektonImg.getHeight() / 2,
                    tektonImg.getWidth(),
                    tektonImg.getHeight()
            );
            return bounds.contains(screenPoint);
        } else {
            // Fallback to hexagon shape if no image
            Path2D.Double hexagonShape = TektonView.createHexagon(tektonCenterPos.x, tektonCenterPos.y, TektonView.DEFAULT_HEX_SIZE);
            return hexagonShape.contains(screenPoint);
        }
    }


    private Tekton getTektonOfEntity(Entity entity) {
        if (entity instanceof Rovar) return ((Rovar) entity).getTekton();
        if (entity instanceof GombaTest) return ((GombaTest) entity).getTekton();
        return null;
    }

    //private boolean isGameEffectivelyOver() { return gameModel == null || gameModel.isGameOver(); }
}