package Fungorium.src.model.player;

import Fungorium.src.model.Rovar;
import java.util.ArrayList;
import java.util.List;


/**
 * A Rovarasz az egyik jatekos tipusa a jateknak.
 * A Player ososztalybol szarmazik.
 * A rovarasznak vannak rovarjai, melyeket iranyitani tud a jatek soran, s a szamuk nohet is, csokkenhet is.
 * A rovarasz a rovarokkal tud a tektonok kozott lepkedni, gombafonalakat elvagni, sporakat megenni.
 * A legfobb celja, hogy a rovarokkal minel tobb sporat elfogyasszon, hiszen ebbol tud pontokat szerezni.
 */
public class Rovarasz extends Player {

    /**
     * A rovarasz rovarainak listaja.
     */
    List<Rovar> rovarok;
    

    /**
     * Rovarasz beallito konstruktor.
     * @param id A rovarasz Id-ja.
     */
    public Rovarasz(String id)
    {
        super(id);
        rovarok = new ArrayList<>();
    }

    /**
     * Hozzaad egy adott rovart a rovarok listahoz.
     * @param rovar Az a rovar, amit hozzaad a rovarok listahoz.
     */
    public void addRovar(Rovar rovar) {
        rovarok.add(rovar);
    }

    /**
     * Eltavolit egy adott rovart a rovarok listabol.
     * @param rovar Az a rovar, amit eltavolit a rovarok listabol.
     */
    public void removeRovar(Rovar rovar) {
        rovarok.remove(rovar);
    }

    /**
     * Visszaadja a rovarok listat.
     */
    public List<Rovar> getRovarok() {
        return rovarok;
    }

}