package Fungorium.src.model.player;

import Fungorium.src.model.GombaFonal;
import Fungorium.src.model.GombaTest;
import java.util.ArrayList;
import java.util.List;

public class Gombasz extends Player {

    List<GombaTest> gombak;
    List<GombaFonal> fonalak;
    
    public Gombasz() {
        gombak = new ArrayList<>();
        fonalak = new ArrayList<>();
    }

    public void addGombaTest(GombaTest gombatest) {
        gombak.add(gombatest);
    }

    public void removeGombaTest(GombaTest gombatest) {
        gombak.remove(gombatest);
    }

    public List<GombaTest> getGombak() {
        return gombak;
    }

    public List<GombaFonal> getFonalak() {
        return fonalak;
    }

    public void setFonalak(List<GombaFonal> fonalak) {
        this.fonalak = fonalak;
    }

    public void addFonal(GombaFonal fonal){
        fonalak.add(fonal);
    }

    public void removeFonal(GombaFonal fonal){
        fonalak.remove(fonal);
    }

}
