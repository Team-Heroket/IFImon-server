package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.objects.UniqueBaseEvolutionPokemonGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "DECK")
public class Deck {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
    }

    public Deck(UniqueBaseEvolutionPokemonGenerator ids, int cards) {
        this.cards = new ArrayList<>();

        for (int i = 0; i < cards; i++) {
            this.cards.add(new Card(ids.get()));
        }

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
    public Card peekCard() {
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
    public void evolveCard(Integer times) {

            // Does not remove yet, if something crashes card would be lost...
            Card toEvolve = this.peekCard();

            // Get next element of the evolve array

            Card evolvedCard = new Card(toEvolve.getEvolutionNames().get(times-1));

            // Removes old card
            this.removeCard();
            // Adds new card TO THE TOP
            this.cards.add(0, evolvedCard); // !Shift operation!
    }

    public List<Card> getCards() {
        return cards;
    }

    public Long getId() {
        return id;
    }

    public boolean isEmpty(){
        return this.cards.isEmpty();
    }

}
