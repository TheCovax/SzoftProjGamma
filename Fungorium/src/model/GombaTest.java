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
import java.util.concurrent.atomic.AtomicInteger;

public class GombaTest extends Entity{

    private static final AtomicInteger generatedCounter = new AtomicInteger(0);
    //List<GombaFonal> fonalak;
    Tekton tekton;
    int level;
    int shotCounter;

    int SHOOTSPORA_COST = 2;
    int GROWFONAL_COST = 1;
    int UPGRADE_COST = 4;


    public GombaTest(Tekton tekton, Player owner) {
        super(owner);
        this.tekton = tekton;
        level = 0;
    }

    public GombaTest(String id, Tekton tekton, Player owner) {
        super(id, owner);
        this.tekton = tekton;
        level = 0;
    }

    public int getAvailableSporaCount() {
        return tekton.getSporak().size();
    }

    public Tekton getTekton(){
        return tekton;
    }

    public void produceSpora() {

        // 1. Spóra típus véletlenszerű kiválasztása
        int r = (int) (Math.random() * 4);

        Spora spora;
        switch (r) {
            case 0 -> {
                spora = new GyorsitoSpora();
            }
            case 1 -> {
                spora = new LassitoSpora();
            }
            case 2 -> {
                spora = new BenitoSpora();
            }
            case 3 -> {
                spora = new OsztoSpora();
            }
            default -> {
                System.out.println("error");
                return;
            }
        }

        // 2. Spóra hozzáadása a megfelelő Tektonhoz
        if (tekton != null) {
            tekton.addSpora(spora, owner);
        } 
    }

    @Override
    public void delete() {
        tekton.removeGombatest(this);
        ((Gombasz) owner).removeGombaTest(this);
    }

    /**
     * Egy szintel fejleszti a gombatestet
     */
    public void upgradeTest() {
        if (tekton == null) {
            System.out.println("No Tekton assigned!");
            return;
        }

        if (tekton.getSporak().size() < 4) {
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
     */
    public void shootSpora(Tekton dst) {
        if (tekton == null || dst == null) {
            System.out.println("Invalid source or target Tekton.");
            return;
        }

        if (tekton.getSporak().size() < 2) {
            System.out.println("Not enough spora to shoot! (Need at least 2 sporak)");
            return;
        }

        if (!tekton.findReachableTektonWithinDistance(1).contains(dst)) {
            System.out.println("Target Tekton is unreachable.");
            return;
        }

        // Van elég spóra és elérhető a cél -> végrehajtás
        dst.addSpora(tekton.removeSpora(), owner);
        shotCounter--;
        tekton.removeSpora(); // A második spóra is eltávolítva
        System.out.println("Successfully shot spora to Tekton: " + dst.getID());
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
    }




    public int getShotCounter() {
        return shotCounter;
    }

    @Override
    protected String getPrefix() {
        return "G";
    }

    @Override
    public void update() {
        if (shotCounter > 0) {
            produceSpora();
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

}
