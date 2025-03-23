package model.spora;

import model.Rovar;
import utility.Logger;

/**
 * A gombatestek által kilőtt egység.
 * Ezzel fejlődhetnek a gombatestek újabb szintekre és elengedhetetlen a gombatest kifejlődéséhez.
 * A gombatestek körönként termelnek új spórákat.
 * A rovarok tápanyagként fogyaszthatják, de az elfogyasztott spóra különböző hatásokat válthat ki rájuk.
 */

public class Spora {
    /**
     * Alapértelmezett hatásidőtartam (körökben mérve).
     */
    public static final int DEFAULT_EFFECT_DURATION = 2;

    /**
     * A spóra alapértelmezett tápanyag tartama.
     */
    public static final int DEFAULT_NUTRITION = 1;

    private int effectDuration;
    private int nutrition;

    public Spora(){
        this.effectDuration = DEFAULT_EFFECT_DURATION;
        this.nutrition = DEFAULT_NUTRITION;
    }

    /**
     * Alkalmazza a spóra effektet egy adott rovarra.
     * @param rovar Az a rovar, amelyre a effeltet alkalmazzuk.
     */
    public void applyEffect(Rovar rovar){
        Logger.methodCall("Spora.applyEffect()");
        Logger.methodReturn("Spora.applyEffect()");
    };

    public int getNutrition() {
        return nutrition;
    }

    public void setNutrition(int nutrition) {
        this.nutrition = nutrition;
    }

    public int getEffectDuration() {
        return effectDuration;
    }
}
