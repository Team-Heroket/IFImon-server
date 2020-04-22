package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "GAME")
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String gameName;

    @Column(nullable = false)
    private String creationTime;

    @Column
    private String startTime;

    @Column(nullable = false)
    private String token;

    // Should only be readable!
    @OneToOne(cascade = CascadeType.ALL)
    private Player creator;

    @OneToOne(cascade = CascadeType.ALL)
    private Player turnPlayer;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Player> players;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Player> winners;

    @Enumerated(EnumType.STRING)
    @Column(length = 13)
    private Mode mode;

    @Enumerated(EnumType.STRING)
    @Column(length = 14)
    private Category category;

    // Cool, gets converted to string for database and back to enum for java
    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private GameStateEnum state;

    // TODO: chat

    public Game() {
        this.players = new ArrayList<>();
    }

    public Game(Player creator) {
        this.creator = creator;

        // The creator of the game is automatically also a player of that game.
        this.players = new ArrayList<>();
        this.players.add(creator);
    }

    //********* Getters and Setters


    public Long getId() {
        return id;
    }

    // This is a new pattern I got taught from a random indian youtuber
    public Game setId(Long id) {
        this.id = id;
        return this;
    }

    public String getGameName() {
        return gameName;
    }

    public Game setGameName(String gameName) {
        this.gameName = gameName;
        return this;
    }

    public String getToken() {
        return token;
    }

    public Game setToken(String token) {
        this.token = token;
        return this;
    }

    public void setCreator(Player creator) {
        this.creator = creator;
    }

    public Player getCreator() {
        return creator;
    }

    public List<Player> getPlayers() {
        return players;
    }



    public void addPlayer(Player player) {
        if (this.players.isEmpty()) {
            this.players = new ArrayList<>();
        }
        this.players.add(player);
    }

    public GameStateEnum getState() {
        return state;
    }

    public Game setState(GameStateEnum state) {
        this.state = state;
        return this;
    }

    public Mode getMode() {
        return mode;
    }

    public Game setMode(Mode mode) {
        this.mode = mode;
        return this;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public Player getTurnPlayer() {
        return turnPlayer;
    }

    public void setTurnPlayer(Player turnPlayer) {
        this.turnPlayer = turnPlayer;
    }

    public void resetPlayers(){
        this.players=null;
    }

    public void resetCreator(){
        this.creator=null;
    }

    public void resetCategory(){ this.category=null; }

    public void resetTurnPlayer(){ this.turnPlayer=null; }

    public void resetWinners(){ this.winners=null; }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Player> getWinners() {
        return winners;
    }

    public void setWinners(List<Player> winner) {
        this.winners = winner;
    }
}
