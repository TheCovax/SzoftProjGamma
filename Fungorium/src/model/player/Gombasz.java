package Fungorium.src.model.player;

import Fungorium.src.model.GombaTest;
import java.util.ArrayList;
import java.util.List;

public class Gombasz extends Player {

    List<GombaTest> gombak;
    
    public Gombasz() {
        gombak = new ArrayList<>();
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

}
