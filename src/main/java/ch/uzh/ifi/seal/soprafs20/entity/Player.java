package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.*;

/**
 * The temporary values of a player in a game
 */
@Entity
@Table(name = "PLAYER")
public class Player {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cascade since we update statistics trough player
    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    private Deck deck;

    @Column
    private Integer berries;

    public Player() {};

    public Player(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public Integer getBerries() {
        return berries;
    }

    public void setBerries(Integer berries) {
        this.berries = berries;
    }
}
