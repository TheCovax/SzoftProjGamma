package Fungorium.src.model.player;

import Fungorium.src.model.spora;
import Fungorium.src.model.player;
import Fungorium.src.model.tekton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Player {

    int score;
    
    public Player() {
        score = 0;
    }

    void increaseScore(int points) {
        this.score += points;
    }

    
    // ...................
    void takeAction() {
        //  TO  DO
    }




    int getScore() {
        return this.score;
    }

    void setScore(int score) {
        this.score = score;
    }




}
