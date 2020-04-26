package ch.uzh.ifi.seal.soprafs20.entity;


//TODO: statistics updates during game or after game
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Table(name = "STATISTICS")
public class Statistics implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @ElementCollection(targetClass=Integer.class)
    private List<Integer> encounteredPokemon;

    @Column(nullable = false)
    private int gamesWon;

    @Column(nullable = false)
    private int gamesPlayed;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false)
    private int storyProgress;

    public Statistics(){
        this.encounteredPokemon=new ArrayList<>();
        this.gamesWon=0;
        this.gamesPlayed=0;
        this.rating=0;
        this.storyProgress=0;
    }

    public List<Integer> getEncounteredPokemon() {
        return encounteredPokemon;
    }
    public void setEncounteredPokemon(List<Integer> encounteredPokemon) {
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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
