package model.spora;

import model.Rovar;
import utility.Logger;

public class BenitoSpora extends Spora{
    @Override
    public  void applyEffect(Rovar rovar){
        Logger.methodCall("BenitoSpora.applyEffect()");

        rovar.setParalyzed(true);
        rovar.setDuration(DEFAULT_EFFECT_DURATION);
        rovar.setSpeed(Rovar.DEFAULT_SPEED);

        Logger.methodReturn("BenitoSpora.applyEffect()");
    };
}
