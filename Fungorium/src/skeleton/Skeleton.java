package Fungorium.src.skeleton;

import java.util.Scanner;

import Fungorium.src.model.GombaTest;
import Fungorium.src.model.spora.Spora;
import Fungorium.src.model.GombaFonal;
import Fungorium.src.model.Rovar;
import Fungorium.src.model.spora.*;
import Fungorium.src.model.tekton.ElszigeteltTekton;
import Fungorium.src.model.tekton.StabilTekton;
import Fungorium.src.model.tekton.Tekton;
import Fungorium.src.utility.Logger;

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
            System.out.println("3. ....");
            System.out.println("4. ....");
            System.out.println("5. ....");
            System.out.println("6. Rovar mozgatása");
            System.out.println("7. Gombafonal elvágása");
            System.out.println("8. Lassító spóra megevése");
            System.out.println("9. Gyorsító spóra megevése");
            System.out.println("10. Bénító spóra megevése");
            System.out.println("11. ....");

    
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
            case "1":
                fonalNoveszteseTestCase();
                break;
            case "2":
                splitTestCase();
                break;
            case "3":
                
                break;
            case "4":

                break;
            case "5":

                break;
            case "6":
                rovarMoveTestCase();
                break;
            case "7":
                cutGombaFonalTestCase();
                break;
            case "8":
                testEatLassitoSpora();
                break;
            case "9":
                testEatGyoritoSpora();;
                break;
            case "10":
                testEatBenitoSpora();
                break;
            case "11":

                break;
            case "12":

                break;
        }
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

        Tekton ujTekton = null;

        ujTekton = test1.split();

        if(ujTekton != null) System.out.println("A tekton sikeresen eltort");
        else System.out.println("A tekton eltorese nem sikerult");


        System.out.println("Az uj tekton tipusa: " + ujTekton.getClass());

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
}