package Fungorium.src.model;

import Fungorium.src.model.player.Gombasz;
import Fungorium.src.model.player.Player;
import Fungorium.src.model.player.Rovarasz;
import Fungorium.src.model.spora.*;
import Fungorium.src.model.tekton.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Game {

    private int roundCounter = 0;
    private static final int MAX_ROUNDS = 15;
    private static final int WINNING_GOMBATEST_COUNT = 10;
    private static final int WINNING_SPORA_SCORE = 30;
    private List<Player> players;
    private List<GombaTest> gombaTestek;
    private List<Rovar> rovarok;
    private List<GombaFonal> gombaFonalak;
    private List<Entity> entities;
    private int currentPlayerIndex = 0;
    private Scanner scanner;
    public Map map = new Map();

    boolean testing;

    public static void main(String[] args) throws IOException {
        Game game = new Game();
        game.initializeGame();
        game.start();
    }


    private void initializeGame() throws IOException {

        // Initialize variables
        map = new Map();
        players = new ArrayList<>();
        gombaTestek = new ArrayList<>();
        rovarok = new ArrayList<>();
        gombaFonalak = new ArrayList<>();
        entities = new ArrayList<>();
        scanner = new Scanner(System.in);

        testing = false;

        // Initialize players
        initPlayers();

        // Load Game map from file
        map.loadMap("SzoftProjGamma/mapsave.txt");
        loadEntitiesFromFile("SzoftProjGamma/mapsave.txt");

        // Synchronize entities from Player class
        populateCollections();
    }

    public void initPlayers(){
        // Create Players
        Gombasz p1 = new Gombasz("GB1");
        Gombasz p2 = new Gombasz("GB2");
        Rovarasz p3 = new Rovarasz("RS3");
        Rovarasz p4 = new Rovarasz("RS4");

        System.out.print("\n\nFirst player's name (Team 1, Gombasz): ");
        p1.setName(scanner.nextLine());

        System.out.print("Second player's name (Team 2, Gombasz): ");
        p2.setName(scanner.nextLine());

        System.out.print("Third player's name (Team 1, Rovarasz): ");
        p3.setName(scanner.nextLine());

        System.out.print("Fourth player's name (Team 2, Rovarasz): ");
        p4.setName(scanner.nextLine());

        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);
    }

    public void start() {
        while (true) {
            System.out.println("\n--- Round " + (roundCounter + 1) + " ---"); // Not increasing yet

            //Collections.shuffle(players);

            for (Player player : players) {
                System.out.println("\nIt's " + player.getName() + "'s turn:");
                currentPlayerIndex = players.indexOf(player);

                String input = "";
                while (currentPlayerIndex == players.indexOf(player)) {
                    populateCollections();
                    showMainMenu();
                    input = scanner.nextLine();

                    switch (input) {
                        case "1" -> inspectEntity();
                        case "2" -> selectEntity();
                        case "3" -> nextPlayer();
                        case "4" -> listAllEntities();
                        case "6" -> setTestingMode();
                        case "7" -> {if(testing) fastForwardRounds();
                                    else System.out.println("Unknown Command");}
                        case "5" -> selectTekton();
                        case "0" -> System.exit(0);
                        default -> System.out.println("Unknown Command");
                    }
                }

                if (checkGameEnd()) {
                    return;
                }
            }
            //round end
            roundEnd();
            
        }
    }

    


    private void roundEnd() {
        roundCounter++;  // Increase after all players played in round
        entities.forEach(action -> action.update()); // Call update on all entities
        map.getTektonok().forEach(action -> action.update()); // Call update on all tektons
    }


    private int nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        System.out.println("Next player: " + players.get(currentPlayerIndex).getName());
        return currentPlayerIndex;
    }


    void fastForwardRounds(){
        System.out.print("How many rounds do you want to skip:");
        int time = Integer.parseInt(scanner.nextLine());
        for(int i = 0; i < time * 4; i++) nextPlayer();
        roundCounter += time;
    }

    void setTestingMode(){
        testing = !testing;
    }

    private void listAllEntities() {
        populateCollections();
        for (Tekton t: map.getTektonok()){
            System.out.println(t.getID());
        }

        for (Entity e: entities){
            System.out.println(e.getID());
        }
    }

    private void showMainMenu() {
        System.out.println("-----------------");
        System.out.println("0) Exit");
        System.out.println("1) Inspect Entity");
        System.out.println("2) Select Entity");
        System.out.println("3) Next Player");
        System.out.println("4) List all Entities");
        System.out.println("5) Select tekton");
        System.out.println("6) Set testing mode (currently: " + testing + ")");
        if(testing) {
            System.out.println("7) Skip Rounds");

        }
        System.out.print("Choose option: ");
    }


    private void inspectEntity() {
        System.out.print("Name an entity ID to inspect: ");
        String id = scanner.nextLine();

        // 1. Search ID in tektonok
        for (Tekton t : map.getTektonok()) {
            if (t.getID().equals(id)) {
                System.out.println(t);
                return;  // return if ID found in tektonok
            }
        }

        // 2. Search ID in entities
        for (Entity e : entities) {
            if (e.getID().equals(id)) {
                System.out.println(e);
                return;
            }
        }

        // 3. ID not found
        System.out.println("Entity with this ID ("+ id + ") could not be found");
    }

    private void selectEntity() {
        System.out.print("Name an entity to select: ");
        String name = scanner.nextLine();

        // Rovar kiválasztása
        for (Rovar r : rovarok) {
            if (r.getID().equals(name) && r.getOwner() == players.get(currentPlayerIndex)) {
                selectRovar(r);
                return;
            }
        }

        // GombaTest kiválasztása
        for (GombaTest g : gombaTestek) {
            if (g.getID().equals(name) && g.getOwner() == players.get(currentPlayerIndex)) {
                selectGombaTest(g);
                return;
            }
        }

        System.out.println("Entity with this ID ("+ name + ") could not be found for this player");
    }

    private void selectTekton() {
        


        System.out.print("Name a tekton to select: ");
        String name = scanner.nextLine();

        Tekton tekton = map.getTektonById(name);

        String input = " ";

        while (!input.equals("0")) {
            System.out.println("\n0) Exit");
            System.out.println("1) Grow Fonal");
            if(testing)System.out.println("2) Split");
            System.out.print("Choose option: ");
        
            input = scanner.nextLine();

            switch (input) {
                case "1" -> {
                    System.out.println("Grow Gombafonal. Name a target Tekton: ");
                    String target_id = scanner.nextLine();
                    GombaFonal ujFonal = tekton.growFonal(map.getTektonById(target_id), getCurrentPlayer());
                    if(ujFonal != null) ((Gombasz) players.get(currentPlayerIndex)).getFonalak().add(ujFonal);
                }

                case "2" -> {
                    if(testing){
                    double tmp = tekton.getSplitRate();
                    tekton.setSplitRate(1);
                    map.addTekton(tekton.split());
                    tekton.setSplitRate(tmp);
                    }
                }
                case "0" -> {
                }
                default -> System.out.println("Invalid Option"); 
            }
        }

    }

            


    private void selectRovar(Rovar rovar) {
        System.out.println("___________________________");
        System.out.println(rovar.getID() + " selected");
        String input = "";

        while (!input.equals("0")) {
            System.out.println("\n0) Exit");
            System.out.println("1) Eat Spora");
            System.out.println("2) Move Rovar");
            System.out.println("3) Cut Fonal");
            System.out.print("Choose option: ");

            input = scanner.nextLine();

            switch (input) {
                case "1" -> {
                    if (rovar.getTekton().getSporak().isEmpty()) {
                        System.out.println("Nincs spora ezen a Tektonon, nem tudsz enni.");
                    } else {
                        // A lista első spóráját próbálja megenni
                        Spora spora = rovar.getTekton().getSporak().peek();
                        if (rovar.eatSpora(spora)) {
                            System.out.println("Sikeresen megetted a sporat: " + spora.getClass().getSimpleName());
                        } else {
                            System.out.println("Nem sikerult megenni a sporat.");
                        }
                    }
                }

                case "2" -> {
                    System.out.print("Target Tekton name: ");
                    String targetTektonName = scanner.nextLine();
                    Tekton targetTekton = map.getTektonById(targetTektonName);

                    if (targetTekton == null) {
                        System.out.println("No Tekton found with the name: " + targetTektonName);
                        return;
                    }

                    // Find reachable Tektons based on the Rovar's speed
                    List<Tekton> reachable = rovar.getTekton().findReachableTektonWithinDistance(rovar.getSpeed());

                    if (reachable.contains(targetTekton)) {
                        rovar.move(targetTekton);
                        System.out.println("Rovar successfully moved to: " + targetTektonName);
                    } else {
                        System.out.println("Target Tekton is out of reach.");
                    }
                }

                case "3" -> {
                    System.out.print("Target GombaFonal name: ");
                    String targetFonalName = scanner.nextLine();

                    GombaFonal targetFonal = null;
                    for (Tekton tekton : map.getTektonok()) { // Use map.getTektonok() to get all Tektons
                        for (GombaFonal gf : tekton.getFonalak()) {
                            if (gf.getID().equals(targetFonalName)) {
                                targetFonal = gf;
                                break;
                            }
                        }
                        if (targetFonal != null) break; // Exit once the GombaFonal is found
                    }

                    // If the GombaFonal is found, remove it
                    if (targetFonal != null) {
                        // Clean up the GombaFonal from both Tektons it connects
                        targetFonal.cut();  // Assumes the clean method disconnects the GombaFonal from the Tektons
                        System.out.println("Cut Gombafonal: " + targetFonalName);
                    } else {
                        System.out.println("No Gombafonal found with the name: " + targetFonalName);
                    }
                }


                case "0" -> {
                }
                default -> System.out.println("Invalid Option");
            }
        }
    }

    private void selectGombaTest(GombaTest gombaTest) {
        String input = "";

        while (!input.equals("0")) {
            System.out.println("\n0) Exit");
            System.out.println("1) Grow Fonal (1 spora)");
            System.out.println("2) Shoot Spora (2 spora)");
            System.out.println("3) Upgrade GombaTest (4 spora)");
            System.out.print("Choose option: ");

            input = scanner.nextLine();

            switch (input) {
                case "1" -> {
                    System.out.println("Grow Gombafonal. Name a target Tekton: ");
                    String target_id = scanner.nextLine();

                    Tekton target = map.getTektonById(target_id);
                    gombaTest.getTekton().growFonal(target, getCurrentPlayer());

                }

                case "2" -> {
                    System.out.println("Shoot Spora. Name a target Tekton: ");
                    String target_id = scanner.nextLine();

                    Tekton target = map.getTektonById(target_id);

                    if (target == null) {
                        System.out.println("Invalid target Tekton ID.");
                        return;
                    }

                    gombaTest.shootSpora(target);
                }


                case "3" -> {
                    System.out.println("Upgrade GombaTest");
                    // upgrade logic
                    gombaTest.upgradeTest();
                }

                // upgrade logic
                case "0" -> {
                }
                default -> System.out.println("Invalid Option");
            }
        }
    }


    private void populateCollections(){
        List<GombaFonal> tmpFonalak = new ArrayList<>();
        List<GombaTest> tmpTestek = new ArrayList<>();
        List<Rovar> tmpRovarok = new ArrayList<>();
        List<Entity> tmpEntities = new ArrayList<>();

        for (Player p : players) {
            if (p instanceof Gombasz gombasz) {
                for (GombaFonal f : gombasz.getFonalak()) {
                    tmpFonalak.add(f);
                }
                for (GombaTest g : gombasz.getGombak()) {
                    tmpTestek.add(g);
                }
                
            }
            if (p instanceof Rovarasz rovarasz) {
                for (Rovar r : rovarasz.getRovarok()) {
                    tmpRovarok.add(r);
                }
            }
        }

        tmpEntities.addAll(tmpFonalak);
        tmpEntities.addAll(tmpTestek);
        tmpEntities.addAll(tmpRovarok);

        this.gombaFonalak = tmpFonalak;
        this.gombaTestek = tmpTestek;
        this.rovarok = tmpRovarok;
        this.entities = tmpEntities;
    }


    private Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    private boolean checkGameEnd() {
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

        if (roundCounter >= MAX_ROUNDS) {
            System.out.println("Game Over! Maximum rounds (" + MAX_ROUNDS + ") reached.");
            return true;
        }

        return gombaszWin || rovaraszWin;
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
                }else {
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
}

