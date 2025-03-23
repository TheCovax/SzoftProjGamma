package model;
import model.spora.Spora;
import model.tekton.*;
import utility.Logger;

import java.util.ArrayList;
import java.util.List;

public class Rovar {

    private Tekton tekton;
    private boolean isParalyzed;
    private int speed;
    private int duration;
    private int collectedNutrition;
    private String owner;

    public Rovar(){
    }

    public boolean move(Tekton dst){
        Logger.methodCall("Rovar.move(dstTekton)");

        // 1. Check paralyzed
        if (isParalyzed()){
            //Logger.log("Rovar is paralyzed, cannot move.");
            Logger.methodReturn("Rovar.move(dstTekton): false");
            return false;
        }

        // 2. Attempt to get reachable tektons
        // TODO: waiting for Tekton class to be finished
        List<Tekton> reachableTektons = new ArrayList<Tekton>();

        // 3. Check if destination is reachable
        if (!reachableTektons.contains(dst)){
            //Logger.log("Destination tekton is not reachable.");
            Logger.methodReturn("Rovar.move(dstTekton): false");
            return false;
        }

        // 4. Execute move
        /*
        * Logger.log("Removing rovar from current tekton.");
         tekton.removeRovar(this);

        Logger.log("Adding rovar to destination tekton.");
        dstTekton.addRovar(this);

        Logger.log("Updating rovar's tekton reference.");
        this.tekton = dstTekton;
        * */

        Logger.methodReturn("Rovar.move(dstTekton): true");
        return true;
    }

    public boolean eatSpora(Spora sp){
        Logger.methodCall("Rovar.eatSpora()");

        // 1. Check if rovar is paralyzed
        if (isParalyzed()){
            Logger.log("Rovar is paralyzed, cannot move.");
            Logger.methodReturn("Rovar.eatSpora(): false");
            return false;
        }

        // 2. Apply spore effect on this rovar
        sp.applyEffect(this);

        // 3. Add spore nutrition to collectedNutrition (optional placeholder)
        Logger.log("Collecting nutrition from spora (placeholder).");
        // collectedNutrition += sp.getNutrition(); // once Spora has getter

        // 4. remove spora from tekton

        Logger.methodReturn("Rovar.eatSpora(): true");
        return false;
    }

    public boolean cutGombaFonal(GombaFonal gf){
        Logger.methodCall("Rovar.cutGombaFonal(gf)");

        if (isParalyzed()){
            Logger.log("Rovar is paralyzed, cannot move.");
            Logger.methodReturn("Rovar.cutGombaFonal(gf): false");
            return false;
        }

        // If tekton does not have this gf
        // TODO: replace with actual call once Tekton is implemented:
        //return false;


        // Execute clean
        // TODO: replace with actual call once Tekton is implemented:
        // gf.clean();

        Logger.methodReturn("Rovar.cutGombaFonal(gf): true");
        return true;
    }

    public Tekton getTekton() {
        return tekton;
    }

    public void setTekton(Tekton tekton) {
        this.tekton = tekton;
    }

    public boolean isParalyzed() {
        Logger.methodCall("Rovar.isParalyzed()");
        Logger.methodReturn("Rovar.isParalyzed()");
        return isParalyzed;
    }

    public void setParalyzed(boolean paralyzed) {
        isParalyzed = paralyzed;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        Logger.methodCall("Rovar.setSpeed()");
        Logger.methodReturn("Rovar.setSpeed()");
        this.speed = speed;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        Logger.methodCall("Rovar.setDuration()");
        Logger.methodReturn("Rovar.setDuration()");
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
