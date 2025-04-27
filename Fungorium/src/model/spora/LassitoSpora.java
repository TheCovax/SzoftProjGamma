package Fungorium.src.model.spora;

import Fungorium.src.model.Rovar;
import Fungorium.src.utility.Logger;

public class LassitoSpora extends Spora {
    public static final int DEFAULT_SLOW_DOWN_VALUE = 1;

    int slowDownValue;

    public LassitoSpora(){
        slowDownValue = DEFAULT_SLOW_DOWN_VALUE;
    }


    @Override
    public  void applyEffect(Rovar r){
        Logger.methodCall("LassitoSpora.applyEffect(r)");

        r.setSpeed(DEFAULT_SLOW_DOWN_VALUE);
        r.setDuration(DEFAULT_EFFECT_DURATION);

        Logger.methodReturn("LassitoSpora.applyEffect(r)");
    };

    public int getSlowDownValue() {
        return slowDownValue;
    }
}