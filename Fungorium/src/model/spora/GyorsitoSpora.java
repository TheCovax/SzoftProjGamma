package model.spora;

import model.Rovar;
import utility.Logger;

public class GyorsitoSpora extends Spora {
    public static final int DEFAULT_SPEED_UP_VALUE = 3;

    @Override
    public  void applyEffect(Rovar rovar){
        Logger.methodCall("GyorsitoSpora.applyEffect()");

        rovar.setSpeed(DEFAULT_SPEED_UP_VALUE);
        rovar.setDuration(DEFAULT_EFFECT_DURATION);

        Logger.methodReturn("Gyorsito.applyEffect()");
    };
}
