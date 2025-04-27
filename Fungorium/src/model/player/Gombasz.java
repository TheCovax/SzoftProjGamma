package Fungorium.src.model.player;

import Fungorium.src.model.spora;
import Fungorium.src.model.player;
import Fungorium.src.model.tekton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Gombasz extends Player {

    List<GombaTest> gombak;
    
    public Gombasz() {
        gombak = new ArrayList();
    }



    void addGombaTest(GombaTest gombatest) {
        gombak.add(gombatest);
    }

    void removeGombaTest(GombaTest gombatest) {
        gombak.remove(gombatest);
    }

    List<GombaTest> getGombak() {
        return gombak;
    }

    //....................................
    @Override
    void takeAction() {
        //  TO  DO
    }

}
