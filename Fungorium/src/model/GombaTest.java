package Fungorium.src.model;

import Fungorium.src.model.tekton.Tekton;

public class GombaTest {

    Tekton tekton;

    //Eltavolit egy sporat a jelenlegi tektonrol es athelyezi a celtektonra
    public void shootSpora(Tekton dst){
        dst.addSpora(tekton.removeSpora());
    }

    public void setTekton(Tekton t){
        tekton = t;
    }
}
