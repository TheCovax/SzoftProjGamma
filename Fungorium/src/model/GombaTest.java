package Fungorium.src.model;

import Fungorium.src.model.player.Gombasz;
import Fungorium.src.model.player.Player;
import Fungorium.src.model.spora.BenitoSpora;
import Fungorium.src.model.spora.GyorsitoSpora;
import Fungorium.src.model.spora.LassitoSpora;
import Fungorium.src.model.spora.OsztoSpora;
import Fungorium.src.model.spora.Spora;
import Fungorium.src.model.tekton.Tekton;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a GombaTest placed on a Tekton,
 * owned by a player. It can produce and shoot Sporas,
 * and grow in level using collected Sporas.
 */
public class GombaTest extends Entity{

    private Tekton tekton;
    private int level;
    private int shotCounter;

    // Spora costs for actions
    private static final int SHOOTSPORA_COST = 2;
    private static final int UPGRADE_COST = 4;


    /**
     * Constructs a new GombaTest with a generated ID.
     *
     * @param tekton The Tekton where it is located.
     * @param owner  The owner player.
     */
    public GombaTest(Tekton tekton, Player owner) {
        super(owner);
        this.tekton = tekton;
        this.level = 1;
        this.shotCounter = 8;
    }

    /**
     * Constructs a new GombaTest with a specific ID.
     *
     * @param id     The unique ID of the GombaTest.
     * @param tekton The Tekton where it is located.
     * @param owner  The owner player.
     */
    public GombaTest(String id, Tekton tekton, Player owner) {
        super(id, owner);
        this.tekton = tekton;
        this.level = 1;
        this.shotCounter = 8;
    }

    /**
     * Produces a new random Spora and places it on the Tekton.
     */
    public void produceSpora() {

        // 1. Spóra típus véletlenszerű kiválasztása
        int r = (int) (Math.random() * 4);
        Spora spora;

        switch (r) {
            case 0 -> spora = new GyorsitoSpora();
            case 1 -> spora = new LassitoSpora();
            case 2 -> spora = new BenitoSpora();
            case 3 -> spora = new OsztoSpora();
            default -> {
                System.out.println("error");
                return;
            }
        }

        // 2. Spóra hozzáadása a megfelelő Tektonhoz
        if (tekton != null) {
            tekton.addSpora(spora, owner);
        } 
        notifyObservers();
    }

    /**
     * Egy szintel fejleszti a gombatestet
     */
    public void upgradeTest() {
        if (tekton == null) {
            System.out.println("No Tekton assigned!");
            return;
        }

        if (tekton.getSporak().size() < UPGRADE_COST) {
            System.out.println("Not enough spora to upgrade! (Need at least 4 sporak)");
            return;
        }

        if (level >= 4) {
            System.out.println("Maximum level reached!");
            return;
        }

        setLevel(level + 1);
        tekton.removeSpora(); // Eltávolítunk 4 spórát összesen
        tekton.removeSpora();
        tekton.removeSpora();
        tekton.removeSpora();

        System.out.println("Successfully upgraded GombaTest to level " + level + "!");
        notifyObservers();
    }

    //beallitja az elerheto tektonok isConnected valtozojat igazra
    void setConnectivity() {

        List<Tekton> nodes = tekton.checkConnectivity(owner);
        while(!nodes.isEmpty()){
            List<Tekton> curr = new ArrayList<>();
            for(Tekton node : nodes){
                for(Tekton t:node.checkConnectivity(owner)){
                    curr.add(t);
                    t.setIsConnected(true);
                }
            }
            nodes.addAll(curr);
        }
    }


    /**
     * Eltavolit egy sporat a jelenlegi tektonrol es athelyezi a celtektonra
     *
     * @return
     */
    public boolean shootSpora(Tekton dst) {
        if (tekton == null || dst == null) {
            System.out.println("Invalid source or target Tekton.");
            return false;
        }

        if (tekton.getSporak().size() < SHOOTSPORA_COST) {
            System.out.println("Not enough spora to shoot! (Need at least 2 sporak)");
            return false;
        }

        if (level < 2) {

            if(!tekton.getNeighbours().contains(dst)) {
                System.out.println("Target Tekton is unreachable.");
                return false;
            }
        }else{
            List<Tekton> szomszedok = tekton.getNeighbours();
            List<Tekton> elerheto = new ArrayList<>();
            for (Tekton t : szomszedok) {
                List<Tekton> loc_szomszedok = t.getNeighbours();
                for (Tekton loc_t : loc_szomszedok) {
                    if (!elerheto.contains(loc_t)) {
                        elerheto.add(t);
                    }
                }
                
            }
            if(!elerheto.contains(dst)) {
                System.out.println("Target Tekton is unreachable.");
                return false;
            }
        }

        // Van elég spóra és elérhető a cél -> végrehajtás
        dst.addSpora(tekton.removeSpora(), owner);
        shotCounter--;
        tekton.removeSpora(); // A második spóra is eltávolítva
        System.out.println("Successfully shot spora to Tekton: " + dst.getID());
        return true;
    }

    @Override
    public void delete() {
        tekton.removeGombatest(this);
        ((Gombasz) owner).removeGombaTest(this);
    }

    @Override
    protected String getPrefix() {
        return "G";
    }

    @Override
    public void update() {
        if (shotCounter > 0) {
            for (int i = 0; i < 3; i++) {
                produceSpora();
                produceSpora();
            }
            if (level > 3){
                produceSpora();
            }
            
        }else{
            delete();
        }
    }

    @Override
    public String toString() {
        return "GombaTest{" +
                "ID='" + getID() + '\'' +
                ", Owner=" + (owner != null ? owner.getName() : "None") +
                ", CurrentTekton=" + (tekton != null ? tekton.getID() : "None") +
                ", Level=" + level +
                ", ShotCounter=" + shotCounter +
                '}';
    }

    /**
     * @return The number of Sporas available on the Tekton.
     */
    public int getAvailableSporaCount() {
        return tekton.getSporak().size();
    }

    public Tekton getTekton(){
        return tekton;
    }

    public void setTekton(Tekton t){
        tekton = t;
    }

    public int getLevel() {
        return this.level;
    }

    /**
     * Beallitja a gombatest szintjet
     */
    public void setLevel(int level) {
        this.level = level;
        notifyObservers();
    }

    public int getShotCounter() {
        return shotCounter;
    }

}
