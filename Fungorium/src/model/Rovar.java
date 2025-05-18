package Fungorium.src.model;
import Fungorium.src.model.observer.Observable;
import Fungorium.src.model.player.Player;
import Fungorium.src.model.player.Rovarasz;
import Fungorium.src.model.spora.Spora;
import Fungorium.src.model.tekton.*;

import javax.print.DocFlavor;
import java.util.List;

/**
 * Represents a Rovar Entity controlled by players.
 * A Rovar can move between Tektons, consume spora, and cut Gombafonal.
 */
public class Rovar extends Entity{

    public static final int DEFAULT_SPEED = 4;

    private Tekton tekton;
    private boolean isParalyzed;
    private int speed;
    private int duration;
    private int collectedNutrition;
    private int remainingActions;

    /**
     * Constructs a Rovar with a specified ID, starting Tekton, and owner.
     *
     * @param id          The unique ID of the Rovar.
     * @param tekton The starting Tekton where the Rovar is placed.
     * @param owner       The player who owns this Rovar.
     */
    public Rovar(String id, Tekton tekton, Player owner){
        super(id, owner);
        this.tekton = tekton;
        this.isParalyzed = false;
        this.speed = DEFAULT_SPEED;
        this.duration = 0;
        this.collectedNutrition = 0;
        this.remainingActions = speed;
    }

    /**
     * Constructs a Rovar with a generated ID, starting Tekton, and owner.
     *
     * @param tekton The starting Tekton where the Rovar is placed.
     * @param owner  The player who owns this Rovar.
     */
    public Rovar(Tekton tekton, Player owner) {
        super(owner);
        this.tekton = tekton;
        this.isParalyzed = false;
        this.speed = DEFAULT_SPEED;
        this.duration = 0;
        this.collectedNutrition = 0;
        this.remainingActions = speed;
    }

    /**
     * Attempts to move the Rovar to a neighboring Tekton.
     *
     * @param dstTekton The destination Tekton.
     * @return true if the move was successful; false otherwise.
     */
    public boolean move(Tekton dstTekton){

        // Check if the move is valid
        // --------------------------
        if (!canAct()){
            return false;
        }

        if (dstTekton == null){
            System.out.println("Move rovar unsuccessful: destination desTekton is null");
            return false;
        }


        // Attempt to get reachable tektons
        List<Tekton> reachableTektons = tekton.findReachableTektonWithinDistance(1);
        // Check if destination is reachable
        if (!reachableTektons.contains(dstTekton)){
            System.out.println("Move Rovar unsuccessful: destination Tekton unreachable");
            return false;
    }


        // Move valid, execute move
        // ------------------------
        tekton.removeRovar(this);
        dstTekton.addRovar(this);
        this.setTekton(dstTekton);
        remainingActions--;

        System.out.println("Successfuly moved " + ID + " to " + dstTekton.getID());
        notifyObservers();
        return true;
    }

    /**
     * Consumes a Spora and applies its effect.
     *
     * @param sp The Spora to be consumed.
     * @return true if the Spora was successfully consumed; false otherwise.
     */
    public boolean eatSpora(Spora sp){

        // Check is eating spora action is valid
        // -------------------------------------
        if (!canAct()){
            return false;
        }

        // Action valid, execute eating spora
        // Execute eating Spora
        sp.applyEffect(this);
        collectedNutrition += sp.getNutrition();
        tekton.removeSpora();
        remainingActions--;

        System.out.println(ID+ " successfully ate Spora");
        notifyObservers();
        return true;
    }

    /**
     * Attempts to cut a GombaFonal on the current Tekton.
     *
     * @param gf The GombaFonal to cut.
     * @return true if the GombaFonal was successfully cut; false otherwise.
     */
    public boolean cutGombaFonal(GombaFonal gf){

        // Check if action is valid
        // ------------------------
        if (!canAct()){
            return false;
        }

        if(!tekton.hasGombafonal(gf)){
            System.out.println("Cutting " + gf.getID() + " failed, it does not exist on the tekton");
            return false;
        }

        // Execute cutting gombafonal
        // --------------------------
        if (!gf.cut()){ // Schedule gombafonal for cutting
            System.out.println("Cutting " + gf.getID() + " failed, its already under Cutting State");
            return false;
        }else {
            remainingActions--;
            System.out.println("Cutting " + gf.getID() + " successful, it will be destroyed soon");
            notifyObservers();
            return true;
        }
    }

    /**
     * Splits the Rovar to create a new Rovar on the same Tekton.
     * The new Rovar is added to the current Tekton and owner.
     */
    public void split(){
        Rovar r2 = new Rovar(tekton,owner);
        tekton.addRovar(r2);
        ((Rovarasz) owner).addRovar(r2);

        System.out.println("New born Rovar: " + r2.getID() + "!");
        notifyObservers();
    }

    /**
     * Updates the Rovar state at the end of each round.
     * Resets paralysis if duration expired, and restores remaining actions.
     */
    @Override
    public void update(){
        if(duration > 0){
            duration--;
            if (duration <= 0){
                isParalyzed = false;
                speed = DEFAULT_SPEED;
            }
        }

        remainingActions = speed;
    }

    @Override
    protected String getPrefix() {
        return "R";
    }

    @Override
    public void delete() {
        tekton.removeRovar(this);
        ((Rovarasz) owner).removeRovar(this);
    }

    @Override
    public String toString(){
        return "Rovar{" +
                "ID='" + getID() + '\'' +
                ", Owner=" + (owner != null ? owner.getName() : "None") +
                ", CurrentTekton=" + (tekton != null ? tekton.getID() : "None") +
                ", Speed=" + speed +
                ", IsParalyzed=" + isParalyzed +
                ", CollectedNutrition=" + collectedNutrition +
                '}';
    }

    public Tekton getTekton() {
        return tekton;
    }

    public void setTekton(Tekton tekton) {
        this.tekton = tekton;
    }

    public boolean isParalyzed() {
        return isParalyzed;
    }

    public void setParalyzed(boolean paralyzed) {
        isParalyzed = paralyzed;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public int getCollectedNutrition() {
        return collectedNutrition;
    }

    public void setCollectedNutrition(int collectedNutrition) {
        this.collectedNutrition = collectedNutrition;
    }

    public int getRemainingActions() {
        return remainingActions;
    }

    private boolean canAct(){
        if (isParalyzed()){
            System.out.println("Action failed:" + ID + "is paralyzed");
            return false;
        }

        if (remainingActions <= 0){
            System.out.println("Action failed: no remaining action for " + ID);
            return false;
        }

        return true;
    }

}
