package Fungorium.src.model;

import Fungorium.src.model.tekton.Tekton;

import java.util.ArrayList;
import java.util.List;

public class GombaTest {

    List<GombaFonal> fonalak;
    Tekton tekton;
    String owner;
    int level;


    public GombaTest(Tekton tekton, String owner) {
        fonalak = new ArrayList<>();
        this.tekton = tekton;
        this.owner = owner;
        level = 0;
    }

    public GombaTest() {

    }

    void produceSpora() {
        //TODO
    }

    void clear() {
         //TODO
    }

    void upgradeTest() {
        if(getLevel() < 4) {
            setLevel(getLevel() + 1);
            tekton.removeSpora();
        }
        else {
            System.out.println("A gombatest elérte a maximális fejlettségi szintet, nem lehet tovább fejleszteni!");
            return;
        }
    }

    //beallitja az elerheto tektonok isConnected valtozojat igazra
    void setRecursiveConnectivity() {

        List<Tekton> nodes = tekton.checkConnectivity();
        while(nodes.size()>0){
            List<Tekton> curr = new ArrayList<>();
            for(Tekton node : nodes){
                for(Tekton t:node.checkConnectivity()){
                    curr.add(t);
                    t.setIsConnected(true);
                }
            }
            nodes.addAll(curr);
        }

    }


    //Eltavolit egy sporat a jelenlegi tektonrol es athelyezi a celtektonra
    public void shootSpora(Tekton dst){
        dst.addSpora(tekton.removeSpora());
    }

    public void setTekton(Tekton t){
        tekton = t;
    }

    int getLevel() {
        return this.level;
    }

    void setLevel(int level) {
        this.level = level;
    }

    
}
