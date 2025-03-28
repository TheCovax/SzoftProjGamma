package Fungorium.src.model;
import Fungorium.src.model.spora.Spora;
import Fungorium.src.model.tekton.*;
import Fungorium.src.utility.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 *  A Rovarászok által irányított karakterek.
 *  A Rovar képes mozogni a tektonok között, spórákat fogyasztani és gombafonalakt vágni.
 */
public class Rovar {
    public static final int DEFAULT_SPEED = 2;

    private Tekton tekton;
    private boolean isParalyzed;
    private int speed;
    private int duration;
    private int collectedNutrition;
    private String owner;

    public Rovar(Tekton startTekton, String owner){
        this.tekton = startTekton;
        this.owner = owner;
        this.isParalyzed = false;
        this.speed = DEFAULT_SPEED;
        this.duration = 0;
        this.collectedNutrition = 0;
    }

    /**
     * Megpróbálja a rovart a megadott cél tektonra mozgatni.
     * @param dstTekton A cél tekton.
     * @return Igaz, ha a mozgás sikeres, egyébként hamis.
     */
    public boolean move(Tekton dstTekton){
        Logger.methodCall("r.move(dstTekton)");

        // 1. Check paralyzed
        if (isParalyzed()){
            Logger.log("Rovar is paralyzed, cannot move.");
            Logger.methodReturn("r.move(dstTekton): false");
            return false;
        }

        // 2. Attempt to get reachable tektons
        List<Tekton> reachableTektons = tekton.findReachableTektonWithinDistance(speed);

        // 3. Check if destination is reachable
        if (!reachableTektons.contains(dstTekton)){
            Logger.log("Destination tekton is not reachable.");
            Logger.methodReturn("r.move(dstTekton): false");
            return false;
        }

        // 4. Execute move
        Logger.methodCall("tekton.removeRovar(r)");
        tekton.removeRovar(this);
        Logger.methodReturn("tekton.removeRovar(r)");

        Logger.methodCall("tekton.addRovar(r)");
        dstTekton.addRovar(this);
        Logger.methodReturn("tekton.addRovar(r)");

        Logger.methodCall("r.setTekton(dstTekton)");
        this.setTekton(dstTekton);
        Logger.methodReturn("r.setTekton(dstTekton)");


        Logger.methodReturn("r.move(dstTekton): true");
        return true;
    }

    /**
     * Elfogyasztja a megadott spórát és alkalmazza annak effektjeit.
     * @param sp A fogyasztandó spóra.
     * @return Igaz, ha a spóra elfogyasztása sikeres volt, egyébként hamis.
     */
    public boolean eatSpora(Spora sp){
        Logger.methodCall("r.eatSpora()");

        // 1. Check if rovar is paralyzed
        if (isParalyzed()){
            Logger.log("Rovar is paralyzed, cannot move.");
            Logger.methodReturn("r.eatSpora(): false");
            return false;
        }

        // 2. Apply spore effect on this rovar
        sp.applyEffect(this);

        // 3. Add spore nutrition to collectedNutrition (optional placeholder)
        Logger.log("Collecting nutrition from spora.");
        collectedNutrition += sp.getNutrition();

        // 4. remove spora from tekton
        tekton.removeSpora();

        Logger.methodReturn("r.eatSpora(): true");
        return false;
    }

    /**
     * Elvágja a megadott gombafonalat.
     * @param gf Az átvágandó gombafonal.
     * @return Igaz, ha az átvágás sikeres, egyébként hamis.
     */
    public boolean cutGombaFonal(GombaFonal gf){
        Logger.methodCall("r.cutGombaFonal(gf)");

        if (isParalyzed()){
            Logger.log("Rovar is paralyzed, cannot move.");
            Logger.methodReturn("r.cutGombaFonal(gf): false");
            return false;
        }

        // If tekton does not have this gf (Maybe not needed)
        if(!tekton.hasGombafonal(gf)){
            Logger.log("Gombafonal not found on current tekton.");
            Logger.methodReturn("r.cutGombaFonal(gf): false");
            return false;
        }

        // Execute cutting gombafonal
        Logger.methodCall("gf.clean()");
        gf.clean();
        Logger.methodReturn("gf.clean()");

        Logger.methodReturn("r.cutGombaFonal(gf): true");
        return true;
    }

    public Tekton getTekton() {
        return tekton;
    }

    public void setTekton(Tekton tekton) {
        this.tekton = tekton;
    }

    public boolean isParalyzed() {
        Logger.methodCall("r.isParalyzed()");
        Logger.methodReturn("r.isParalyzed()");
        return isParalyzed;
    }

    public void setParalyzed(boolean paralyzed) {
        isParalyzed = paralyzed;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        Logger.methodCall("r.setSpeed(int)");
        Logger.methodReturn("r.setSpeed(int)");
        this.speed = speed;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        Logger.methodCall("r.setDuration(effectDuration)");
        Logger.methodReturn("r.setDuration(effectDuration)");
        this.duration = duration;
    }

    public int getCollectedNutrition() {
        return collectedNutrition;
    }

    public void setCollectedNutrition(int collectedNutrition) {
        this.collectedNutrition = collectedNutrition;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

}
