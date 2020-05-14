package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.repository.CardRepository;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PokeAPICacheRepository;
import ch.uzh.ifi.seal.soprafs20.rest.dto.CardGetDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;


public class CardServiceTest {
    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardService cardService;

    private Card card;

    @Mock
    private PokeAPICacheRepository pokeAPICacheRepository;

    @MockBean
    private PokeAPICacheService pokeAPICacheService;


    // the cardservicetest tests most methods from the deck class

    @Test
    public void Test_addCardtoDeck() {
        // check if deck is empty
        Deck deck = new Deck();
        assertEquals(0, deck.getCards().size());

        // now add a card and check for increased deck size
        deck.addCard(new Card(1));
        assertEquals(1,deck.getCards().size());
    }

    @Test
    public void Test_getPeekCard() {
        // create a deck with two cards (bulbasaur on top)
        Deck deck = new Deck();
        deck.addCard(new Card(1));
        deck.addCard(new Card(2));

        // check decksize == 2 and top card is bulbasaur
        assertEquals(2,deck.getCards().size());
        assertEquals(1,deck.peekCard().getPokemonId());
    }

    @Test
    public void Test_removeCardFromDeck() {
        // create a deck with two cards (bulbasaur on top)
        Deck deck = new Deck();
        deck.addCard(new Card(1));
        deck.addCard(new Card(2));

        // check decksize == 2 and top card is bulbasaur
        assertEquals(2,deck.getCards().size());
        assertEquals(1,deck.peekCard().getPokemonId());

        // now remove one card and then top card should be ivysaur and decksize == 1
        deck.removeCard();
        assertEquals(1,deck.getCards().size());
        assertEquals(2,deck.peekCard().getPokemonId());
    }

/** cant mock static methods
    @Test
    public void Test_evolveTopPeekCard() {
        // create a deck with a bulbasaur
        Deck deck = new Deck();
        deck.addCard(new Card(1));
        assertEquals(1,deck.peekCard().getPokemonId());

        // evolve to ivysaur
        deck.evolveCard(1);
        assertEquals(2,deck.peekCard().getPokemonId());

        // evolve to bisaflor
        deck.evolveCard(1);
        assertEquals(3,deck.peekCard().getPokemonId());
    }
    **/

    @Test
    public void Test_isEmptyDeck() {
        // create an empty deck
        Deck deck = new Deck();
        assertEquals(true, deck.isEmpty());

        // add a card and deck should not be empty
        deck.addCard(new Card(1));
        assertEquals(false, deck.isEmpty());
    }

    @Test
    public void Test_initializeCard() {
        // create a bulbasaur
        Card testCard = new Card(1);

        assertEquals("Bulbasaur",testCard.getName());
        assertEquals(1,testCard.getPokemonId());
        assertNotEquals(0, testCard.getEvolutionNames().size());
    }
}
