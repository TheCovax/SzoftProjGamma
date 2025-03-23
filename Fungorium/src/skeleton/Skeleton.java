package Fungorium.src.skeleton;

import java.util.Scanner;

import Fungorium.src.model.GombaTest;
import Fungorium.src.model.spora.Spora;
import Fungorium.src.model.tekton.ElszigeteltTekton;
import Fungorium.src.model.tekton.StabilTekton;
import Fungorium.src.model.tekton.Tekton;

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
}