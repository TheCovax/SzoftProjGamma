package Fungorium.src.model.player;

public class Player {
    private final String id;
    int score;
    String name;
    int team;

    public Player(String id) {
        this.id = id;
        score = 0;
    }

    void increaseScore(int points) {
        this.score += points;
    }

    public String getName(){
        return this.name;
    }
    
    public void setName(String name){
        this.name = name;
    }

    public int getScore() {
        return this.score;
    }

    void setScore(int score) {
        this.score = score;
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public String getID(){
        return id;
    }



}
