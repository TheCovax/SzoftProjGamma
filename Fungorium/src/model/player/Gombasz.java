package Fungorium.src.model.player;

import Fungorium.src.model.GombaFonal;
import Fungorium.src.model.GombaTest;
import java.util.ArrayList;
import java.util.List;

/**
 * A Gombasz az egyik jatekos tipusa a jateknak.
 * A Player ososztalybol szarmazik.
 * A gombasznak vannak gombai (gombatest), melyeket iranyitani tud a jatek soran, s a szamuk nohet is, csokkenhet is.
 * A gombasz a gombatestek altal koronkent termelt sporakat ki tudja loni szomszedos tektonokra 
 * vagy szomszedos tektonok szomszedos tektonjaira a fejlettsegenek fuggvenyeben. Tovabba kepes gombafonalakat noveszteni
 * a tektonok kozott, majd tovabbi gombatestet noveszteni egy arra alkalmas tektonon az oda kilott sporak felhasznalasaval.
 * A legfobb celja, hogy minel tobb gombatestet novesszen, hiszen ebbol tud pontokat szerezni.
 */
public class Gombasz extends Player {

    /**
     * A gombasz gombainak listaja.
     * A gombasz fonalainak listaja.
     */
    List<GombaTest> gombak;
    List<GombaFonal> fonalak;
    
    /**
     * Gombasz beallito konstruktor.
     * @param id A gombasz Id-ja.
     */
    public Gombasz(String id) {
        super(id);
        gombak = new ArrayList<>();
        fonalak = new ArrayList<>();
    }

    /**
     * Hozzaad egy adott gombatestet a gombak listahoz.
     * @param gombatest Az a gombatest, amit hozzaad a gombak listahoz.
     */
    public void addGombaTest(GombaTest gombatest) {
        gombak.add(gombatest);
    }

    /**
     * Eltavolit egy adott gombatestet a gombak listabol.
     * @param gombatest Az a gombatest, amit eltavolit a gombak listabol.
     */
    public void removeGombaTest(GombaTest gombatest) {
        gombak.remove(gombatest);
    }

    /**
     * Visszaadja a gombak listat.
     */
    public List<GombaTest> getGombak() {
        return gombak;
    }

    /**
     * Visszaadja a fonalak listat.
     */
    public List<GombaFonal> getFonalak() {
        return fonalak;
    }

    /**
     * Beallitja a fonalak listat egy megadott fonalakbol allo listaval.
     * @param fonalak Az a lista, amelynek elemeivel beallitja a fonalak listat.
     */
    public void setFonalak(List<GombaFonal> fonalak) {
        this.fonalak = fonalak;
    }

    /**
     * Hozzaad egy adott fonalat a fonalak listahoz.
     * @param fonal Az a fonal, amit hozzaad a fonalak listahoz.
     */
    public void addFonal(GombaFonal fonal){
        fonalak.add(fonal);
    }

    /**
     * Eltavolit egy adott fonalat a fonalak listabol.
     * @param fonal Az a fonal, amit eltavolit a fonalak listabol.
     */
    public void removeFonal(GombaFonal fonal){
        fonalak.remove(fonal);
    }

}
