package ch.uzh.ifi.seal.soprafs20.objects;

import ch.uzh.ifi.seal.soprafs20.constant.Category;

import java.util.List;

public class Board {
    private List<Player> players;
    private Player turnPlayer;
    private Category chosenCategory;
    private Long timer;

    public List<Player> getPlayers() { return players; }
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Player getTurnPlayer(){ return turnPlayer; }
    public void setTurnPlayer(Player turnPlayer){ this.turnPlayer=turnPlayer;}

    public Category getchosenCategory(){ return chosenCategory;}
    public void setChosenCategory(Category chosenCategory){ this.chosenCategory=chosenCategory;}

    public Long getTimer(){return timer;}
    public void setTimer(Long timer){this.timer=timer;}
}

