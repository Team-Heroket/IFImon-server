package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class StatisticsHelperTest {
    @Mock
    private StatisticsHelper statisticsHelper;


    @Test
    public void Test_doPreStatistics() {
        // given a user who has only seen ivysaur before
        User testUser = new User();
        testUser.setStatistics(new Statistics());
        testUser.getStatistics().getEncounteredPokemon().add(2);

        // given user gets a bulbasaur as a card in his deck
        Player player1 = new Player(testUser);
        player1.setDeck(new Deck());
        player1.getDeck().addCard(new Card(1));

        // the prestatistics method now adds the users cards as seen to the encountered pokemons of the user
        Game testGame = new Game(player1);
        statisticsHelper.doPreStatistics(testGame);

        assertEquals(2, testUser.getStatistics().getEncounteredPokemon().size());
    }

    @Test
    public void Test_doPostStatistics() {
        // given a user who has only seen ivysaur before
        User testUser = new User();
        testUser.setStatistics(new Statistics());
        testUser.getStatistics().getEncounteredPokemon().add(2);

        // given user gets a bulbasaur as a card in his deck
        Player player1 = new Player(testUser);
        player1.setDeck(new Deck());
        player1.getDeck().addCard(new Card(1));

        // create a game with player1 as winner
        Game testGame = new Game(player1);
        List<Player> winners = new ArrayList<>();
        winners.add(player1);
        testGame.setWinners(winners);

        // now do poststatistics
        statisticsHelper.doPostStatistics(testGame);

        // testuser amount of wins should be 1 now
        assertEquals(1,testUser.getStatistics().getGamesWon());
        assertEquals(2,testUser.getStatistics().getRating());
    }

    @Test
    public void Test_doPostStatistics_withoutWinner() {
        // given a user who has only seen ivysaur before
        User testUser = new User();
        testUser.setStatistics(new Statistics());
        testUser.getStatistics().getEncounteredPokemon().add(2);

        // given user gets a bulbasaur as a card in his deck
        Player player1 = new Player(testUser);
        player1.setDeck(new Deck());
        player1.getDeck().addCard(new Card(1));

        // create a game with empty winner list
        Game testGame = new Game(player1);
        List<Player> noWinners = new ArrayList<>();
        testGame.setWinners(noWinners);

        // now do poststatistics, but we expect an exception because we have no winner
        assertThrows(SopraServiceException.class, () -> statisticsHelper.doPostStatistics(testGame));
    }

    @Test
    public void Test_encounter() {
        // given a user who has only seen ivysaur before
        User testUser = new User();
        testUser.setStatistics(new Statistics());
        testUser.getStatistics().getEncounteredPokemon().add(2);
        Player player1 = new Player(testUser);

        // let player1 encounter bulbasaur as a single card (not in deck)
        statisticsHelper.encounter(player1,new Card(1));

        // encountered size must be 2 now
        assertEquals(2, testUser.getStatistics().getEncounteredPokemon().size());
    }
}
