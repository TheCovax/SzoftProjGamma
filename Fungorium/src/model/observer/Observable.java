package Fungorium.src.model.observer;

import java.util.Observer;

public interface Observable {
    void attach(Observer o);
    void detach(Observer o);
    void notifyObservers();
}
