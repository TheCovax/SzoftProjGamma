package Fungorium.src.model.player;

import Fungorium.src.model.Rovar;
import java.util.ArrayList;
import java.util.List;

public class Rovarasz extends Player {

    List<Rovar> rovarok;
    
    public Rovarasz() {
        rovarok = new ArrayList<>();
    }

    public void addRovar(Rovar rovar) {
        rovarok.add(rovar);
    }

    public void removeRovar(Rovar rovar) {
        rovarok.remove(rovar);
    }

    public List<Rovar> getRovarok() {
        return rovarok;
    }

}