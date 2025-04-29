package Fungorium.src.model.spora;

import Fungorium.src.model.Rovar;

public class OsztoSpora extends Spora {


    //Letrehoz meg egy rovart
    @Override
    public void applyEffect(Rovar r){
        //Logger.methodCall("GyorsitoSpora.applyEffect()");

        r.split();

        //Logger.methodReturn("Gyorsito.applyEffect()");
    };
}