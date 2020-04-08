package ch.uzh.ifi.seal.soprafs20.rest.dto;

import java.util.*;
import ch.uzh.ifi.seal.soprafs20.constant.*;
import ch.uzh.ifi.seal.soprafs20.objects.Player;

public class BoardGetDTO {


    private List<Player> players;
    private Player turnPlayer;
    private Category chosenCategory;
    private Long timer;




    public List<Player> getPlayers() { return players; }
    public void setPlayers(List<Player> players) { this.players = players; }

    public Player getTurnPlayer() { return turnPlayer; }
    public void setTurnPlayer(Player turnPlayer) { this.turnPlayer = turnPlayer; }

    public Category getChosenCategory() { return chosenCategory; }
    public void setChosenCategory(Category chosenCategory) { this.chosenCategory = chosenCategory; }

    public Long getTimer() { return timer; }
    public void setTimer(Long timer) { this.timer = timer; }

}
