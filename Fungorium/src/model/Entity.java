package Fungorium.src.model;

import Fungorium.src.model.player.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Entity {
    protected String ID;
    protected Player owner;
    private static final HashMap<Class<?>, AtomicInteger> idCounters = new HashMap<>();
    private static final Set<String> usedIds = new HashSet<>();

    public Entity(String id, Player owner){
        this.ID = id;
        this.owner = owner;
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

    private String generateAutoId() {
        Class<?> clazz = this.getClass();
        AtomicInteger counter = idCounters.computeIfAbsent(clazz, k -> new AtomicInteger(0));

        String newId;
        do {
            newId = getPrefix() + counter.incrementAndGet();
        } while (!usedIds.add(newId));

        return newId;
    }

    private static void registerId(String id) {
        if (!usedIds.add(id)) {
            throw new IllegalArgumentException("ID already exists: " + id);
        }
    }

    protected abstract String getPrefix();

    /**
     * Called every round to update the entity.
     */
    public abstract void update();
}
