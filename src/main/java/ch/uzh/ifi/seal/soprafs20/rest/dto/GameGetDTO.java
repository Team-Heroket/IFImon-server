package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.constant.*;
import ch.uzh.ifi.seal.soprafs20.entity.Deck;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.entity.Statistics;

import javax.persistence.*;
import java.util.List;

public class GameGetDTO {


    private Long id;

    private String gameName;

    private String token;

    private PlayerDTO creator;

    private List<PlayerDTO> players;

    private List<PlayerDTO> winners;

    private Mode mode;

    private Category category;

    private GameStateEnum state;

    private String creationTime;

    private String startTime;

    private PlayerDTO turnPlayer;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getGameName() { return gameName; }
    public void setGameName(String gameName) { this.gameName = gameName; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public PlayerDTO getCreator() { return creator; }
    public void setCreator(PlayerDTO creator) { this.creator = creator; }

    public List<PlayerDTO> getPlayers() { return players; }
    public void setPlayers(List<PlayerDTO> players) { this.players = players; }

    public Mode getMode() { return mode; }
    public void setMode(Mode mode) { this.mode = mode; }

    public GameStateEnum getState() { return state; }
    public void setState(GameStateEnum state) { this.state = state; }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public PlayerDTO getTurnPlayer() {
        return turnPlayer;
    }

    public void setTurnPlayer(PlayerDTO turnPlayer) {
        this.turnPlayer = turnPlayer;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<PlayerDTO> getWinners() {
        return winners;
    }

    public void setWinners(List<PlayerDTO> winners) {
        this.winners = winners;
    }
}
