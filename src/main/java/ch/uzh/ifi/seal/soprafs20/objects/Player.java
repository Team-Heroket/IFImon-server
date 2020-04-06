package ch.uzh.ifi.seal.soprafs20.objects;

import ch.uzh.ifi.seal.soprafs20.entity.User;

import java.util.List;

public class Player {
    private Deck deck;
    private User user;
    private int berries;


    public Deck getDeck() { return deck; }
    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public User getUser() { return user; }
    public void setUser(User user) {
        this.user = user;
    }

    public int getBerries() { return berries; }
    public void setBerries(int berries) {
        this.berries=berries;
    }

}
