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
    private String name;

    @Column(nullable = false)
    private String token;

    // Should only be readable!
    @OneToOne
    private Player creator;

    @OneToMany
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

    //********* Getters and Setters


    public Long getId() {
        return id;
    }

    // This is a new pattern I got taught from a random indian youtuber
    public Game setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Game setName(String name) {
        this.name = name;
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
