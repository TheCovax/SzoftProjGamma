package Fungorium.src.model;

import Fungorium.src.model.tekton.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Represents the map of the game, holding all Tekton instances and their neighbor relationships.
 * Tektons are loaded from a text file following a predefined format.
 */
public class Map {
    private ArrayList<Tekton> tektonok;

    public Map(){
        tektonok = new ArrayList<>();
    }

    /**
     * Loads the map's tektons and their neighbour relationships from a txt file.
     * the Map format is available in the documentation.
     * @param filename the path to the txt file
     * @throws IOException if the file cannot be read
     */
    public void loadMap(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        String mode = "";

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            // Identify mode based on section header
            if (line.startsWith("#")){
                if (line.toLowerCase().contains("tektons")) {
                    mode = "tekton";
                } else if (line.toLowerCase().contains("neighbours")) {
                    mode = "neighbour";
                } else {
                    mode = ""; // other sections we ignore for now
                }
            }
            // Load Tektons
            else if (mode.equals("tekton")){
                String[] parts = line.split("\\s+");
                if (parts.length >= 2){
                    String id = parts[0];
                    String type = parts[1];
                    // TODO: create tekton by type
                    Tekton tekton = createTektonByType(id, type);
                    addTekton(tekton);
                }
            }
            // Load Neighbours
            else if (mode.equals("neighbour")){
                String[] parts = line. split("\\s+");
                if (parts.length >= 2) {
                    // TODO: Get Tekton by ID and add neighbours
                    Tekton mainTekton = getTektonById(parts[0]);
                    for (int i = 1; i < parts.length; i++) {
                        Tekton neighbour = getTektonById(parts[i]);
                        if (mainTekton != null && neighbour != null) {
                            mainTekton.addNeighbour(neighbour);
                        }
                    }
                }
            }
        }

        reader.close();
    }


    /**
     * Creates a Tekton instance based on the specified type and ID.
     *
     * @param id   The ID of the Tekton.
     * @param type The type of Tekton (Stabil, Kopar, etc.).
     * @return A Tekton subclass matching the type.
     * @throws IllegalArgumentException If the type is unknown.
     */
    private Tekton createTektonByType(String id, String type){
        switch (type.toLowerCase()){
            case "elszigetelt":
                return new ElszigeteltTekton(id);
            case "hosztilis":
                return new HosztilisTekton(id);
            case "kopar":
                return new KoparTekton(id);
            case "stabil":
                return new StabilTekton(id);
            case "termekeny":
                return new TermekenyTekton(id);
            default:
                throw new IllegalArgumentException("Unknown Tekton type" + type);
        }
    }

    /**
     * Finds and returns the Tekton with the given ID.
     * @param id The ID of the Tekton to find.
     * @return The Tekton object with the matching ID, or null if not found.
     */
    public Tekton getTektonById(String id) {
        for (Tekton t : tektonok) {
            if (t.getID().equals(id)) {
                return t;
            }
        }

        throw new NoSuchElementException("No Tekton found with ID: " + id);
    }

    public ArrayList<Tekton> getTektonok(){
        return tektonok;
    }

    public void addTekton(Tekton t){
        tektonok.add(t);
    }
}