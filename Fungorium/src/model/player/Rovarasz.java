package Fungorium.src.model.player;

import Fungorium.src.model.spora;
import Fungorium.src.model.player;
import Fungorium.src.model.tekton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Rovarasz extends Player {

    List<Rovar> rovarok;
    
    public Rovarasz() {
        rovarok = new ArrayList();
    }


    void addRovar(Rovar rovar) {
        rovarok.add(rovar);
    }

    
    void removeRovar(Rovar rovar) {
        rovarok.remove(rovar);
    }

    List<Rovar> getRovarok() {
        return rovarok;
    }

    //.............................
    @Override
    void takeAction(Action action) {
        //  TO  DO
    }
}