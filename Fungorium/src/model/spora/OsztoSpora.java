package Fungorium.src.model.spora;

import Fungorium.src.model.Rovar;
import Fungorium.src.utility.Logger;

public class OsztoSpora extends Spora {


    //Letrehoz meg egy rovart
    @Override
    public void applyEffect(Rovar r){
        Logger.methodCall("GyorsitoSpora.applyEffect()");

        r.split();

        Logger.methodReturn("Gyorsito.applyEffect()");
    };
}