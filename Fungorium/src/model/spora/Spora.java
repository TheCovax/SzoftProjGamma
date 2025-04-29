package Fungorium.src.model.spora;

import Fungorium.src.model.Rovar;

/**
 * A gombatestek által kilőtt egység.
 * Ezzel fejlődhetnek a gombatestek újabb szintekre és elengedhetetlen a gombatest kifejlődéséhez.
 * A gombatestek körönként termelnek új spórákat.
 * A rovarok tápanyagként fogyaszthatják, de az elfogyasztott spóra különböző hatásokat válthat ki rájuk.
 */

public abstract class Spora {
    /**
     * Alapértelmezett hatásidőtartam (körökben mérve).
     */
    public static final int DEFAULT_EFFECT_DURATION = 2;

    /**
     * A spora alapertelmezett tapanyag tartama.
     */
    public static final int DEFAULT_NUTRITION = 1;

    private int effectDuration;
    private int nutrition;

    /**
     * Spora beallito konstruktor.
     */
    public Spora(){
        this.effectDuration = DEFAULT_EFFECT_DURATION;
        this.nutrition = DEFAULT_NUTRITION;
    }

    /**
     * Alkalmazza a spora effektet egy adott rovarra.
     * @param r Az a rovar, amelyre az effektet alkalmazzuk.
     */
    public abstract void applyEffect(Rovar r);

    /**
     * Visszaadja, hogy hany pontot er a spora.
     */
    public int getNutrition() {
        return nutrition;
    }

    /**
     * Beallitja, hogy hany pontot er a spora.
     * @param nutrition a spora pontszama.
     */
    public void setNutrition(int nutrition) {
        this.nutrition = nutrition;
    }

    /**
     * Visszaadja a spora hatasanak idejet.
     */
    public int getEffectDuration() {
        return effectDuration;
    }
}
