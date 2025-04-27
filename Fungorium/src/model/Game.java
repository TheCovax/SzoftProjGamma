package Fungorium.src.model;

import Fungorium.src.model.player.Gombasz;
import Fungorium.src.model.player.Player;
import Fungorium.src.model.player.Rovarasz;
import Fungorium.src.model.spora.OsztoSpora;
import Fungorium.src.model.spora.Spora;
import Fungorium.src.model.tekton.*;
import java.io.IOException;
import java.util.*;

public class Game {

    private static final int WINNING_SCORE = 10;
    private List<Player> players;
    private List<GombaTest> gombaTestek;
    private List<Rovar> rovarok;
    private List<GombaFonal> gombaFonalak;
    private List<Entity> entities;
    private int currentPlayerIndex = 0;
    private Scanner scanner;
    public Map map = new Map();



    public static void main(String[] args) throws IOException {
        Game game = new Game();
        game.initializeGame();
        game.start();
    }


    private void initializeGame() throws IOException {

        map = new Map();
        players = new ArrayList<>();
        gombaTestek = new ArrayList<>();
        rovarok = new ArrayList<>();
        gombaFonalak = new ArrayList<>();
        scanner = new Scanner(System.in);

        // Itt állítod össze a játék kezdeti állapotát (demo kedvéért pár dummy érték)
        Gombasz p1 = new Gombasz();
        Gombasz p2 = new Gombasz();
        Rovarasz p3 = new Rovarasz();
        Rovarasz p4 = new Rovarasz();
        
        System.out.println("First player's name (Team 1, Gombasz): ");
        p1.setName(scanner.nextLine());

        System.out.println("Second player's name (Team 2, Gombasz): ");
        p2.setName(scanner.nextLine());

        System.out.println("Third player's name (Team 1, Rovarasz): ");
        p3.setName(scanner.nextLine());

        System.out.println("Fourth player's name (Team 2, Rovarasz): ");
        p4.setName(scanner.nextLine());

        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);

        map.loadMap("./SzoftProjGamma/mapsave.txt");


        p3.addRovar(new Rovar(map.getTektonById("TA"), p3));
        p3.addRovar(new Rovar(map.getTektonById("TA"), p3));
        p4.addRovar(new Rovar(map.getTektonById("TB"), p4));
        p4.addRovar(new Rovar(map.getTektonById("TB"), p4));


        p1.addGombaTest( new GombaTest(map.getTektonById("TA"), p1));
        p1.addGombaTest( new GombaTest(map.getTektonById("TC"), p1));
        p2.addGombaTest( new GombaTest(map.getTektonById("TD"), p2));
        p2.addGombaTest( new GombaTest(map.getTektonById("TB"), p2));

        p1.addFonal(new GombaFonal(map.getTektonById("TA"), map.getTektonById("TB"), p1));
        p1.addFonal(new GombaFonal(map.getTektonById("TB"), map.getTektonById("TC"), p1));
        p1.addFonal(new GombaFonal(map.getTektonById("TA"), map.getTektonById("TC"), p1));
        p2.addFonal(new GombaFonal(map.getTektonById("TA"), map.getTektonById("TD"), p1));
        p2.addFonal(new GombaFonal(map.getTektonById("TD"), map.getTektonById("TE"), p1));

        map.getTektonById("TE").addSpora(new OsztoSpora());

        populateCollections();
    }

    public void start() {
        String input = "";

        while (!input.equals("0")) {
            populateCollections();
            showMainMenu();
            input = scanner.nextLine();

            switch (input) {
                case "1" -> inspectEntity();
                case "2" -> selectEntity();
                case "3" -> nextPlayer();
                case "4" -> listAllEntities();
                case "0" -> System.out.println("Exiting...");
                default -> System.out.println("Unknown Command");
            }
        }
    }

    private void listAllEntities() {
        for (Tekton t: map.getTektonok()){
            System.out.println(t.getID());
        }

        for (Entity e: entities){
            System.out.println(e.getID());
        }
    }

    private void showMainMenu() {
        /*
        System.out.println("\nEntities on map:");

        System.out.println("\nTektonok:");
        for (Tekton t : map.getTektonok()) {
            System.out.println(t);
        }
        System.out.println("\n\nFonalak:");
        for (GombaFonal f : gombaFonalak) {
            System.out.println(f.getID());
        }
        System.out.println("\n\nTestek:");
        for (GombaTest g : gombaTestek) {
            System.out.println(g.getID());
        }
        System.out.println("\n\nRovarok:");
        for (Rovar r : rovarok) {
            System.out.println(r.getID());
        }*/
        System.out.println("-----------------");
        System.out.println("0) Exit");
        System.out.println("1) Inspect Entity");
        System.out.println("2) Select Entity");
        System.out.println("3) Next Player");
        System.out.println("4) List all Entities");
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
            if (r.getID().equals(name)) {
                selectRovar(r);
                return;
            }
        }

        // GombaTest kiválasztása
        for (GombaTest g : gombaTestek) {
            if (g.getID().equals(name)) {
                selectGombaTest(g);
                return;
            }
        }

        System.out.println("Entity with this ID ("+ name + ") could not be found");
    }

    private void selectRovar(Rovar rovar) {
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

                    // Assuming we have a list of Tekton objects (or reachable Tekton(s) in the game world)
                    // Iterate over each Tekton to find the GombaFonal by name
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
                        targetFonal.clean();  // Assumes the clean method disconnects the GombaFonal from the Tektons
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
                    System.out.println("Grow Gombafonal");
                    // growFonal logic

                }



                case "2" -> {
                    System.out.println("Shoot Spora. Name a target Tekton: ");
                    String target_id = scanner.nextLine();

                    // shootSpora logic
                    Tekton source = gombaTest.tekton;
                    Tekton target = map.getTektonById(target_id);
                    if( source.findReachableTektonWithinDistance(1).contains(target)){
                        gombaTest.shootSpora(target);
                    }else{
                        System.out.println("Target is unreachable.");
                    }

                // shootSpora logic
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

    private void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        System.out.println("Next Player: " + players.get(currentPlayerIndex).getName());

        checkGameEnd();
    }

    private void checkGameEnd() {
        // 1. Pontszám elérés
        for (Player p : players) {
            if (p.getScore() >= WINNING_SCORE) {
                System.out.println("Game Over! " + p.getName() + " reached " + WINNING_SCORE + " points!");
                System.exit(0);
            }
        }

        // 2. GombaTestek elfogytak
        if (gombaTestek.isEmpty()) {
            System.out.println("Game Over! Someone ran out of GombaTest!");
            System.exit(0);
        }
    }

    
}

