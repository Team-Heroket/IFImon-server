package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
@Table(name = "DECK")
public class Deck {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    private ArrayList<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
    }

    /**
     * Add a card to the end of the deck
     *
     * @param card to add
     */
    public Deck addCard(Card card) {
        // end of list
        this.cards.add(card);
        return this;
    }

    /**
     * Returns the card from the top of the deck
     *
     * @return Card
     */
    public Card getCard() {
        // first card of list
        return this.cards.get(0);
    }

    /**
     * Returns the card from the top AND REMOVES it.
     *
     * @return removed Card
     */
    public Card removeCard() {
        // Returns and removes card
        return this.cards.remove(0); // !Shift operation!
    }

    /**
     * Evolves card at the top
     *
     * @pre The card is evolve-able
     * @return The evolved Card
     */
    public Card evolveCard() {

        // Does not remove yet, if something crashes card would be lost...
        Card toEvolve = this.getCard();

        // Get next element of the evolve array

        Card evolvedCard = new Card(/* ID of evolved Pkmn */);

        // Removes old card
        this.removeCard();
        // Adds new card TO THE TOP
        this.cards.add(0, evolvedCard); // !Shift operation!

        // Maybe not necessary
        return evolvedCard;
    }

}
