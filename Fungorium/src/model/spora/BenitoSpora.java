package Fungorium.src.model.spora;

import Fungorium.src.model.Rovar;

public class BenitoSpora extends Spora{
    @Override
    public  void applyEffect(Rovar r){
        //Logger.methodCall("BenitoSpora.applyEffect(r)");

        //Logger.methodCall("r.setParalyzed(true)");
        r.setParalyzed(true);
        //Logger.methodReturn("r.setParalyzed(true)");

        r.setDuration(DEFAULT_EFFECT_DURATION);
        r.setSpeed(Rovar.DEFAULT_SPEED);

        //Logger.methodReturn("BenitoSpora.applyEffect(r)");
    };
}