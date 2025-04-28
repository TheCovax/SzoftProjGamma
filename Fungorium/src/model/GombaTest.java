package Fungorium.src.model;

import Fungorium.src.model.player.Player;
import Fungorium.src.model.spora.BenitoSpora;
import Fungorium.src.model.spora.GyorsitoSpora;
import Fungorium.src.model.spora.LassitoSpora;
import Fungorium.src.model.spora.OsztoSpora;
import Fungorium.src.model.spora.Spora;
import Fungorium.src.model.tekton.Tekton;
import Fungorium.src.utility.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GombaTest extends Entity{

    private static final AtomicInteger generatedCounter = new AtomicInteger(0);
    List<GombaFonal> fonalak;
    Tekton tekton;
    int level;
    int shotCounter;



    public GombaTest(Tekton tekton, Player owner) {
        super(owner);
        fonalak = new ArrayList<>();
        this.tekton = tekton;
        level = 0;
    }

    public GombaTest(String id, Tekton tekton, Player owner) {
        super(id, owner);
        fonalak = new ArrayList<>();
        this.tekton = tekton;
        level = 0;
    }

    public void produceSpora() {
        Logger.methodCall("g.produceSpora()");

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
            //Logger.methodCall("tekton.addSpora(spora)");
            tekton.addSpora(spora, owner);
            //Logger.methodReturn("tekton.addSpora(spora)");
        } else {
            //Logger.log("Nincs tekton hozzárendelve ehhez a gombatesthez.");
        }
    }


    
    void clear() {
         //TODO
    }

    /**
     * Egy szintel fejleszti a gombatestet
     */
    public void upgradeTest() {
        //TODO: azert ezt majd finomitsuk xd
        if(getLevel() < 4) {
            setLevel(getLevel() + 1);
            tekton.removeSpora();
        }
        else {
            System.out.println("Maximum level reached!");
        }
    }

    //beallitja az elerheto tektonok isConnected valtozojat igazra
    void setRecursiveConnectivity() {

        List<Tekton> nodes = tekton.checkConnectivity();
        while(!nodes.isEmpty()){
            List<Tekton> curr = new ArrayList<>();
            for(Tekton node : nodes){
                for(Tekton t:node.checkConnectivity()){
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
    public void shootSpora(Tekton dst){
        dst.addSpora(tekton.removeSpora(), owner);
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

    public List<GombaFonal> getFonalak() {
        return fonalak;
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
        // TODO: produce spora
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
