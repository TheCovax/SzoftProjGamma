package Fungorium.src.model.player;


/**
 * A Player a Gombasz es a Rovarasz osztalyok ose.
 * A player osztaly fogja ossze a jatek jatekosait 
 */
public class Player {
    private final String id;
    int score;
    String name;
    int team;

    /**
     * Player beallito konstruktor.
     * @param id A player Id-ja.
     */
    public Player(String id) {
        this.id = id;
        score = 0;
    }

    /**
     * Noveli a player pontszamat.
     * @param points Az a pontszam, amivel noveljuk az eddigi pontokat.
     */
    void increaseScore(int points) {
        this.score += points;
    }

    /**
     * Visszaadja a player nevet.
     */
    public String getName(){
        return this.name;
    }
    
    /**
     * Beallitja a player nevet.
     * @param name Az a nev, aminek elnevezzuk a playert.
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Visszaadja a player pontszamat.
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Beallitja a player pontszamat.
     * /**
     * Visszaadja a player pontszamat.
     * @param score Az a pontszam, amire beallitjuk a player pontszamat.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Visszaadja a player csapatat.
     */
    public int getTeam() {
        return team;
    }

    /**
     * Beallitja a player csapatat.
     * @param team az a csapat int erteke, amihez tartozik a player
     */
    public void setTeam(int team) {
        this.team = team;
    }

    /**
     * Visszaadja a player Id-jat.
     */
    public String getID(){
        return id;
    }

    
}
