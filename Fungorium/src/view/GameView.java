// In GameView.java
package Fungorium.src.view;

import Fungorium.src.model.*; // Assuming this imports Rovar, GombaTest, GombaFonal, etc.
import Fungorium.src.model.player.Gombasz;
import Fungorium.src.model.player.Player;
import Fungorium.src.model.player.Rovarasz;
import Fungorium.src.model.tekton.Tekton;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer; // Ensure this is imported if Tekton.getNeighbours() returns List
import javax.swing.*;

public class GameView extends JFrame implements Observer {
    private Game gameModel;

    private MapView mapView;
    private TektonPanel tektonPanel;
    private EntityPanel entityPanel;
    private GamePanel gamePanel;
    private ActionPanel actionPanel;
    private JLabel skipHintLabel;

    private static final int SIDE_PANEL_WIDTH = 230;

    // <<< YOUR EXISTING ENUMS ARE GOOD, ensure GOMBASZ_GROW_FONAL is in PendingAction >>>
    enum InteractionMode  {
        NORMAL,             // Default: control selected entity or select entities/tektons on map
        MAP_NAVIGATION,     // Freely navigate map focus from tekton to tekton using keys
        AWAITING_TARGET_TEKTON // An action is pending a neighbor tekton target via key
    }

    enum PendingAction {
        NONE,
        ROVAR_MOVE,
        GOMBASZ_SHOOT_SPORA,
        ROVAR_CUT_FONAL,
        GOMBASZ_GROW_FONAL
    }

    private InteractionMode currentInteractionMode;
    private PendingAction currentPendingAction;
    private Entity actionInitiator;        // For entity-initiated actions (Rovar, GombaTest)
    private Tekton sourceTektonForAction;  // For actions initiated from a selected Tekton (like Grow Fonal)


    private static final Map<Integer, Integer> neighborKeyToIndexMap = new HashMap<>();
    static {
        // Your mapping: P=5 (UL), O=4 (L), K=3 (LL), ,=2 (LR), .=1 (RR), ;=0 (R)
        // This implies a visual layout of neighbors for the keys.
        neighborKeyToIndexMap.put(KeyEvent.VK_1, 4);         // Top-Left (index 5)
        neighborKeyToIndexMap.put(KeyEvent.VK_2, 5);         // Top-Right 
        neighborKeyToIndexMap.put(KeyEvent.VK_3, 0);         // Right 
        neighborKeyToIndexMap.put(KeyEvent.VK_4, 1);         // Bottom-Right 
        neighborKeyToIndexMap.put(KeyEvent.VK_5, 2);         // Bottom-Left 
        neighborKeyToIndexMap.put(KeyEvent.VK_6, 3);         // Left 
        // Adjust indices if your Tekton.getNeighbours() has a different fixed directional order.
    }


    public GameView(Game gameModel) {
        this.gameModel = gameModel;
        this.gameModel.addObserver(this);

        currentInteractionMode = InteractionMode.NORMAL;
        currentPendingAction = PendingAction.NONE;
        actionInitiator = null;
        sourceTektonForAction = null;

        setTitle("Fungorium - Strategy Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(40, 40, 40));
        setContentPane(mainPanel);

        setSize(1280, 720);
        setResizable(false);

        // Pass 'this' to MapView if MapView needs to call methods on GameView (e.g. for mouse clicks)
        mapView = new MapView(gameModel);
        tektonPanel = new TektonPanel();
        entityPanel = new EntityPanel();
        gamePanel = new GamePanel();
        actionPanel = new ActionPanel();
        skipHintLabel = new JLabel("Hold N to Skip Turn, T to Toggle Map Navigation", SwingConstants.CENTER); // Updated hint
        skipHintLabel.setForeground(Color.LIGHT_GRAY);
        skipHintLabel.setFont(new Font("Arial", Font.BOLD, 14)); // Slightly smaller

        // --- Layout setup (Your existing good layout) ---
        JPanel leftContainer = new JPanel(); leftContainer.setOpaque(false); leftContainer.setLayout(new BoxLayout(leftContainer, BoxLayout.Y_AXIS)); leftContainer.setPreferredSize(new Dimension(SIDE_PANEL_WIDTH, 0));
        JPanel gamePanelWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER)); gamePanelWrapper.setOpaque(false); gamePanelWrapper.add(gamePanel); leftContainer.add(gamePanelWrapper);
        leftContainer.add(Box.createVerticalGlue());
        JPanel actionPanelWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER)); actionPanelWrapper.setOpaque(false); actionPanelWrapper.add(actionPanel); leftContainer.add(actionPanelWrapper);
        JPanel rightContainer = new JPanel(); rightContainer.setOpaque(false); rightContainer.setLayout(new BoxLayout(rightContainer, BoxLayout.Y_AXIS)); rightContainer.setPreferredSize(new Dimension(SIDE_PANEL_WIDTH, 0));
        JPanel tektonPanelWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER)); tektonPanelWrapper.setOpaque(false); tektonPanelWrapper.add(tektonPanel); rightContainer.add(tektonPanelWrapper);
        rightContainer.add(Box.createVerticalGlue());
        JPanel entityPanelWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER)); entityPanelWrapper.setOpaque(false); entityPanelWrapper.add(entityPanel); rightContainer.add(entityPanelWrapper);
        JPanel topHintPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); topHintPanel.setOpaque(false); topHintPanel.add(skipHintLabel);
        mainPanel.add(topHintPanel, BorderLayout.NORTH); mainPanel.add(leftContainer, BorderLayout.WEST); mainPanel.add(mapView, BorderLayout.CENTER); mainPanel.add(rightContainer, BorderLayout.EAST);

        addKeyListener(new KeyListener() {
            @Override public void keyPressed(KeyEvent e) { handleKeyPress(e.getKeyCode()); }
            @Override public void keyReleased(KeyEvent e) {}
            @Override public void keyTyped(KeyEvent e) {}
        });
        setFocusable(true);
        setVisible(true);
        updateView();
    }

    // <<< MODIFIED handleKeyPress to include all actions >>>
    private void handleKeyPress(int keyCode) {

        Player currentPlayer = gameModel.getCurrentPlayer();
        Entity selectedEntityByModel = gameModel.getSelectedEntity();
        Tekton selectedTektonByModel = gameModel.getSelectedTekton(); // Model's currently selected/focused Tekton

        if (keyCode == KeyEvent.VK_ESCAPE) {
            if (currentInteractionMode != InteractionMode.NORMAL) {
                resetInteractionModeAndNotify("Action cancelled.");
            }
            return;
        }

        // Toggle Map Navigation Mode with 'T' key
        if (keyCode == KeyEvent.VK_T) {
            if (currentInteractionMode == InteractionMode.NORMAL) {
                currentInteractionMode = InteractionMode.MAP_NAVIGATION;
                // Ensure a tekton is "selected" in the model for navigation to start from
                if (gameModel.getSelectedTekton() == null) {
                    Tekton initialFocus = getTektonOfEntity(selectedEntityByModel);
                    if (initialFocus == null && gameModel.getGameMap() != null && !gameModel.getGameMap().getTektonok().isEmpty()) {
                        initialFocus = gameModel.getGameMap().getTektonok().get(0);
                    }
                    gameModel.setSelectedTekton(initialFocus); // This will notify and MapView should center
                }
                updateActionPanelInstruction("MAP NAV: 1, 2, 3, 4, 5, 6 to move focus. T for entity control. G to Grow Fonal.");
            } else if (currentInteractionMode == InteractionMode.MAP_NAVIGATION) {
                currentInteractionMode = InteractionMode.NORMAL;
                gameModel.autoSelectFirstEntityForCurrentPlayer(); // Re-select active entity
                updateActionPanelInstruction("Entity Control Mode.");
            }
            // If AWAITING_TARGET_TEKTON, 'T' does nothing or could cancel (ESC is better for cancel)
            requestFocusInWindow();
            return;
        }

        switch (currentInteractionMode) {
            case NORMAL: // Player controls their selected entity or selects entities/tektons via MapView clicks
                if (currentPlayer instanceof Rovarasz) {
                    if (selectedEntityByModel instanceof Rovar rovar && rovar.getOwner() == currentPlayer) {
                        switch (keyCode) {
                            case KeyEvent.VK_M: // Move Rovar
                                this.actionInitiator = rovar;
                                this.currentPendingAction = PendingAction.ROVAR_MOVE;
                                this.currentInteractionMode = InteractionMode.AWAITING_TARGET_TEKTON;
                                updateActionPanelInstruction("MOVE (" + rovar.getID() + "): 1, 2, 3, 4, 5, 6 for target. (ESC)");
                                break;
                            case KeyEvent.VK_E: // Eat Spora
                                boolean eatSuccess = gameModel.playerAction_selectedRovarEatSpora();
                                updateActionPanelInstruction(eatSuccess ? "Rovar " + rovar.getID() + " ate spora." : "Eat spora failed for " + rovar.getID() + ".");
                                break;
                            case KeyEvent.VK_C: // Cut Fonal
                                this.actionInitiator = rovar;
                                this.currentPendingAction = PendingAction.ROVAR_CUT_FONAL;
                                this.currentInteractionMode = InteractionMode.AWAITING_TARGET_TEKTON;
                                updateActionPanelInstruction("CUT FONAL (" + rovar.getID() + "): 1, 2, 3, 4, 5, 6 for target fonal's other Tekton. (ESC)");
                                break;
                        }
                    } else if (keyCode == KeyEvent.VK_M || keyCode == KeyEvent.VK_E || keyCode == KeyEvent.VK_C) {
                        updateActionPanelInstruction("ROVARASZ: Select your Rovar first.");
                    }
                } else if (currentPlayer instanceof Gombasz) {
                    if (selectedEntityByModel instanceof GombaTest gTest && gTest.getOwner() == currentPlayer) {
                        switch (keyCode) {
                            case KeyEvent.VK_S: // Shoot Spora
                                this.actionInitiator = gTest;
                                this.currentPendingAction = PendingAction.GOMBASZ_SHOOT_SPORA;
                                this.currentInteractionMode = InteractionMode.AWAITING_TARGET_TEKTON;
                                updateActionPanelInstruction("SHOOT (" + gTest.getID() + "): 1, 2, 3, 4, 5, 6 for target. (ESC)");
                                break;
                            case KeyEvent.VK_U: // Upgrade GombaTest
                                boolean upgradeSuccess = gameModel.playerAction_selectedGombaTestUpgrade();
                                updateActionPanelInstruction(upgradeSuccess ? "GombaTest " + gTest.getID() + " upgraded." : "Upgrade failed for " + gTest.getID() + ".");
                                break;
                        }
                    } else if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_U) {
                        updateActionPanelInstruction("GOMBASZ: Select your GombaTest first.");
                    }
                    // 'G' for Grow Fonal is now primarily initiated from MAP_NAVIGATION mode
                    // or if you want it here too, ensure selectedTektonByModel is the intended source.
                }

                if (keyCode == KeyEvent.VK_N) { // Next Player
                    resetInteractionModeAndNotify("");
                    gameModel.nextPlayerTurn();
                }
                break;

            case MAP_NAVIGATION: // Freely navigate map focus using keys
                if (neighborKeyToIndexMap.containsKey(keyCode)) {
                    if (selectedTektonByModel != null) { // Navigate from current model-selected tekton
                        int neighborIndex = neighborKeyToIndexMap.get(keyCode);
                        if (selectedTektonByModel.getNeighbours() != null &&
                                neighborIndex >= 0 && neighborIndex < selectedTektonByModel.getNeighbours().size()) {
                            Tekton nextFocusedTekton = selectedTektonByModel.getNeighbours().get(neighborIndex);
                            gameModel.setSelectedTekton(nextFocusedTekton); // This notifies, MapView re-centers
                            updateActionPanelInstruction("MAP NAV: Focused " + nextFocusedTekton.getID() + ". (T for entity ctrl, G to Grow)");
                        } else {
                            updateActionPanelInstruction("No neighbor in that direction from " + selectedTektonByModel.getID() + ".");
                        }
                    } else {
                        updateActionPanelInstruction("No Tekton focused to navigate from. Click map or press T.");
                    }
                } else if (keyCode == KeyEvent.VK_G && currentPlayer instanceof Gombasz) { // Grow Fonal from currently focused Tekton
                    if (selectedTektonByModel != null) {
                        // Game.playerAction_growFonal will check if player can grow from this source.
                        this.sourceTektonForAction = selectedTektonByModel; // The focused tekton is the source
                        this.currentPendingAction = PendingAction.GOMBASZ_GROW_FONAL;
                        this.currentInteractionMode = InteractionMode.AWAITING_TARGET_TEKTON; // Now expect neighbor key for destination
                        updateActionPanelInstruction("GROW FONAL (from " + sourceTektonForAction.getID() + "): 1, 2, 3, 4, 5, 6 for target neighbor. (ESC)");
                    } else {
                        updateActionPanelInstruction("GOMBASZ: No Tekton focused on map to grow fonal from.");
                    }
                }
                break;

            case AWAITING_TARGET_TEKTON: // Waiting for P,O,K,L... to select a TARGET for a pending action
                if (neighborKeyToIndexMap.containsKey(keyCode)) {
                    int neighborIndex = neighborKeyToIndexMap.get(keyCode);
                    Tekton contextTektonForNeighborList = null;

                    if (currentPendingAction == PendingAction.GOMBASZ_GROW_FONAL) {
                        contextTektonForNeighborList = this.sourceTektonForAction;
                    } else if (actionInitiator instanceof Rovar) {
                        contextTektonForNeighborList = ((Rovar) actionInitiator).getTekton();
                    } else if (actionInitiator instanceof GombaTest) {
                        contextTektonForNeighborList = ((GombaTest) actionInitiator).getTekton();
                    }

                    if (contextTektonForNeighborList != null && contextTektonForNeighborList.getNeighbours() != null &&
                            neighborIndex >= 0 && neighborIndex < contextTektonForNeighborList.getNeighbours().size()) {
                        Tekton targetNeighborTekton = contextTektonForNeighborList.getNeighbours().get(neighborIndex);
                        processTektonTargetSelectedWithKey(targetNeighborTekton);
                    } else {
                        updateActionPanelInstruction("Invalid key or no such neighbor (" + neighborIndex + ") from " + (contextTektonForNeighborList!=null?contextTektonForNeighborList.getID():"Source") + ". Try again or ESC.");
                    }
                }
                break;
        }
        requestFocusInWindow();
    }

    private void processTektonTargetSelectedWithKey(Tekton targetNeighborTekton) {
        if (targetNeighborTekton == null) {
            resetInteractionModeAndNotify("Targeting error: Target is null.");
            return;
        }
        // actionInitiator or sourceTektonForAction should be non-null if currentPendingAction is not NONE

        boolean actionSuccess = false;
        String actionFeedback = "Action failed or was not applicable.";

        switch (currentPendingAction) {
            case ROVAR_MOVE:
                if (actionInitiator instanceof Rovar rovar) {
                    actionSuccess = gameModel.playerAction_moveRovar(rovar, targetNeighborTekton);
                    actionFeedback = actionSuccess ? "Rovar " + rovar.getID() + " moved." : "Move failed for " + rovar.getID() + ".";
                }
                break;
            case GOMBASZ_SHOOT_SPORA:
                if (actionInitiator instanceof GombaTest shooter) {
                    actionSuccess = gameModel.playerAction_shootSpora(targetNeighborTekton);
                    actionFeedback = actionSuccess ? "Spora shot from " + shooter.getID() + "." : "Shoot spora failed.";
                }
                break;
            case ROVAR_CUT_FONAL:
                if (actionInitiator instanceof Rovar rovar) {
                    Tekton rovarCurrentTekton = rovar.getTekton();
                    GombaFonal fonalToCut = findFonalBetween(rovarCurrentTekton, targetNeighborTekton);
                    if (fonalToCut != null) {
                        actionSuccess = gameModel.playerAction_cutFonal(rovar, fonalToCut); // Ensure this method exists in Game
                        actionFeedback = actionSuccess ? "Fonal to " + targetNeighborTekton.getID() + " cut." : "Cut fonal failed.";
                    } else {
                        actionFeedback = "No direct fonal to " + targetNeighborTekton.getID() + ".";
                    }
                }
                break;
            case GOMBASZ_GROW_FONAL:
                if (sourceTektonForAction != null) {
                    actionSuccess = gameModel.playerAction_growFonal(sourceTektonForAction, targetNeighborTekton); // Ensure Game.java has this
                    actionFeedback = actionSuccess ? "Fonal grown from " + sourceTektonForAction.getID() + "." : "Grow fonal failed from " + sourceTektonForAction.getID() + ".";
                } else {
                    actionFeedback = "Grow fonal failed: Source Tekton was not set.";
                    System.err.println("ERROR: processTektonTargetSelectedWithKey: sourceTektonForAction is null for GOMBASZ_GROW_FONAL");
                }
                break;
            case NONE: actionFeedback = "No pending action was set."; break;
            default: actionFeedback = "Unknown pending action: " + currentPendingAction; break;
        }
        System.out.println("DEBUG: " + actionFeedback);
        resetInteractionModeAndNotify(actionFeedback);
    }

    // Helper for finding fonal (ensure this is correctly implemented)
    private GombaFonal findFonalBetween(Tekton t1, Tekton t2) {
        if (t1 == null || t2 == null) return null;
        if (t1.getFonalak() != null) { // Prefer checking direct fonals of t1
            for (GombaFonal fonal : t1.getFonalak()) {
                if (fonal.getOtherEnd(t1) == t2 && fonal.isActive()) { // Check if active for cutting
                    return fonal;
                }
            }
        }
        // Fallback for safety if Tekton.getFonalak() is not comprehensive or if fonal not linked to both ends in its list
        if (gameModel !=null && gameModel.getAllGombaFonalak() != null) {
            for (GombaFonal fonal : gameModel.getAllGombaFonalak()) {
                if (((fonal.getSrc() == t1 && fonal.getDst() == t2) ||
                        (fonal.getSrc() == t2 && fonal.getDst() == t1)) && fonal.isActive()) {
                    return fonal;
                }
            }
        }
        return null;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == gameModel) {
            System.out.println("DEBUG: GameView received update from Game model. Event: " + arg);
            String eventKey = (arg instanceof String) ? (String) arg : "";
            if ((eventKey.equals("PLAYER_CHANGED") || eventKey.startsWith("ROUND_STARTED_")) &&
                    currentInteractionMode == InteractionMode.AWAITING_TARGET_TEKTON ) { // If player/round changes mid-targeting
                resetInteractionModeAndNotify("Turn ended; action cancelled.");
            }
            updateView();
        }
    }

    public void updateView() {
        System.out.println("DEBUG: GameView.updateView() called.");
        if (gameModel == null) return;

        if (gamePanel != null) gamePanel.updateContents(gameModel);
        if (mapView != null) mapView.updateMapView(gameModel); // MapView will center on gameModel.getSelectedTekton()
        //if (tektonPanel != null) tektonPanel.updateContents(gameModel.getSelectedTekton(), gameModel);
        if (entityPanel != null) entityPanel.updateContents(gameModel.getSelectedEntity(), gameModel);
        if (actionPanel != null && gameModel.getCurrentPlayer() != null) {
            if (currentInteractionMode == InteractionMode.NORMAL || currentInteractionMode == InteractionMode.MAP_NAVIGATION) {
                actionPanel.updateContents(gameModel.getCurrentPlayer(), gameModel.getSelectedEntity());
            }
        }
    }

    private void resetInteractionModeAndNotify(String finalMessage) {
        boolean modeWasAltered = (currentInteractionMode != InteractionMode.NORMAL && currentInteractionMode != InteractionMode.MAP_NAVIGATION);
        // When resetting from AWAITING_TARGET_TEKTON, decide if it should go to NORMAL or MAP_NAVIGATION
        // For simplicity, most actions will probably return to NORMAL.
        // If an action was initiated from MAP_NAVIGATION, you might want to return there.
        // For now, always reset to NORMAL from a pending action state.
        currentInteractionMode = InteractionMode.NORMAL; // Default reset target
        // If you want to return to MAP_NAVIGATION after a G grow fonal initiated from there,
        // you'd need to store the previous mode before entering AWAITING_TARGET_TEKTON.

        currentPendingAction = PendingAction.NONE;
        actionInitiator = null;
        sourceTektonForAction = null;

        if (actionPanel != null) {
            if (finalMessage != null && !finalMessage.isEmpty()) {
                actionPanel.setInstructionText(finalMessage);
            } else if (modeWasAltered) { // If we were in AWAITING_TARGET and no specific message, clear
                actionPanel.setInstructionText(" ");
            }
            // Refresh general actions as we are back to NORMAL
            if (gameModel != null && gameModel.getCurrentPlayer() != null) {
                actionPanel.updateContents(gameModel.getCurrentPlayer(), gameModel.getSelectedEntity());
            }
        }
        System.out.println("DEBUG: GameView: Mode reset. Feedback: " + finalMessage);
    }

    private void updateActionPanelInstruction(String message) {
        if (actionPanel != null) {
            actionPanel.setInstructionText(message);
        }
        System.out.println("UI INSTRUCTION: " + message);
    }

    // Helper to get Tekton from entity
    private Tekton getTektonOfEntity(Entity entity) {
        if (entity instanceof Rovar) return ((Rovar) entity).getTekton();
        if (entity instanceof GombaTest) return ((GombaTest) entity).getTekton();
        return null;
    }
}