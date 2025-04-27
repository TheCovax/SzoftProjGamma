package Fungorium.src.skeleton;

import Fungorium.src.model.*;
import Fungorium.src.model.player.Player;
import Fungorium.src.model.tekton.*;
import java.util.*;

public class Game {

    private static final int WINNING_SCORE = 10;
    private List<Player> players;
    private List<GombaTest> gombaTestek;
    private List<Rovar> rovarok;
    private List<GombaFonal> gombaFonalak;
    private List<Tekton> tektonok;
    private int currentPlayerIndex = 0;
    private Scanner scanner;

  
    

    public static void main(String[] args) {
        Game game = new Game();
        game.initializeGame();
        game.start();
    }


    private void initializeGame() {

        players = new ArrayList<>();
        gombaTestek = new ArrayList<>();
        rovarok = new ArrayList<>();
        gombaFonalak = new ArrayList<>();
        tektonok = new ArrayList<>();
        scanner = new Scanner(System.in);

        // Itt állítod össze a játék kezdeti állapotát (demo kedvéért pár dummy érték)
        Player p1 = new Player();
        Player p2 = new Player();
        players.add(p1);
        players.add(p2);

        StabilTekton t1 = new StabilTekton();
        StabilTekton t2 = new StabilTekton();
        tektonok.add(t1);
        tektonok.add(t2);

        GombaTest g1 = new GombaTest(t1, p1);
        gombaTestek.add(g1);
        t1.addGombaTest(g1);

        Rovar r1 = new Rovar(t2, p1);
        rovarok.add(r1);
        t2.addRovar(r1);

        
    }

    public void start() {
        String input = "";

        while (!input.equals("0")) {
            showMainMenu();
            input = scanner.nextLine();

            switch (input) {
                case "1":
                    inspectEntity();
                    break;
                case "2":
                    selectEntity();
                    break;
                case "3":
                    nextPlayer();
                    break;
                case "0":
                    System.out.println("Kilépés...");
                    break;
                default:
                    System.out.println("Ismeretlen parancs.");
            }
        }
    }

    private void showMainMenu() {
        System.out.println("\nEntities on map:");
        for (Tekton t : tektonok) {
            //TODO: System.out.println(t.getID());
        }
        for (GombaFonal f : gombaFonalak) {
            //TODO: System.out.println(f.getID());
        }
        for (GombaTest g : gombaTestek) {
            //TODO: System.out.println(g.getID());
        }
        for (Rovar r : rovarok) {
            //TODO: System.out.println(r.getID());
        }
        System.out.println("-----------------");
        System.out.println("0) Exit");
        System.out.println("1) Inspect Entity");
        System.out.println("2) Select Entity");
        System.out.println("3) Next Player");
        System.out.print("Choose option: ");
    }

    private void inspectEntity() {
        System.out.print("Name an entity to inspect: ");
        String name = scanner.nextLine();

        // keresés az entitások között név alapján
        // itt érdemes lenne minden entitásnak legyen getId()
    }

    private void selectEntity() {
        System.out.print("Name an entity to select: ");
        String name = scanner.nextLine();

        // Kiválasztás logika: Pl. ha rovar -> move, cutFonal, eatSpora
        // Ha gombatest -> growFonal, shootSpora, upgradeTest
    }

    private void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        System.out.println("Következő játékos: " + players.get(currentPlayerIndex).getName());

        checkGameEnd();  // mindig ellenőrizzük a játék végét
    }

    private void checkGameEnd() {
        // 1. Pontszám elérés
        for (Player p : players) {
            if (p.getScore() >= WINNING_SCORE) {
                System.out.println("Játék vége! " + p.getName() + " elérte a " + WINNING_SCORE + " pontot!");
                System.exit(0);
            }
        }

        // 2. GombaTestek elfogytak
        if (gombaTestek.isEmpty()) {
            System.out.println("Játék vége! Elfogytak a GombaTestek!");
            System.exit(0);
        }
    }
}
