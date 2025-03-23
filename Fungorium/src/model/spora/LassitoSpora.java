package model.spora;

import model.Rovar;
import utility.Logger;

public class LassitoSpora extends Spora {
    @Override
    public  void applyEffect(Rovar rovar){
        Logger.methodCall("LassitoSpora.applyEffect()");

        rovar.setSpeed(10);
        rovar.setDuration(10);

        Logger.methodReturn("LassitoSpora.applyEffect()");
    };
}
