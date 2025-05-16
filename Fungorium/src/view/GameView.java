// In GameView.java
package Fungorium.src.view;

import Fungorium.src.model.*;
import Fungorium.src.model.player.Gombasz;
import Fungorium.src.model.player.Player;
import Fungorium.src.model.player.Rovarasz;
import Fungorium.src.model.tekton.Tekton;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class GameView extends JFrame implements Observer {
    private Game gameModel;

    private MapView mapView;
    private TektonPanel tektonPanel;
    private EntityPanel entityPanel;
    private GamePanel gamePanel;
    private ActionPanel actionPanel; // <<< Ensure this is correctly initialized and ActionPanel.java exists >>>
    private JLabel skipHintLabel;

    private static final int SIDE_PANEL_WIDTH = 230;


     enum InteractionMode  {
         NORMAL,
         AWAITING_TARGET_TEKTON
     }

     enum PendingAction {
         NONE,
         ROVAR_MOVE,
         GOMBASZ_SHOOT_SPORA,
         ROVAR_CUT_FONAL
     }

    private InteractionMode currentInteractionMode; // Your existing field
    private PendingAction currentPendingAction;   // Your existing field
    private Entity actionInitiator;             // Your existing field

    // private Rovar rovarToMove = null; // This is now covered by actionInitiator when pendingAction is ROVAR_MOVE

    // Mapping for neighbor selection keys to neighbor index (0-5)
    private static final Map<Integer, Integer> neighborKeyToIndexMap = new HashMap<>();
    static {
        // Your chosen mapping
        neighborKeyToIndexMap.put(KeyEvent.VK_P, 5);
        neighborKeyToIndexMap.put(KeyEvent.VK_SEMICOLON, 0);
        neighborKeyToIndexMap.put(KeyEvent.VK_PERIOD, 1);
        neighborKeyToIndexMap.put(KeyEvent.VK_COMMA, 2);
        neighborKeyToIndexMap.put(KeyEvent.VK_K, 3);
        neighborKeyToIndexMap.put(KeyEvent.VK_O, 4);
    }


    public GameView(Game gameModel) {
        this.gameModel = gameModel;
        this.gameModel.addObserver(this);

        // Initialize UI state variables
        currentInteractionMode = InteractionMode.NORMAL;
        currentPendingAction = PendingAction.NONE;
        actionInitiator = null;

        setTitle("Fungorium - Strategy Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(40, 40, 40));
        setContentPane(mainPanel);

        setSize(1280, 720);
        setResizable(false);

        mapView = new MapView(gameModel);
        tektonPanel = new TektonPanel();
        entityPanel = new EntityPanel();
        gamePanel = new GamePanel();
        actionPanel = new ActionPanel(); // Make sure ActionPanel.java is implemented
        skipHintLabel = new JLabel("Hold n to Skip", SwingConstants.CENTER);
        skipHintLabel.setForeground(Color.LIGHT_GRAY);
        skipHintLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // --- Layout setup (Your existing layout) ---
        JPanel leftContainer = new JPanel();
        leftContainer.setOpaque(false);
        leftContainer.setLayout(new BoxLayout(leftContainer, BoxLayout.Y_AXIS));
        leftContainer.setPreferredSize(new Dimension(SIDE_PANEL_WIDTH, 0));
        JPanel gamePanelWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        gamePanelWrapper.setOpaque(false);
        gamePanelWrapper.add(gamePanel);
        leftContainer.add(gamePanelWrapper);
        leftContainer.add(Box.createVerticalGlue());
        JPanel actionPanelWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        actionPanelWrapper.setOpaque(false);
        actionPanelWrapper.add(actionPanel);
        leftContainer.add(actionPanelWrapper);

        JPanel rightContainer = new JPanel();
        rightContainer.setOpaque(false);
        rightContainer.setLayout(new BoxLayout(rightContainer, BoxLayout.Y_AXIS));
        rightContainer.setPreferredSize(new Dimension(SIDE_PANEL_WIDTH, 0));
        JPanel tektonPanelWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tektonPanelWrapper.setOpaque(false);
        tektonPanelWrapper.add(tektonPanel);
        rightContainer.add(tektonPanelWrapper);
        rightContainer.add(Box.createVerticalGlue());
        JPanel entityPanelWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        entityPanelWrapper.setOpaque(false);
        entityPanelWrapper.add(entityPanel);
        rightContainer.add(entityPanelWrapper);

        JPanel topHintPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topHintPanel.setOpaque(false);
        topHintPanel.add(skipHintLabel);

        mainPanel.add(topHintPanel, BorderLayout.NORTH);
        mainPanel.add(leftContainer, BorderLayout.WEST);
        mainPanel.add(mapView, BorderLayout.CENTER);
        mainPanel.add(rightContainer, BorderLayout.EAST);

        addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e.getKeyCode());
            }
            @Override
            public void keyReleased(KeyEvent e) {}
            @Override
            public void keyTyped(KeyEvent e) {}
        });

        setFocusable(true);
        setVisible(true);
        updateView();
    }

    // <<< START OF REFACTORED handleKeyPress FOR MOVE ROVAR & SHOOT SPORA >>>
    private void handleKeyPress(int keyCode) {

        Player currentPlayer = gameModel.getCurrentPlayer();
        Entity selectedEntityByModel = gameModel.getSelectedEntity();

        if (keyCode == KeyEvent.VK_ESCAPE) {
            if (currentInteractionMode != InteractionMode.NORMAL) {
                System.out.println("DEBUG: GameView: Action cancelled by ESCAPE.");
                resetInteractionModeAndNotify("Action cancelled.");
            }
            return;
        }

        switch (currentInteractionMode) {
            case NORMAL:
                if (currentPlayer instanceof Rovarasz) {
                    switch (keyCode) {
                        case KeyEvent.VK_M: // 'M' to initiate Move Rovar
                            if (selectedEntityByModel instanceof Rovar && selectedEntityByModel.getOwner() == currentPlayer) {
                                Rovar rovarToPrep = (Rovar) selectedEntityByModel;

                                this.actionInitiator = rovarToPrep;
                                this.currentPendingAction = PendingAction.ROVAR_MOVE;
                                this.currentInteractionMode = InteractionMode.AWAITING_TARGET_TEKTON;
                                String instruction = "MOVE (" + rovarToPrep.getID() + "): Use P,O,K,L,;,., or , for target. (ESC to cancel)";
                                updateActionPanelInstruction(instruction);
                            } else {
                                updateActionPanelInstruction("ROVARASZ: Select your Rovar to Move.");
                            }
                            break;
                        case KeyEvent.VK_E: // 'E' for Eat Spora (Direct action)
                            if (selectedEntityByModel instanceof Rovar && selectedEntityByModel.getOwner() == currentPlayer) {
                                Rovar rovarToEat = (Rovar) selectedEntityByModel;

                                boolean eatSuccess = gameModel.playerAction_selectedRovarEatSpora();
                                updateActionPanelInstruction(eatSuccess ? "Rovar " + rovarToEat.getID() + " ate spora." : "Eat spora failed.");
                            } else {
                                updateActionPanelInstruction("ROVARASZ: Select your Rovar to Eat Spora.");
                            }
                            break;
                        // TODO: case KeyEvent.VK_G: for Grow Fonal (might need AWAITING_TARGET_TEKTON mode)
                        case KeyEvent.VK_C: // Cut Fonal
                            if (selectedEntityByModel instanceof Rovar rovar && rovar.getOwner() == currentPlayer) {
                                this.actionInitiator = rovar;
                                this.currentPendingAction = PendingAction.ROVAR_CUT_FONAL;
                                this.currentInteractionMode = InteractionMode.AWAITING_TARGET_TEKTON; // Re-use for selecting neighbor Tekton whose fonal to cut
                                updateActionPanelInstruction("CUT FONAL (" + rovar.getID() + "): Use P,O,K,L,etc. to select neighbor Tekton (fonal to it). (ESC)");
                            } else {
                                updateActionPanelInstruction("ROVARASZ: Select your Rovar to Cut Fonal.");
                            }
                            break;
                    }
                } else if (currentPlayer instanceof Gombasz) {
                    switch (keyCode) {
                        case KeyEvent.VK_S: // 'S' to initiate Shoot Spora
                            if (selectedEntityByModel instanceof GombaTest && selectedEntityByModel.getOwner() == currentPlayer) {
                                GombaTest gombaTestShooter = (GombaTest) selectedEntityByModel;
                                // Optional quick check: e.g. gombaTestShooter.getShotCounter() > 0
                                // Model (GombaTest.shootSpora via Game.playerAction_shootSpora) does authoritative check.
                                this.actionInitiator = gombaTestShooter;
                                this.currentPendingAction = PendingAction.GOMBASZ_SHOOT_SPORA;
                                this.currentInteractionMode = InteractionMode.AWAITING_TARGET_TEKTON;
                                String instruction = "SHOOT (" + gombaTestShooter.getID() + "): Use P,O,K,L,;,., or , for target. (ESC to cancel)";
                                updateActionPanelInstruction(instruction);
                            } else {
                                updateActionPanelInstruction("GOMBASZ: Select your GombaTest to Shoot Spora.");
                            }
                            break;
                        case KeyEvent.VK_U: // 'U' for Upgrade GombaTest (Direct Action)
                            if (selectedEntityByModel instanceof GombaTest && selectedEntityByModel.getOwner() == currentPlayer) {
                                boolean upgradeSuccess = gameModel.playerAction_selectedGombaTestUpgrade(); // Model uses its selectedEntity
                                updateActionPanelInstruction(upgradeSuccess ? "GombaTest upgraded." : "Upgrade failed.");
                            } else {
                                updateActionPanelInstruction("GOMBASZ: Select your GombaTest to Upgrade.");
                            }
                            break;
                    }
                }

                if (keyCode == KeyEvent.VK_N) {
                    resetInteractionModeAndNotify("");
                    gameModel.nextPlayerTurn();
                }
                break; // End of case NORMAL

            case AWAITING_TARGET_TEKTON:
                if (actionInitiator != null && neighborKeyToIndexMap.containsKey(keyCode)) {
                    int neighborIndex = neighborKeyToIndexMap.get(keyCode);
                    Tekton sourceContextTekton = null;

                    if (actionInitiator instanceof Rovar) {
                        sourceContextTekton = ((Rovar) actionInitiator).getTekton();
                    } else if (actionInitiator instanceof GombaTest) {
                        sourceContextTekton = ((GombaTest) actionInitiator).getTekton();
                    }
                    // Add other instanceof checks if needed (e.g., if actionInitiator could be a Tekton itself)

                    if (sourceContextTekton != null && sourceContextTekton.getNeighbours() != null &&
                            neighborIndex >= 0 && neighborIndex < sourceContextTekton.getNeighbours().size()) {

                        Tekton targetTekton = sourceContextTekton.getNeighbours().get(neighborIndex);
                        processTektonTargetSelectedWithKey(targetTekton);
                    } else {
                        updateActionPanelInstruction("Invalid neighbor key or no such neighbor (" + neighborIndex + "). Try again or ESC.");
                    }
                }
                // Other keys are ignored in this mode (ESC handled at the top)
                break; // End of case AWAITING_TARGET_TEKTON
        }
        requestFocusInWindow();
    }
    // <<< END OF REFACTORED handleKeyPress METHOD >>>

    // <<< NEW OR MODIFIED: Method to process the target Tekton selected via key >>>
    private void processTektonTargetSelectedWithKey(Tekton targetTekton) {
        if (targetTekton == null || actionInitiator == null) {
            resetInteractionModeAndNotify("Targeting error.");
            return;
        }

        boolean actionSuccess = false;
        String actionFeedback = "Action could not be completed.";

        switch (currentPendingAction) {
            case ROVAR_MOVE:
                if (actionInitiator instanceof Rovar) {
                    Rovar rovar = (Rovar) actionInitiator;
                    actionSuccess = gameModel.playerAction_moveRovar(rovar, targetTekton);
                    actionFeedback = actionSuccess ? "Rovar " + rovar.getID() + " moved." : "Move failed for " + rovar.getID() + ".";
                }
                break;
            case GOMBASZ_SHOOT_SPORA:
                if (actionInitiator instanceof GombaTest) {
                    GombaTest shooter = (GombaTest) actionInitiator;
                    actionSuccess = gameModel.playerAction_shootSpora(targetTekton);
                    actionFeedback = actionSuccess ? "Spora shot from " + shooter.getID() + "." : "Shoot spora failed for " + shooter.getID() + ".";
                }
                break;
            case ROVAR_CUT_FONAL:
                if (actionInitiator instanceof Rovar rovar) {
                    Tekton rovarCurrentTekton = rovar.getTekton();
                    GombaFonal fonalToCut = rovarCurrentTekton.findFonalBetween(targetTekton);
                    if (fonalToCut != null) {
                        actionSuccess = gameModel.playerAction_cutFonal(rovar, fonalToCut);
                        actionFeedback = actionSuccess ? "Fonal to " + targetTekton.getID() + " cut." : "Cut fonal failed.";
                    } else {
                        actionFeedback = "No direct fonal found to cut towards " + targetTekton.getID() + ".";
                    }
                }
                break;
            case NONE:
                actionFeedback = "No pending action when target was selected.";
                break;
            default: // Should ideally not happen if currentPendingAction is always valid or NONE
                actionFeedback = "Unknown pending action: " + currentPendingAction;
                break;
        }
        System.out.println("DEBUG: " + actionFeedback);
        resetInteractionModeAndNotify(actionFeedback);
    }


    @Override
    public void update(Observable o, Object arg) {
        if (o == gameModel) {
            System.out.println("DEBUG: GameView received update from Game model. Event: " + arg);
            String eventKey = (arg instanceof String) ? (String) arg : "";

            // If a model event implies a UI interaction sequence is over, reset UI mode.
            // This is important if model can reject an initiated action after some processing.
            if (eventKey.startsWith("MOVE_ROVAR_CANNOT_ACT_") ||
                    eventKey.startsWith("MOVE_ROVAR_NO_VALID_TARGETS_") ||
                    eventKey.startsWith("SHOOT_SPORA_CANNOT_ACT_") || // Example, if Game.java sends such detailed events
                    eventKey.startsWith("PENDING_ACTION_CANCELLED_")) {
                if(currentInteractionMode != InteractionMode.NORMAL){ // Only reset if not already normal
                    resetInteractionModeAndNotify("Action cannot proceed or was cancelled by model.");
                }
            }
            // General update of all view components
            updateView();
        }
    }

    public void updateView() {
        System.out.println("DEBUG: GameView.updateView() called.");
        if (gameModel == null) return;

        if (gamePanel != null) {
            gamePanel.updateContents(gameModel);
        }
        if (mapView != null) {
            mapView.updateMapView(gameModel);
        }
        if (tektonPanel != null) {
            //tektonPanel.updateContents(gameModel.getSelectedTekton(), gameModel); // Assumes TektonPanel has this method
        }
        if (entityPanel != null) {
            entityPanel.updateContents(gameModel.getSelectedEntity(), gameModel); // Assumes EntityPanel has this method
        }
        if (actionPanel != null && gameModel.getCurrentPlayer() != null) {
            // Only update general actions list if in NORMAL mode.
            // Specific instructions are handled by updateActionPanelInstruction.
            if (currentInteractionMode == InteractionMode.NORMAL) {
                actionPanel.updateContents(gameModel.getCurrentPlayer(), gameModel.getSelectedEntity());
                // Clear any lingering instruction text when returning to normal and view updates.
                // This might be redundant if resetInteractionModeAndNotify already clears it.
                // actionPanel.setInstructionText(" ");
            }
        }
    }

    // <<< MODIFIED: Renamed your existing resetInteractionMode to this one that takes feedback >>>
    private void resetInteractionModeAndNotify(String finalMessage) {
        boolean modeWasAltered = (currentInteractionMode != InteractionMode.NORMAL);
        currentInteractionMode = InteractionMode.NORMAL;
        currentPendingAction = PendingAction.NONE;
        actionInitiator = null;

        if (actionPanel != null) {
            // Set final feedback message or clear if no message/mode wasn't altered needing a clear.
            if (finalMessage != null && !finalMessage.isEmpty()) {
                actionPanel.setInstructionText(finalMessage);
            } else if (modeWasAltered) { // If we were in a mode and now reset without specific feedback
                actionPanel.setInstructionText(" "); // Clear previous instruction
            }
            // After resetting mode, the call to updateView() will refresh ActionPanel's general actions.
        }
        System.out.println("DEBUG: GameView: View interaction mode reset to NORMAL. Feedback: " + finalMessage);
    }

    // <<< NEW (or ensure it's correctly implemented): Helper for ActionPanel instructions >>>
    private void updateActionPanelInstruction(String message) {
        if (actionPanel != null) {
            actionPanel.setInstructionText(message); // Ensure ActionPanel.java has this public method
        }
        System.out.println("UI INSTRUCTION: " + message);
    }

    // Your existing enums should be fine here:
    // enum InteractionMode { NORMAL, AWAITING_TARGET_TEKTON }
    // enum PendingAction { NONE, ROVAR_MOVE, GOMBASZ_SHOOT_SPORA }

    // main method should be in a separate App.java or if here, ensure it creates Game and GameView correctly.
}