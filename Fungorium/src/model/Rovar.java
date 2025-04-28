package Fungorium.src.model;
import Fungorium.src.model.player.Player;
import Fungorium.src.model.player.Rovarasz;
import Fungorium.src.model.spora.Spora;
import Fungorium.src.model.tekton.*;
import Fungorium.src.utility.Logger;
import java.util.List;

/**
 * Represents a Rovar Entity controlled by players.
 * A Rovar can move between Tektons, consume spora, and cut Gombafonal.
 */
public class Rovar extends Entity{
    public static final int DEFAULT_SPEED = 2;

    private Tekton tekton;
    private boolean isParalyzed;
    private int speed;
    private int duration;
    private int collectedNutrition;
    private int remainingActions;

    /**
     * Constructs a Rovar with a specific ID, Tekton, and owner.
     */
    public Rovar(String id, Tekton startTekton, Player owner){
        super(id, owner);
        this.tekton = startTekton;
        this.isParalyzed = false;
        this.speed = DEFAULT_SPEED;
        this.duration = 0;
        this.collectedNutrition = 0;
        this.remainingActions = speed;
    }

    /**
     * Constructs a Rovar with a generated ID and given Tekton and owner.
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
     * Megpróbálja a rovart a megadott cél tektonra mozgatni.
     * @param dstTekton A cél tekton.
     * @return Igaz, ha a mozgás sikeres, egyébként hamis.
     */
    public boolean move(Tekton dstTekton){

        // Check paralyzed
        if (isParalyzed())
            return false;

        // Check if dstTekton is null
        if (dstTekton == null)
            return false;

        // Check still remaining actions
        if (remainingActions <= 0)
            return false;

        // Attempt to get reachable tektons
        List<Tekton> reachableTektons = tekton.getNeighbours();

        // Check if destination is reachable
        if (!reachableTektons.contains(dstTekton))
            return false;

        // Execute move
        tekton.removeRovar(this);
        dstTekton.addRovar(this);
        this.setTekton(dstTekton);
        remainingActions--;

        return true;
    }

    /**
     * Elfogyasztja a megadott spórát és alkalmazza annak effektjeit.
     * @param sp A fogyasztandó spóra.
     * @return Igaz, ha a spóra elfogyasztása sikeres volt, egyébként hamis.
     */
    public boolean eatSpora(Spora sp){

        // Check if rovar is paralyzed
        if (isParalyzed())
            return false;

        // Check still remaining actions
        if (remainingActions <= 0)
            return false;

        // Execute eating Spora
        sp.applyEffect(this);
        collectedNutrition += sp.getNutrition();
        tekton.removeSpora();
        remainingActions--;

        return true;
    }

    /**
     * Elvágja a megadott gombafonalat.
     * @param gf Az átvágandó gombafonal.
     * @return Igaz, ha az átvágás sikeres, egyébként hamis.
     */
    public boolean cutGombaFonal(GombaFonal gf){

        if (isParalyzed()){
            return false;
        }

        // Check still remaining actions
        if (remainingActions <= 0)
            return false;

        // If tekton does not have this gf (Maybe not needed)
        if(!tekton.hasGombafonal(gf)){
            return false;
        }

        // Execute cutting gombafonal
        // successful holds if the gombafonal is successfully scheduled for destruction
        if (!gf.cut()){
            return false;
        }else {
            remainingActions--;
            return true;
        }
    }

    /**
     * Egy uj rovart hoz letre, ugyan arra a tektonra
     */
    public void split(){
        Rovar r2 = new Rovar(tekton,owner);
        tekton.addRovar(r2);
        ((Rovarasz) owner).addRovar(r2);
    }

    @Override
    protected String getPrefix() {
        return "R";
    }

    /**
     * Frissitit a rovar allapotat minden kor utan
     * Ha van letelt a duration visszaallitja a rovart alap allapotba
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

}
