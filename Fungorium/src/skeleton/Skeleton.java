package Fungorium.src.skeleton;

import Fungorium.src.model.GombaFonal;
import Fungorium.src.model.GombaTest;
import Fungorium.src.model.Rovar;
import Fungorium.src.model.spora.*;
import Fungorium.src.model.tekton.ElszigeteltTekton;
import Fungorium.src.model.tekton.StabilTekton;
import Fungorium.src.model.tekton.Tekton;
import Fungorium.src.utility.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Skeleton {
    public static void main(String[] args) {
        testCases();
    }


    static void testCases(){
        Scanner scanner = new Scanner(System.in);

        String input = "";

        while (!input.equals("0")) {
            System.out.println("\nFungorium skeleton tesztek");

            System.out.println("----------------------------");
    
            System.out.println("0. Kilepes");
            System.out.println("1. Fonal novesztese");
            System.out.println("2. Tekton torese");
            System.out.println("3. Spora lovese");
            System.out.println("4. Spora termelese");
            System.out.println("5. GombaTest fejlesztése");
            System.out.println("6. GombaTest növesztése");
            System.out.println("7. Rovar mozgatása");
            System.out.println("8. Gombafonal elvágása");
            System.out.println("9. Lassító spóra megevése");
            System.out.println("10. Gyorsító spóra megevése");
            System.out.println("11. Bénító spóra megevése");
            System.out.println("12. Jatek vege (maxpontszám)");
            System.out.println("13. Játék vége (nincs több GombaTest)");
            

    
            System.out.println("----------------------------");
    
            System.out.print("Adja meg melyik test-case-t szeretné futtatni:");
            input = scanner.nextLine();

            if(!input.equals("0")){
                selectTestCase(input);
            }
        }
    }

    static void selectTestCase(String input){
        switch (input) {
            case "1" -> fonalNoveszteseTestCase();
            case "2" -> splitTestCase();
            case "3" -> sporaLovesTestCase();
            case "4" -> testProduceSpora();
            case "5" -> gombaTestFejlesztesTestCase();
            case "6" -> gombaTestNovesztesTestCase();
            case "7" -> rovarMoveTestCase();
            case "8" -> cutGombaFonalTestCase();
            case "9" -> testEatLassitoSpora();
            case "10" -> {
                testEatGyoritoSpora();
            }
            case "11" -> testEatBenitoSpora();
            case "12" -> testJatekVege();
            case "13" -> testJatekVegeElfogyottGombaTest();
        }
    }

    static void sporaLovesTestCase(){

        //Teszt tektonok letrehozasa
        StabilTekton tekton1 = new StabilTekton(0.0, new GombaTest());
        StabilTekton tekton2 = new StabilTekton();

        //Spora hozzaadasa az elso tektonhoz
        tekton1.addSpora(new Spora());

        tekton1.getTestek().get(0).setTekton(tekton1);

        System.out.println("Tekton1 Sporaszam:" + tekton1.getSporak().size());
        System.out.println("Tekton2 Sporaszam:" + tekton2.getSporak().size());

        System.out.println("Spora lovese tekton 1-rol tekton 2-re");
        tekton1.getTestek().get(0).shootSpora(tekton2);
        
        System.out.println("Tekton1 Sporaszam:" + tekton1.getSporak().size());
        System.out.println("Tekton2 Sporaszam:" + tekton2.getSporak().size());
    }

    static void fonalNoveszteseTestCase(){
        //Teszt tektonok letrehozasa
        StabilTekton tekton1 = new StabilTekton();
        StabilTekton tekton2 = new StabilTekton();

        System.out.println("Fonal novesztese 2 stabil tekton eseten");
        System.out.println("------------------------------");

        System.out.println("Tekton 1 fonalak szama: " + tekton1.getFonalak().size());
        System.out.println("Tekton 2 fonalak szama: " + tekton2.getFonalak().size());

        //Fonal novesztese sporak nelkul (sikertelen)
        System.out.println("Fonal novesztese tekton 1 és 2 közt, sporak nelkul");
        tekton1.growFonal(tekton2, "Teszt");

        System.out.println("Tekton 1 fonalak szama: " + tekton1.getFonalak().size());
        System.out.println("Tekton 2 fonalak szama: " + tekton2.getFonalak().size());

        //2 spora hozzaadasa
        tekton1.addSpora(new Spora());
        tekton1.addSpora(new Spora());

        //Fonal novesztese eleg sporaval (sikeres)
        System.out.println("Fonal novesztese tekton 1 és 2 közt, eleg sporaval");
        tekton1.growFonal(tekton2, "Teszt");

        System.out.println("Tekton 1 fonalak szama: " + tekton1.getFonalak().size());
        System.out.println("Tekton 2 fonalak szama: " + tekton2.getFonalak().size());

        System.out.println("------------------------------");
        System.out.println("Fonal novesztese 1 stabil tekton es 1 elszigetelt tekton eseten");
        System.out.println("------------------------------");

        StabilTekton stabilTekton = new StabilTekton();
        ElszigeteltTekton elszigeteltTekton  = new ElszigeteltTekton();

        System.out.println("Stabil tekton fonalak szama: " + stabilTekton.getFonalak().size());
        System.out.println("Elszigetelt tekton fonalak szama: " + elszigeteltTekton.getFonalak().size());

        //Sporak hozaadasa a noveszteshez
        stabilTekton.addSpora(new Spora());
        stabilTekton.addSpora(new Spora());
        stabilTekton.addSpora(new Spora());
        stabilTekton.addSpora(new Spora());
        

        //Fonal novesztese
        System.out.println("Ketto fonal novesztese a ket tekton kozt");
        stabilTekton.growFonal(elszigeteltTekton, "Teszt");
        stabilTekton.growFonal(elszigeteltTekton, "Teszt");


        System.out.println("Stabil tekton fonalak szama: " + stabilTekton.getFonalak().size());
        System.out.println("Elszigetelt tekton fonalak szama: " + elszigeteltTekton.getFonalak().size());

    }

    static void splitTestCase(){

        Tekton test1 = new Tekton(1.0, new GombaTest());
        Tekton test2 = new Tekton(0, new GombaTest());

        test2.split();

        System.out.println("0.0 split rate-el rendelkezo tekton canSplit() erdemnye: " + test2.canSplit());
        System.out.println("1.0 split rate-el rendelkezo tekton canSplit() erdemnye: " + test1.canSplit());

        System.out.println("Az eltorheto tekton eltorese");

        Tekton ujTekton = test1.split();

        if(ujTekton != null) System.out.println("A tekton sikeresen eltort");
        else System.out.println("A tekton eltorese nem sikerult");


        System.out.println("Az uj tekton tipusa: " + ujTekton.getClass());

    }


    /**
     * Teszteset a GombaTest fejlesztés ellenőrzésére.
     *
     * A teszt ellenőrzi az alábbi eseteket:
     * 1. Ha a GombaTest fejlettségi szintje <4, akkor képes fejlődni egy szintet.
     * 2. Ha a GombaTest fejlettségi szintje 4-es, akkor már nem lehet tovább fejleszteni.
     */
    public static void gombaTestFejlesztesTestCase() {


        // Initialization
        // --------------
        // Creating a Tekton
        Tekton tekton = new Tekton();

        // Adding GombaTest to Tekton
        GombaTest gt = new GombaTest(tekton, "Gomba1");
        System.out.println("A gombatest fejlettségi szintje: " + gt.getLevel());

        // setting GombaTest's level 3
        System.out.println("A gombatest fejlettségi szintjét 3-ra állítjuk");
        gt.setLevel(3);
        System.out.println("A gombatest fejlettségi szintje: " + gt.getLevel());


        // Test case if GombaTest is upgradeable
        System.out.println("A gombatestet fejlesztjük");

        gt.upgradeTest();
        System.out.println("Teszt: ha a GombaTest szintje 4, a fejlesztés sikeres volt.");
        System.out.println("A gombatest fejlettségi szintje: " + gt.getLevel());

    

        // Test case if GombaTest is not upgradeable
        gt.upgradeTest();
        System.out.println("Teszt: ha a GombaTest szintje még mindig 4, akkor sikeres a teszt.");
        System.out.println("A gombatest fejlettségi szintje: " + gt.getLevel());

    }

    /**
     * Teszteset a GombaTest növesztés ellenőrzésére.
     * A teszt ellenőrzi az alábbi eseteket:
     * 1. Egy gombatestet növesztünk a tektonra.
     * 2. Ha van spóra a tektonon, növesztünk gombatestet.
     */
    public static void gombaTestNovesztesTestCase() {
        // Létrehozunk egy tektont
        Tekton tekton1 = new Tekton();
        System.out.println("A tekton gombatesteinek száma: " + tekton1.getGombaTestek().size());
        // Növesztünk rá egy gombatestet
        System.out.println("Növesztünk egy gombatestet a tektonra");
        GombaTest gt1 = new GombaTest(tekton1, "gomba1");
        tekton1.addGombaTest(gt1);
        System.out.println("A tekton gombatesteinek száma: " + tekton1.getGombaTestek().size());


        // Létrehozunk egy tektont
        Tekton tekton2 = new Tekton();
        tekton2.addSpora(new Spora());
        System.out.println("A tekton gombatesteinek száma: " + tekton1.getGombaTestek().size());
        System.out.println("A tekton spóráinak száma: " + tekton2.getSporak().size());
        // Növesztünk rá egy gombatestet
        GombaTest gt2 = new GombaTest(tekton1, "gomba1");
        tekton2.addGombaTest(gt2);
        tekton2.removeSpora();
        System.out.println("A tekton gombatesteinek száma: " + tekton1.getGombaTestek().size());
        System.out.println("A tekton spóráinak száma: " + tekton2.getSporak().size());

    }




    /**
     * Teszteset a Rovar mozgatásának ellenőrzésére.
     *
     * A teszt ellenőrzi az alábbi eseteket:
     * 1. Ha a rovar bénult, nem mozdulhat el.
     * 2. Ha a cél tekton nem elérhető, a mozgás sikertelen.
     * 3. Sikeres mozgás esetén a rovar áthelyeződik az új tektonra.
     */
    static void rovarMoveTestCase(){
        Logger.log("\n==========================");
        Logger.log("Teszt: Rovar mozgás teszteset indítása");
        Logger.log("==========================");

        Logger.log("Initializing test environment for Rovar move");

        Logger.setEnabled(false);
        // Initialization
        // --------------
        Tekton tekton = new Tekton();
        Tekton dstTekton = new Tekton();
        GombaFonal gf = new GombaFonal(tekton, dstTekton, "Teszt");
        Rovar r = new Rovar(tekton,"Teszt");

        // Add neighbour connections between tekton and dstTekton
        tekton.addNeighbour(dstTekton);
        dstTekton.addNeighbour(tekton);

        // Add rovar to tekton
        tekton.addRovar(r);

        // Add Gombafonal between tekton and dstTekton
        tekton.addGombaFonal(gf);
        dstTekton.addGombaFonal(gf);

        // Initialization end
        // ------------------
        Logger.setEnabled(true);

        // Test case if Rovar is paralyzed
        Logger.log("\n==> Test: Rovar is paralyzed, should not move.");
        r.setParalyzed(true);
        r.move(dstTekton);

        // Test case if destination tekton is unreachable
        Logger.log("\n==> Test: Destination tekton is unreachable, should not move.");
        r.setParalyzed(false);
        Tekton unreachableTekton = new Tekton();
        r.move(unreachableTekton);

        // Successful moving
        Logger.log("\n==> Test: Successful move.");
        r.move(dstTekton);

        Logger.log("\n==========================");
        Logger.log("Rovar mozgás teszteset vége");
        Logger.log("==========================");
    }

    /**
     * Teszteset a spóra termelésének ellenőrzésére.
     *
     * A teszt célja, hogy validálja, a gombatest képes-e spórát termelni és azt a megfelelő tektonra helyezni.
     * A teszt a következőket vizsgálja:
     *
     * 1. A spóra termelése sikeresen növeli a tektonon található spórák számát eggyel.
     * 2. A gombatesthez tartozó tekton megfelelően frissül az új spórával.
     */

    static void testProduceSpora() {
        Logger.log("\n==========================");
        Logger.log("Teszt: Spóra termelés");
        Logger.log("==========================");

        // Inicializálás
        StabilTekton tekton = new StabilTekton(0.0, new GombaTest());
        GombaTest gt = tekton.getTestek().get(0);
        gt.setTekton(tekton);

        // Spóra termelés
        gt.produceSpora();

        // Eredmény ellenőrzése
        System.out.println("Spórák száma a tektonon: " + tekton.getSporak().size());

        Logger.log("==========================");
        Logger.log("Spóra termelés teszt vége");
        Logger.log("==========================");
    }



    /**
     * Teszteset a rovar gombafonal elvágásának ellenőrzésére.
     * A teszt két fő esetet vizsgál:
     * 1. A rovar bénult állapotban van, ezért nem képes elvágni a gombafonalat.
     * 2. A rovar nem bénult, ezért sikeresen elvágja a gombafonalat, amely eltávolításra kerül a kapcsolódó tektonokból.
     */
    public static void cutGombaFonalTestCase(){
        Logger.log("\n==========================");
        Logger.log("Teszt: Gombafonal elvágás teszteset indítása");
        Logger.log("==========================");

        Logger.log("Initializing test environment for cutting Gombafonal");

        Logger.setEnabled(false);
        // Initialization
        // --------------
        Tekton src = new Tekton();
        Tekton dst = new Tekton();
        GombaFonal gf = new GombaFonal(src,dst,"Teszt");
        Rovar r = new Rovar(src, "Teszt");

        // add neighbour connection between src and dst
        src.addNeighbour(dst);
        dst.addNeighbour(src);

        // add gombafonal between src and dst
        src.addGombaFonal(gf);
        dst.addGombaFonal(gf);

        // Initialization end
        // ------------------
        Logger.setEnabled(true);

        // Test case if Rovar is paralyzed
        Logger.log("\n==> Test: Rovar is paralyzed, should not move.");
        r.setParalyzed(true);
        r.cutGombaFonal(gf);

        // Test successful cut
        Logger.log("\n==> Test: Successful cutting gombafonal.");
        r.setParalyzed(false);
        r.cutGombaFonal(gf);

        Logger.log("\n==========================");
        Logger.log("Gombafonal elvágás teszteset vége");
        Logger.log("==========================");
    }

    static void testEatBenitoSpora(){
        Logger.log("Initializing test environment for EatBenitoSpora");

        Logger.setEnabled(false);
        // Initialization
        // --------------
        Tekton src = new Tekton();
        BenitoSpora sp = new BenitoSpora();
        Rovar r = new Rovar(src, "Teszt");

        // Adding entities to the src tekton
        src.addRovar(r);
        src.addSpora(sp);

        // Initialization end
        // ------------------
        Logger.setEnabled(true);

        // Test case if Rovar is paralyzed
        Logger.log("\n==> Test: Rovar is paralyzed, should not move.");
        r.setParalyzed(true);
        r.eatSpora(sp);

        // Test successful eating spora
        Logger.log("\n==> Test: Successful eating spora.");
        r.setParalyzed(false);
        r.eatSpora(sp);
    }

    static void testEatLassitoSpora(){
        Logger.log("Initializing test environment for EatLassitoSpora");

        Logger.setEnabled(false);
        // Initialization
        // --------------
        Tekton src = new Tekton();
        LassitoSpora sp = new LassitoSpora();
        Rovar r = new Rovar(src, "Teszt");

        // Adding entities to the src tekton
        src.addRovar(r);
        src.addSpora(sp);

        // Initialization end
        // ------------------
        Logger.setEnabled(true);

        // Test case if Rovar is paralyzed
        Logger.log("\n==> Test: Rovar is paralyzed, should not move.");
        r.setParalyzed(true);
        r.eatSpora(sp);

        // Test successful eating spora
        Logger.log("\n==> Test: Successful eating spora.");
        r.setParalyzed(false);
        r.eatSpora(sp);
    }

    static void testEatGyoritoSpora(){
        Logger.log("Initializing test environment for EatGyorsitoSpora");

        Logger.setEnabled(false);
        // Initialization
        // --------------
        Tekton src = new Tekton();
        GyorsitoSpora sp = new GyorsitoSpora();
        Rovar r = new Rovar(src, "Teszt");

        // Adding entities to the src tekton
        src.addRovar(r);
        src.addSpora(sp);

        // Initialization end
        // ------------------
        Logger.setEnabled(true);

        // Test case if Rovar is paralyzed
        Logger.log("\n==> Test: Rovar is paralyzed, should not move.");
        r.setParalyzed(true);
        r.eatSpora(sp);

        // Test successful eating spora
        Logger.log("\n==> Test: Successful eating spora.");
        r.setParalyzed(false);
        r.eatSpora(sp);
    }

    /**
     * Teszteset a játék végének ellenőrzésére pontszám alapján.
     *
     * A játék akkor ér véget, ha bármelyik játékos (A vagy B) eléri a maximális pontszámot.
     * Ha ez megtörtént, az a csapat nyer, amelyiknek több pontja van.
     */
    /**
     * Teszteset a játék végének ellenőrzésére, amikor A játékos eléri a maximális pontszámot.
     */
    static void testJatekVege() {
        Logger.log("\n==========================");
        Logger.log("Teszt: Játék vége (A játékos eléri a maximális pontszámot)");
        Logger.log("==========================");

        int maxScore = 10;

        int scoreA = 10;  // A játékos pontszáma
        int scoreB = 8;   // B játékos pontszáma

        if (scoreA >= maxScore || scoreB >= maxScore) {
            Logger.log("A játék véget ér!");

            if (scoreA > scoreB) {
                Logger.log("A játékos A több pontot ért el.");
            } else if (scoreB > scoreA) {
                Logger.log("A játékos B több pontot ért el.");
            } else {
                Logger.log("Mindkét játékos ugyanannyi pontot ért el (döntetlen).");
            }

        } else {
            Logger.log("A játék folytatódik.");
        }

        Logger.log("==========================");
        Logger.log("Játék vége teszt vége");
        Logger.log("==========================");
    }

    /**
     * Teszteset: A játék véget ér, ha elfogytak a GombaTest példányok.
     */
    static void testJatekVegeElfogyottGombaTest() {
        Logger.log("\n==========================");
        Logger.log("Teszt: Játék vége (elfogyott az összes GombaTest)");
        Logger.log("==========================");

        List<GombaTest> gombatestek = new ArrayList<>(); // üres lista

        if (gombatestek.isEmpty()) {
            Logger.log("A játék véget ér: nincs több GombaTest a pályán.");
        } else {
            Logger.log("A játék folytatódik: van még legalább egy GombaTest.");
        }

        Logger.log("==========================");
        Logger.log("Játék vége teszt vége");
        Logger.log("==========================");
    }



}