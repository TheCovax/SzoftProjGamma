package Fungorium.src.model;

import Fungorium.src.model.observer.Observable;
import Fungorium.src.model.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Abstract base class for all game entities (e.g., Rovar, GombaTest, GombaFonal).
 *
 * Each entity has a unique ID, an owner (Player)
 * including automatic ID generation if none is provided.
 */
public abstract class Entity implements Observable {
    protected String ID;
    protected Player owner;

    /** Global counters for automatically generating unique IDs by class type. */
    private static final HashMap<Class<?>, AtomicInteger> idCounters = new HashMap<>();

    /** Global set of all used IDs to ensure uniqueness across all entities. */
    private static final Set<String> usedIds = new HashSet<>();


    protected List<Observer> observers = new ArrayList();

    @Override
    public void attach(Observer o) {
        observers.add(o);
    }

    @Override
    public void detach(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(){
        for (Observer o : observers) {
            //o.update();
        }
    }

    public Entity(String id, Player owner){
        this.ID = id;
        this.owner = owner;
        registerId(id);
    }

    public Entity(Player owner){
        this.ID = generateAutoId();
        this.owner = owner;
    }

    public String getID(){
        return ID;
    }

    public Player getOwner(){
        return owner;
    }

    /**
     * Generates a new unique ID automatically for this entity class.
     *
     * The generated ID consists of a class-specific prefix and a running number.
     *
     * @return The newly generated ID.
     */
    private String generateAutoId() {
        Class<?> clazz = this.getClass();
        AtomicInteger counter = idCounters.computeIfAbsent(clazz, k -> new AtomicInteger(0));

        String newId;
        do {
            newId = getPrefix() + counter.incrementAndGet();
        } while (!usedIds.add(newId));

        return newId;
    }

    /**
     * Registers a manually assigned ID and ensures it is globally unique.
     *
     * @param id The ID to register.
     * @throws IllegalArgumentException if the ID already exists.
     */
    private static void registerId(String id) {
        if (!usedIds.add(id)) {
            throw new IllegalArgumentException("ID already exists: " + id);
        }
    }

    /**
     * Returns the class-specific prefix used for generating automatic IDs.
     *
     * @return The prefix string (e.g., "R" for Rovar, "G" for GombaTest).
     */
    protected abstract String getPrefix();

    /**
     * Called every game round to update the internal state of the entity.
     *
     * Implemented by subclasses to define specific behaviors like growing, moving, etc.
     */
    public abstract void update();

    /**
     * Called to remove the entity from the game completely.
     *
     * This includes detaching from owner, Tekton, or other structures.
     */
    public abstract void delete();
}
