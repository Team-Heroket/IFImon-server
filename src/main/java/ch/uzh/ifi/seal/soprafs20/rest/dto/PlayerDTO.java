package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.entity.Deck;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.entity.Statistics;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.*;

public class PlayerDTO {


    private UserGetDTO user;
    private Long id;
    private Integer berries;
    private Deck deck;
    private Integer emote;


    public UserGetDTO getUser() { return user; }
    public void setUser(UserGetDTO user) { this.user = user; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getBerries() {
        return berries;
    }

    public void setBerries(Integer berries) {
        this.berries = berries;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public Integer getEmote() {
        return emote;
    }

    public void setEmote(Integer emote) {
        this.emote = emote;
    }
}
