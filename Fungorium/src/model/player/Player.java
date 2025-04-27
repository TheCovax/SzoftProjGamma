package Fungorium.src.model.player;

public class Player {

    int score;
    String name;

    public Player() {
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




}
