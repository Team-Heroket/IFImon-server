package ch.uzh.ifi.seal.soprafs20.service;


import ch.uzh.ifi.seal.soprafs20.constant.GameStateEnum;
import ch.uzh.ifi.seal.soprafs20.constant.Mode;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class NPCServiceTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    private Npc testNpc;

    @BeforeEach
    public void setup() {
        testNpc = new Npc("test");
    }

    @Test
    public void Test_NPC_constructor() {
        assertEquals("test_BOT",testNpc.getUser().getUsername());
        assertEquals("none",testNpc.getUser().getPassword());
        assertNotNull(testNpc.getUser().getCreationDate());
        assertEquals(0,testNpc.getUser().getStatistics().getGamesWon());
        assertEquals(true,testNpc.getUser().getOnline());
    }

    @Test
    public void Test_NPC_initializationParameters() {
        testNpc.getUser().setId(100L);
        assertEquals(100L,testNpc.getUser().getId());

        Deck deck = new Deck();
        deck.addCard(new Card("bulbasaur"));
        testNpc.setDeck(deck);
        assertNotNull(testNpc.getDeck());
        assertEquals(1,testNpc.getDeck().getCards().get(0).getPokemonId());

        testNpc.setBerries(10);
        assertEquals(10,testNpc.getBerries());
    }


}
