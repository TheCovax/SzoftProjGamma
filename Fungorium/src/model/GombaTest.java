package Fungorium.src.model;

import Fungorium.src.model.spora.GyorsitoSpora;
import Fungorium.src.model.spora.LassitoSpora;
import Fungorium.src.model.spora.BenitoSpora;
import Fungorium.src.model.spora.Spora;
import Fungorium.src.utility.Logger;
import java.util.List;
import java.util.ArrayList;


import Fungorium.src.model.tekton.Tekton;

import java.util.ArrayList;
import java.util.List;

public class GombaTest {

    List<GombaFonal> fonalak;
    Tekton tekton;
    String owner;
    int level;


    public GombaTest(Tekton tekton, String owner) {
        fonalak = new ArrayList<>();
        this.tekton = tekton;
        this.owner = owner;
        level = 0;
    }

    public GombaTest() {

    }

    public void produceSpora() {
        Logger.methodCall("g.produceSpora()");

        // 1. Spóra típus véletlenszerű kiválasztása
        int r = (int) (Math.random() * 3); // 0, 1 vagy 2

        Spora spora;
        switch (r) {
            case 0:
                Logger.log("Gyorsító spóra létrehozása");
                spora = new GyorsitoSpora();
                break;
            case 1:
                Logger.log("Lassító spóra létrehozása");
                spora = new LassitoSpora();
                break;
            case 2:
                Logger.log("Bénító spóra létrehozása");
                spora = new BenitoSpora();
                break;
            default:
                Logger.log("Nem sikerült spóra típust választani");
                Logger.methodReturn("g.produceSpora()");
                return;
        }

        // 2. Spóra hozzáadása a megfelelő Tektonhoz
        if (tekton != null) {
            Logger.methodCall("tekton.addSpora(spora)");
            tekton.addSpora(spora);
            Logger.methodReturn("tekton.addSpora(spora)");
        } else {
            Logger.log("Nincs tekton hozzárendelve ehhez a gombatesthez.");
        }

        Logger.methodReturn("g.produceSpora()");
    }


    void clear() {
         //TODO
    }

    void upgradeTest() {
        if(getLevel() < 4) {
            setLevel(getLevel() + 1);
            tekton.removeSpora();
        }
        else {
            System.out.println("A gombatest elérte a maximális fejlettségi szintet, nem lehet tovább fejleszteni!");
            return;
        }
    }

    //beallitja az elerheto tektonok isConnected valtozojat igazra
    void setRecursiveConnectivity() {

        List<Tekton> nodes = tekton.checkConnectivity();
        while(nodes.size()>0){
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


    //Eltavolit egy sporat a jelenlegi tektonrol es athelyezi a celtektonra
    public void shootSpora(Tekton dst){
        dst.addSpora(tekton.removeSpora());
    }

    public void setTekton(Tekton t){
        tekton = t;
    }

    int getLevel() {
        return this.level;
    }

    void setLevel(int level) {
        this.level = level;
    }

    
}
