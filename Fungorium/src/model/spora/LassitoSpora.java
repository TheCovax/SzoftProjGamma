package model.spora;

import model.Rovar;
import utility.Logger;

public class LassitoSpora extends Spora {
    public static final int DEFAULT_SLOW_DOWN_VALUE = 1;

    @Override
    public  void applyEffect(Rovar rovar){
        Logger.methodCall("LassitoSpora.applyEffect()");

        rovar.setSpeed(DEFAULT_SLOW_DOWN_VALUE);
        rovar.setDuration(DEFAULT_EFFECT_DURATION);

        Logger.methodReturn("LassitoSpora.applyEffect()");
    };
}
