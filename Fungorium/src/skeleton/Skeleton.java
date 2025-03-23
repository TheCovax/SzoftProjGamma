package skeleton;

import model.GombaFonal;
import model.Rovar;
import model.spora.*;
import model.tekton.Tekton;
import utility.Logger;

public class Skeleton {
    public static void main(String[] args) {
        System.out.println(Runtime.version());

        //testMoveRovar();
        testEatLassitoSpora();
    }

    public static void testMoveRovar(){

        Logger.log("Initializing test environment for Rovar move");

        // Initialization
        Tekton tekton = new Tekton();
        Tekton dstTekton = new Tekton();
        GombaFonal gf = new GombaFonal();
        Rovar r = new Rovar();

        // Add neighbour connections between tekton and dstTekton

        // Add Gombafonal between tekton and dstTekton

        // Test case if Rovar is paralyzed
        Logger.log("\nTest: Rovar is paralyzed, should not move.");
        r.setParalyzed(true);
        r.move(dstTekton);

        // Test case if destination tekton is unreachable
        Logger.log("\nTest: Destination tekton is unreachable, should not move.");
        Tekton unreachableTekton = new Tekton();
        r.move(unreachableTekton);

        // Successful moving
        Logger.log("\nTest: Successful move.");
        r.setParalyzed(false);
        r.move(dstTekton);

    }

    public static void testCutGombaFonal(){
        Logger.log("Initializing test environment for cutting Gombafonal");

        // Initialization
        Tekton src = new Tekton();
        Tekton dst = new Tekton();
        GombaFonal gf = new GombaFonal();
        Rovar r = new Rovar();

        // Neighbour
        // TODO: add neighbour connections

        // Test case if Rovar is paralyzed
        Logger.log("\nTest: Rovar is paralyzed, should not move.");
        //r.setParalyzed(true);
        r.cutGombaFonal(gf);

        // Test if gombafonal is not present on tekton

        // Test successful cut
    }

    public static void testEatBenitoSpora(){

    }

    public static void testEatLassitoSpora(){
        Tekton src = new Tekton();
        LassitoSpora sp = new LassitoSpora();
        Rovar r = new Rovar();

        // TODO: add connections

        r.setParalyzed(false);

        r.eatSpora(sp);
    }

    public static void testEatGyoritoSpora(){

    }
}