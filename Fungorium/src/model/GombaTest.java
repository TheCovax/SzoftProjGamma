package Fungorium.src.model;

import Fungorium.src.model.tekton.Tekton;

public class GombaTest {

    List<GombaFonal> fonalak;
    Tekton tekton;
    String owner;
    int level;


    GombaTest(Tekton tekton, String owner) {
        fonalak = new ArrayList<>();
        this.tekton = tekton;
        this.owner = owner;
        level = 0;
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

    void setRecursiveConnectivity() {
        //TODO
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
