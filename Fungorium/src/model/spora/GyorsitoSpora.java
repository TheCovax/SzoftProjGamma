package Fungorium.src.model.spora;

import Fungorium.src.model.Rovar;
import Fungorium.src.utility.Logger;

public class GyorsitoSpora extends Spora {
    public static final int DEFAULT_SPEED_UP_VALUE = 3;

    @Override
    public  void applyEffect(Rovar r){
        Logger.methodCall("GyorsitoSpora.applyEffect()");

        r.setSpeed(DEFAULT_SPEED_UP_VALUE);
        r.setDuration(DEFAULT_EFFECT_DURATION);

        Logger.methodReturn("Gyorsito.applyEffect()");
    };
}