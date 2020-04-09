package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.GameStateEnum;
import ch.uzh.ifi.seal.soprafs20.constant.Mode;

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
    private String token;

    // Should only be readable!
    @OneToOne(cascade = CascadeType.ALL)
    private Player creator;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Player> players;

    @Enumerated(EnumType.STRING)
    @Column(length = 6)
    private Mode mode;

    // Cool, gets converted to string for database and back to enum for java
    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private GameStateEnum state;

    // TODO: table

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

    public Player getCreator() {
        return creator;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        if (this.players.isEmpty()) {
            // TODO: Determine if ArrayList is the waye
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
}
