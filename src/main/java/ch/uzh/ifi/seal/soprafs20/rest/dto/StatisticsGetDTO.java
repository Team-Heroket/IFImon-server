package ch.uzh.ifi.seal.soprafs20.rest.dto;

import java.util.*;

public class StatisticsGetDTO {

    private Set<Integer> encounteredPokemon;

    private int gamesWon;

    private int gamesPlayed;

    private int rating;

    private int storyProgress;

    public List<Integer> getEncounteredPokemon() {
        List<Integer> asArray = new ArrayList<>(encounteredPokemon);
        Collections.sort(asArray);
        return asArray;
    }

    public void setEncounteredPokemon(Set<Integer> encounteredPokemon) {
        this.encounteredPokemon = encounteredPokemon;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getStoryProgress() {
        return storyProgress;
    }

    public void setStoryProgress(int storyProgress) {
        this.storyProgress = storyProgress;
    }
}
