package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.constant.GameStateEnum;
import ch.uzh.ifi.seal.soprafs20.constant.Mode;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.entity.Statistics;

import javax.persistence.*;
import java.util.List;

public class GameGetDTO {


    private Long id;

    private String gameName;

    private String token;

    private Player creator;

    private List<Player> players;

    private Mode mode;

    private GameStateEnum state;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getGameName() { return gameName; }
    public void setGameName(String gameName) { this.gameName = gameName; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Player getCreator() { return creator; }
    public void setCreator(Player creator) { this.creator = creator; }

    public List<Player> getPlayers() { return players; }
    public void getPlayers(List<Player> players) { this.players = players; }

    public Mode getMode() { return mode; }
    public void setMode(Mode mode) { this.mode = mode; }

    public GameStateEnum getState() { return state; }
    public void setState(GameStateEnum state) { this.state = state; }


}
