package Fungorium.src.model;

import Fungorium.src.model.player.Gombasz;
import Fungorium.src.model.player.Player;
import Fungorium.src.model.player.Rovarasz;
import Fungorium.src.model.spora.*;
import Fungorium.src.model.tekton.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Game extends Observable {
    private Scanner scanner;

    // Game constants
    private int roundCounter;
    private static final int MAX_ROUNDS = 15;
    private static final int WINNING_GOMBATEST_COUNT = 10;
    private static final int WINNING_SPORA_SCORE = 30;

    // Map which holds all tektons
    public Map map;

    private List<Player> players;
    private int currentPlayerIndex = 0;

    // List of all entities
    private List<GombaTest> gombaTestek;
    private List<Rovar> rovarok;
    private List<GombaFonal> gombaFonalak;
    private List<Entity> entities;

    private boolean testingMode;

    private Tekton selectedTekton;
    private Entity selectedEntity;

    // Constructor
    public Game() {
        // Initialize variables
        map = new Map();
        players = new ArrayList<>();
        gombaTestek = new ArrayList<>();
        rovarok = new ArrayList<>();
        gombaFonalak = new ArrayList<>();
        entities = new ArrayList<>();
        scanner = new Scanner(System.in);
        testingMode = false;
    }

    public void startGame() {
        if (players.isEmpty()) {
            System.err.println("Cannot start game: No players initialized.");
            return;
        }


        roundCounter = 1; // First round is 1
        currentPlayerIndex = 0; // Start with the first player
        autoSelectFirstEntityForCurrentPlayer(); // Auto-select for starting player

        System.out.println("DEBUG: Game Started! Round: " + roundCounter + " Player: " + getCurrentPlayer().getName());
        notifyStateChange("GAME_STARTED_PLAYER_" + getCurrentPlayer().getID());
    }

    /**
     * Initializes the game state: players, map, entities.
     * Called once at the start.
     *
     * @throws IOException if file loading fails.
     */
    public void initializeGame() throws IOException {
        System.out.println("DEBUG: Initializing Game");

        initPlayers(); // Initialize players
        // Load Game map from file
        map.loadMap("./SzoftProjGamma/mapsave.txt");
        loadEntitiesFromFile("./SzoftProjGamma/mapsave.txt");

        // Synchronize entities from Player class
        populateCollections();
    }

    /**
     * Initializes a default set of players.
     * Names are hardcoded; could be made dynamic later.
     */
    public void initPlayers() {
        // Create Players
        // TODO. set player nam on GUI
        Gombasz p1 = new Gombasz("GB1");
        p1.setName("Gombasz Alpha"); // REFINED: Set placeholder names directly
        Gombasz p2 = new Gombasz("GB2");
        p2.setName("Gombasz Beta");
        Rovarasz p3 = new Rovarasz("RS3");
        p3.setName("Rovarasz Gamma");
        Rovarasz p4 = new Rovarasz("RS4");
        p4.setName("Rovarasz Delta");


        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);
    }

    /**
     * Advances to the next player's turn and notifies observers.
     */
    public int nextPlayerTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

        if (currentPlayerIndex == 0)
            endRound();

        System.out.println("DEBUG: Advancing to next player: " + getCurrentPlayer().getName());

        autoSelectFirstEntityForCurrentPlayer();
        notifyStateChange("PLAYER_CHANGED");
        return currentPlayerIndex;
    }


    private void endRound() {
        roundCounter++;  // Increase after all players played in round
        System.out.println("DEBUG: --- Round " + roundCounter + " Ended ---"); // Keep for debug

        populateCollections();

        // Update all entities using the consolidated list
        // Make sure allEntities is comprehensive and up-to-date
        for (Entity entity : entities) {
            entity.update(); // Call each entity's own update logic
        }

        // Update all tektons (e.g., for splitting)
        for (Tekton tekton : map.getTektonok()) {
            tekton.update(testingMode);
        }

        notifyStateChange("ROUND_ENDED");
        checkGameEnd();
    }

    /**
     * Selects the next controllable entity for the current player.
     * If a Rovar is selected, it cycles through the current player's Rovars.
     * If a GombaTest is selected, it cycles through the current player's GombaTests.
     * If no entity or a different type of entity is selected, it selects the first
     * controllable entity of the current player's type.
     */
    public void selectNextControllableEntityForCurrentPlayer() {

        Player currentPlayer = getCurrentPlayer();
        Entity currentSelected = getSelectedEntity(); // Get what's currently selected in the model
        Entity nextEntityToSelect = null;

        if (currentPlayer instanceof Rovarasz rovarasz) {
            List<Rovar> playerRovars = rovarasz.getRovarok(); // Assumes Rovarasz has getRovarok()
            if (playerRovars == null || playerRovars.isEmpty()) {
                setSelectedEntity(null); // No rovars to select
                return;
            }

            int currentIndex = -1;
            if (currentSelected instanceof Rovar && currentSelected.getOwner() == currentPlayer) {
                currentIndex = playerRovars.indexOf(currentSelected);
            }

            int nextIndex = (currentIndex + 1) % playerRovars.size();
            nextEntityToSelect = playerRovars.get(nextIndex);

        } else if (currentPlayer instanceof Gombasz gombasz) {
            List<GombaTest> playerGombaTests = gombasz.getGombak(); // Assumes Gombasz has getGombak()
            if (playerGombaTests == null || playerGombaTests.isEmpty()) {
                setSelectedEntity(null); // No gombatests to select
                return;
            }

            int currentIndex = -1;
            if (currentSelected instanceof GombaTest && currentSelected.getOwner() == currentPlayer) {
                currentIndex = playerGombaTests.indexOf(currentSelected);
            }

            int nextIndex = (currentIndex + 1) % playerGombaTests.size();
            nextEntityToSelect = playerGombaTests.get(nextIndex);
        }

        setSelectedEntity(nextEntityToSelect); // This method updates selectedTekton and notifies observers
        if (nextEntityToSelect != null) {
            System.out.println("DEBUG: Game: Cycled to entity: " + nextEntityToSelect.getID());
        } else {
            System.out.println("DEBUG: Game: No entity to cycle to for player " + currentPlayer.getName());
        }
    }

    // NEW: Helper to auto-select the first entity for the current player
    public void autoSelectFirstEntityForCurrentPlayer() {
        Player currentPlayer = getCurrentPlayer();
        Entity firstEntity = null;

        if (currentPlayer instanceof Rovarasz rovarasz) {
            if (!rovarasz.getRovarok().isEmpty()) {
                firstEntity = rovarasz.getRovarok().get(0);
            }
        } else if (currentPlayer instanceof Gombasz gombasz) {
            if (!gombasz.getGombak().isEmpty()) {
                firstEntity = gombasz.getGombak().get(0);
            }
        }

        if (firstEntity != null) {
            setSelectedEntity(firstEntity); // This will also set selectedTekton and notify
        } else {
            setSelectedEntity(null); // No selectable entity, clear selection and notify
            System.out.println("DEBUG: No selectable entity found for player " + currentPlayer.getName());
        }
    }

    private boolean checkGameEnd() {
        //TODO. who won?
        boolean gombaszWin = false;
        boolean rovaraszWin = false;

        for (Player p : players) {
            if (p instanceof Gombasz gombasz) {
                if (gombasz.getGombak().size() >= WINNING_GOMBATEST_COUNT) {
                    System.out.println("Game Over! Gombász " + gombasz.getName() + " has developed " + gombasz.getGombak().size() + " GombaTests!");
                    gombaszWin = true;
                }
            } else if (p instanceof Rovarasz rovarasz) {
                if (rovarasz.getScore() >= WINNING_SPORA_SCORE) {
                    System.out.println("Game Over! Rovarász " + rovarasz.getName() + " collected " + rovarasz.getScore() + " points!");
                    rovaraszWin = true;
                }
            }
        }

        // TODO. notify game view for game end
        if (roundCounter >= MAX_ROUNDS) {
            System.out.println("Game Over! Maximum rounds (" + MAX_ROUNDS + ") reached.");
            return true;
        }

        return gombaszWin || rovaraszWin;
    }


    /**
     * Gathers all distinct entities from players and map structures
     * into the list in Game vlass.
     * Call after setup and potentially after major state changes if entities are created/destroyed.
     */
    private void populateCollections() {
        gombaTestek.clear();
        rovarok.clear();
        gombaFonalak.clear();
        entities.clear();

        // Maybe add contains to make sure not add entities twice to lists.
        for (Player p : players) {
            if (p instanceof Gombasz gombasz) {
                for (GombaFonal f : gombasz.getFonalak()) {
                    gombaFonalak.add(f);
                }
                for (GombaTest g : gombasz.getGombak()) {
                    gombaTestek.add(g);
                }
            }
            if (p instanceof Rovarasz rovarasz) {
                for (Rovar r : rovarasz.getRovarok()) {
                    rovarok.add(r);
                }
            }
        }

        entities.addAll(gombaFonalak);
        entities.addAll(gombaTestek);
        entities.addAll(rovarok);
    }

    private Player findPlayerById(String id) {
        for (Player p : players) {
            if (p.getID().equals(id)) {
                return p;
            }
        }
        return null;
    }

    private void loadEntitiesFromFile(String filename) throws IOException {
        Scanner fileScanner = new Scanner(new File(filename));
        String mode = "";

        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine().trim();
            if (line.isEmpty()) continue;

            if (line.startsWith("#")) {
                if (line.toLowerCase().contains("gombatestek")) {
                    mode = "gombatest";
                } else if (line.toLowerCase().contains("rovarok")) {
                    mode = "rovar";
                } else if (line.toLowerCase().contains("gombafonalak")) {
                    mode = "gombafonal";
                } else if (line.toLowerCase().contains("sporak")) {
                    mode = "spora";
                } else {
                    mode = "";
                }
                continue;
            }

            String[] parts = line.split("\\s+");
            if (mode.equals("gombatest") && parts.length >= 3) {
                String id = parts[0];
                Tekton tekton = map.getTektonById(parts[1]);
                Player owner = findPlayerById(parts[2]);

                if (tekton != null && owner != null) {
                    GombaTest gt = new GombaTest(id, tekton, owner);
                    tekton.addGombaTest(gt);
                    if (owner instanceof Gombasz gombasz) gombasz.addGombaTest(gt);
                }
            } else if (mode.equals("rovar") && parts.length >= 3) {
                String id = parts[0];
                Tekton tekton = map.getTektonById(parts[1]);
                Player owner = findPlayerById(parts[2]);
                if (tekton != null && owner != null) {
                    Rovar r = new Rovar(id, tekton, owner);
                    tekton.addRovar(r);
                    if (owner instanceof Rovarasz rovarasz) rovarasz.addRovar(r);
                }
            } else if (mode.equals("gombafonal") && parts.length >= 4) {
                String id = parts[0];
                Tekton src = map.getTektonById(parts[1]);
                Tekton dst = map.getTektonById(parts[2]);
                Player owner = findPlayerById(parts[3]);
                if (src != null && dst != null && owner != null) {
                    GombaFonal gf = new GombaFonal(id, src, dst, owner, GombaFonal.State.ACTIVE);
                    src.addGombaFonal(gf);
                    dst.addGombaFonal(gf);
                    if (owner instanceof Gombasz gombasz) gombasz.addFonal(gf);
                }
            } else if (mode.equals("spora") && parts.length >= 2) {
                String sporaType = parts[0];
                Tekton tekton = map.getTektonById(parts[1]);

                if (tekton != null) {
                    switch (sporaType.toLowerCase()) {
                        case "osztospora" -> tekton.addSpora(new OsztoSpora(), players.get(currentPlayerIndex));
                        case "gyorsitospora" -> tekton.addSpora(new GyorsitoSpora(), players.get(currentPlayerIndex));
                        case "lassitospora" -> tekton.addSpora(new LassitoSpora(), players.get(currentPlayerIndex));
                        case "benitospora" -> tekton.addSpora(new BenitoSpora(), players.get(currentPlayerIndex));
                        default -> System.out.println("Unknown Spora type: " + sporaType);
                    }
                }
            }
        }

        fileScanner.close();
    }


    // Player actions
    // --------------------------------------


    

    public boolean playerAction_nextEntity() {
        if (selectedEntity != null) {
            int currentIndex = entities.indexOf(selectedEntity);
            if (currentIndex >= 0) {
                if(currentIndex < entities.size() - 1){
                    setSelectedEntity(entities.get(currentIndex + 1));
                return true;
                }else{
                    setSelectedEntity(entities.get(0));
                    return true;
                }
            }
        }
        return false;
    }



    /**
     * Example action: Current player's selected Rovar attempts to eat a spora.
     *
     * @return true if action was successful, false otherwise.
     */
    public boolean playerAction_selectedRovarEatSpora() {
        if (selectedEntity instanceof Rovar rovar && rovar.getOwner() == getCurrentPlayer()) {
            Tekton currentTekton = rovar.getTekton();
            if (currentTekton != null && !currentTekton.getSporak().isEmpty()) {
                Spora sporaToEat = currentTekton.peekSpora();
                boolean success = rovar.eatSpora(sporaToEat);
                if (success) {
                    // Update player score if Rovarasz
                    if (rovar.getOwner() instanceof Rovarasz rovaraszOwner) {
                        // add the eaten spora nutrition to player score
                        rovaraszOwner.setScore(rovaraszOwner.getScore() + sporaToEat.getNutrition());
                    }
                }

                populateCollections();
                notifyStateChange("ROVAR_ATE_SPORA");
                return true;
            }
        }

        return false;
    }

    /**
     * Example action: Moves a Rovar to a target Tekton.
     *
     * @param rovar        The Rovar to move.
     * @param targetTekton The destination Tekton.
     * @return true if move was successful.
     */
    public boolean playerAction_moveRovar(Rovar rovar, Tekton targetTekton) {
        if (rovar != null && targetTekton != null && rovar.getOwner() == getCurrentPlayer()) {
            // Rovar.move() should contain all logic for validation (reachability, action points)
            if (rovar.move(targetTekton)) {
                selectedTekton = rovar.getTekton();
                notifyStateChange("ROVAR_MOVED");
                return true;
            }
        }
        return false;
    }

    public boolean playerAction_cutFonal(Rovar rovar, GombaFonal fonal){
        if (rovar != null && fonal != null && rovar.getOwner() == getCurrentPlayer()) {
            // Rovar.move() should contain all logic for validation (reachability, action points)
            if (rovar.cutGombaFonal(fonal)) {
                notifyStateChange("ROVAR_CUT");
                return true;
            }
        }
        return false;
    }

    public boolean playerAction_growFonal(Tekton src, Tekton dst){
        boolean growable = false;

        if (src.getGombatest() != null && src.getGombatest().getOwner() == getCurrentPlayer()){
            growable = true;
        }

        for (int i = 0; i < src.getFonalak().size(); i++) {
            if (src.getFonalak().get(i).getOwner() == getCurrentPlayer())
                growable = true;
        }

        if (growable){
            src.growFonal(dst, getCurrentPlayer());
            notifyStateChange("FONAL_GROWNED");
            return true;
        }

        return false;
    }

    /**
     * Action: Selected GombaTest shoots a spora to a target Tekton.
     *
     * @param target The target Tekton.
     * @return True if successful, false otherwise.
     */
    public boolean playerAction_shootSpora(Tekton target) {
        if (selectedEntity instanceof GombaTest currentGombaTest &&
                currentGombaTest.getOwner() == getCurrentPlayer() &&
                target != null) {

            int targetOldSporaCount = target.getSporak().size();
            if (currentGombaTest.shootSpora(target)) {
                notifyStateChange("SPORA_SHOT");
                return true;
            }
        }

        return false;
    }

    /**
     * Example action: Current player's selected GombaTest attempts to upgrade.
     * @return true if successful.
     */
    public boolean playerAction_selectedGombaTestUpgrade() {
        if (selectedEntity instanceof GombaTest gombaTest && gombaTest.getOwner() == getCurrentPlayer()) {
            // GombaTest.upgradeTest() handles spora consumption and level change.

            // TODO. upgrade logic
            gombaTest.upgradeTest();
            notifyStateChange("GOMBATEST_UPGRADED");
            return true;
        }
        return false;
    }


    // --- Getters for View ---
    // --------------------------------------------
    public int getRoundCounter() { return roundCounter; }
    public Player getCurrentPlayer() {return players.get(currentPlayerIndex);}
    public List<Player> getPlayers() { return Collections.unmodifiableList(players); }
    public Map getGameMap() { return map; } // Renamed for clarity from just 'map'
    public List<Tekton> getAllTektons() { return map != null ? Collections.unmodifiableList(map.getTektonok()) : Collections.emptyList(); }

    public List<GombaTest> getAllGombaTestek() { return Collections.unmodifiableList(gombaTestek); }
    public List<Rovar> getAllRovarok() { return Collections.unmodifiableList(rovarok); }
    public List<GombaFonal> getAllGombaFonalak() { return Collections.unmodifiableList(gombaFonalak); }


    public Tekton getSelectedTekton() { return selectedTekton; }
    public Entity getSelectedEntity() { return selectedEntity; }

    // --- Setters for UI interaction (Controller calls these) ---
    public void setSelectedTekton(Tekton tekton) {
        this.selectedTekton = tekton;
        this.selectedEntity = null; // Clear entity selection if tekton is selected, or handle as needed
        System.out.println("DEBUG: Tekton selected: " + (tekton != null ? tekton.getID() : "None"));
        notifyStateChange("TEKTON_CHANGED");
    }

    public void setSelectedEntity(Entity entity) {
        this.selectedEntity = entity;
        if (entity instanceof Rovar) {
            this.selectedTekton = ((Rovar) entity).getTekton();
        } else if (entity instanceof GombaTest) {
            this.selectedTekton = ((GombaTest) entity).getTekton();
        } else {
            this.selectedTekton = null; // Or keep current tekton selection?
        }
        notifyStateChange();
        System.out.println("DEBUG: Entity selected: " + (entity != null ? entity.getID() : "None"));
    }


    // Observer related stuffs
    // -----------------------------------------------
    /**
     * Sets the changed flag and notifies all observers.
     * Can pass an argument describing the event.
     * @param arg Optional argument describing the event.
     */
    public void notifyStateChange(Object arg) {
        setChanged();
        notifyObservers(arg);
    }
    public void notifyStateChange() { // Overload for no specific arg
        setChanged();
        notifyObservers();
    }

}






//    void fastForwardRounds(){
//        System.out.print("How many rounds do you want to skip:");
//        int time = Integer.parseInt(scanner.nextLine());
//        for(int i = 0; i < time * 4; i++) nextPlayerTurn();
//        for (int i = 0; i < time; i++) {
//            endRound();
//        }
//    }
//
//    void setTestingMode(){
//        testingMode = !testingMode;
//    }
//
//    private void listAllEntities() {
//
//        populateCollections();
//
//        System.out.println("\n");
//        List<Tekton> tmp_tektonok = map.getTektonok();
//        String tektonIds = tmp_tektonok.stream()
//                           .map(Tekton::getID)
//                           .reduce((id1, id2) -> id1 + " " + id2)
//                           .orElse("");
//        System.out.println("Tekton IDs: " + tektonIds);
//
//        java.util.Map<String, List<Entity>> groupedEntities = new HashMap<>();
//        for (Entity e : entities) {
//            groupedEntities.computeIfAbsent(e.getClass().getSimpleName(), k -> new ArrayList<>()).add(e);
//        }
//        groupedEntities.forEach((type, entityList) -> {
//            String entityIds = entityList.stream()
//                         .map(Entity::getID)
//                         .reduce((id1, id2) -> id1 + " " + id2)
//                         .orElse("");
//            System.out.println(type + " IDs: " + entityIds);
//        });
//
//    }
//
//    private void showMainMenu() {
//        System.out.println("-----------------");
//        System.out.println("0) Exit");
//        System.out.println("1) Inspect Entity");
//        System.out.println("2) Select Entity");
//        System.out.println("3) Next Player");
//        System.out.println("4) List all Entities");
//        System.out.println("5) Select tekton");
//        System.out.println("6) Toggle testing mode (currently: " + testingMode + ")");
//        if(testingMode) {
//            System.out.println("7) Skip Rounds");
//
//        }
//        System.out.print("Choose option: ");
//    }
//
//
//    private void inspectEntity() {
//        System.out.print("Name an entity ID to inspect: ");
//        String id = scanner.nextLine();
//
//        // 1. Search ID in tektonok
//        for (Tekton t : map.getTektonok()) {
//            if (t.getID().equals(id)) {
//                System.out.println(t);
//                return;  // return if ID found in tektonok
//            }
//        }
//
//        // 2. Search ID in entities
//        for (Entity e : entities) {
//            if (e.getID().equals(id)) {
//                System.out.println(e);
//                return;
//            }
//        }
//
//        // 3. ID not found
//        System.out.println("Entity with this ID ("+ id + ") could not be found");
//    }
//
//    private void selectEntity() {
//        System.out.print("Name an entity to select: ");
//        String name = scanner.nextLine();
//
//        // Rovar kiválasztása
//        for (Rovar r : rovarok) {
//            if (r.getID().equals(name) && r.getOwner() == players.get(currentPlayerIndex)) {
//                selectRovar(r);
//                return;
//            }
//        }
//
//        // GombaTest kiválasztása
//        for (GombaTest g : gombaTestek) {
//            if (g.getID().equals(name) && g.getOwner() == players.get(currentPlayerIndex)) {
//                selectGombaTest(g);
//                return;
//            }
//        }
//
//        System.out.println("Entity with this ID ("+ name + ") could not be found for this player");
//    }
//
//    private void selectTekton() {
//
//
//
//        System.out.print("Name a tekton to select: ");
//        String name = scanner.nextLine();
//
//        Tekton tekton = map.getTektonById(name);
//
//        String input = " ";
//
//        while (!input.equals("0")) {
//            System.out.println("\n0) Exit");
//            System.out.println("1) Grow Fonal");
//            if(testingMode)System.out.println("2) Split");
//            System.out.print("Choose option: ");
//
//            input = scanner.nextLine();
//
//            switch (input) {
//                case "1" -> {
//                    System.out.println("Grow Gombafonal. Name a target Tekton: ");
//                    String target_id = scanner.nextLine();
//                    GombaFonal ujFonal = tekton.growFonal(map.getTektonById(target_id), getCurrentPlayer());
//                    if(ujFonal != null) ((Gombasz) players.get(currentPlayerIndex)).getFonalak().add(ujFonal);
//                    System.out.println("Grow new fonal successful, new Fonal ID: " + ujFonal.getID());
//                }
//
//                case "2" -> {
//                    if(testingMode){
//                    double tmp = tekton.getSplitRate();
//                    tekton.setSplitRate(1);
//                    map.addTekton(tekton.split());
//                    tekton.setSplitRate(tmp);
//                    }
//                }
//                case "0" -> {
//                }
//                default -> System.out.println("Invalid Option");
//            }
//        }
//
//    }
//
//
//
//
//    private void selectRovar(Rovar rovar) {
//        System.out.println("___________________________");
//        System.out.println(rovar.getID() + " selected");
//        String input = "";
//
//        while (!input.equals("0")) {
//            System.out.println("\n0) Exit");
//            System.out.println("1) Eat Spora");
//            System.out.println("2) Move Rovar");
//            System.out.println("3) Cut Fonal");
//            System.out.print("Choose option: ");
//
//            input = scanner.nextLine();
//
//            switch (input) {
//                case "1" -> {
//                    if (rovar.getTekton().getSporak().isEmpty()) {
//                        System.out.println("Nincs spora ezen a Tektonon, nem tudsz enni.");
//                    } else {
//                        // A lista első spóráját próbálja megenni
//                        Spora spora = rovar.getTekton().getSporak().peek();
//                        if (rovar.eatSpora(spora)) {
//                            System.out.println("Sikeresen megetted a sporat: " + spora.getClass().getSimpleName());
//                        } else {
//                            System.out.println("Nem sikerult megenni a sporat.");
//                        }
//                    }
//                }
//
//                case "2" -> {
//                    System.out.print("Target Tekton name: ");
//                    String targetTektonName = scanner.nextLine();
//                    Tekton targetTekton = map.getTektonById(targetTektonName);
//
//                    rovar.move(targetTekton);
//
//                }
//
//                case "3" -> {
//                    System.out.print("Target GombaFonal name: ");
//                    String targetFonalName = scanner.nextLine();
//
//                    GombaFonal targetFonal = null;
//                    for (Tekton tekton : map.getTektonok()) { // Use map.getTektonok() to get all Tektons
//                        for (GombaFonal gf : tekton.getFonalak()) {
//                            if (gf.getID().equals(targetFonalName)) {
//                                targetFonal = gf;
//                                break;
//                            }
//                        }
//                        if (targetFonal != null) break; // Exit once the GombaFonal is found
//                    }
//                    /*
//                    // If the GombaFonal is found, remove it
//                    if (targetFonal != null) {
//                        // Clean up the GombaFonal from both Tektons it connects
//                        targetFonal.cut();  // Assumes the clean method disconnects the GombaFonal from the Tektons
//                        System.out.println("Cut Gombafonal: " + targetFonalName);
//                    } else {
//                        System.out.println("No Gombafonal found with the name: " + targetFonalName);
//                    }*/
//                    rovar.cutGombaFonal(targetFonal);
//                }
//
//
//                case "0" -> {
//                }
//                default -> System.out.println("Invalid Option");
//            }
//        }
//    }
//
//    private void selectGombaTest(GombaTest gombaTest) {
//        String input = "";
//
//        while (!input.equals("0")) {
//            System.out.println("\n0) Exit");
//            //System.out.println("1) Grow Fonal (1 spora)");
//            System.out.println("1) Shoot Spora (2 spora)");
//            System.out.println("2) Upgrade GombaTest (4 spora)");
//            System.out.print("Choose option: ");
//
//            input = scanner.nextLine();
//
//            switch (input) {
//                case "1" -> {
//                    System.out.println("Grow Gombafonal. Name a target Tekton: ");
//                    String target_id = scanner.nextLine();
//
//                    Tekton target = map.getTektonById(target_id);
//                    Tekton tekton = gombaTest.getTekton();
//                    GombaFonal ujFonal = tekton.growFonal(target, getCurrentPlayer());
//                    if(ujFonal != null) ((Gombasz) players.get(currentPlayerIndex)).getFonalak().add(ujFonal);
//                    System.out.println("Grow new fonal successful, new Fonal ID: " + ujFonal.getID());
//
//                }
//
//                case "2" -> {
//                    System.out.println("Shoot Spora. Name a target Tekton: ");
//                    String target_id = scanner.nextLine();
//
//                    Tekton target = map.getTektonById(target_id);
//
//                    if (target == null) {
//                        System.out.println("Invalid target Tekton ID.");
//                        return;
//                    }
//
//                    gombaTest.shootSpora(target);
//                }
//
//
//                case "3" -> {
//                    System.out.println("Upgrade GombaTest");
//                    // upgrade logic
//                    gombaTest.upgradeTest();
//                }
//
//                // upgrade logic
//                case "0" -> {
//                }
//                default -> System.out.println("Invalid Option");
//            }
//        }
//    }
