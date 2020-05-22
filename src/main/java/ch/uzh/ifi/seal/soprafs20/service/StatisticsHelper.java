package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class StatisticsHelper {

    private final static Logger log = LoggerFactory.getLogger(StatisticsHelper.class);

    public static void doPreStatistics(Game game) {
        List<Player> players = game.getPlayers();

        // *** games played
        for (Player player: players) {
            Statistics stat = player.getUser().getStatistics();
            stat.setGamesPlayed(stat.getGamesPlayed()+1);
        }

        // The player only encounters his the pokémons from his own deck
        for (Player player: players) {
            if (!(player instanceof Npc)) {
                TreeSet<Integer> encountered = new TreeSet<>(player.getUser().getStatistics().getEncounteredPokemon());
                for (Card card: player.getDeck().getCards()) {
                    int id = card.getPokemonId();
                    encountered.add(id);
                }
                player.getUser().getStatistics().setEncounteredPokemon(new ArrayList<>(encountered));
            }
        }

    }

    /**
     * Encounter Pokémon in game.
     *
     * The only two possiblities are evolving your own card
     * and getting a card from "enemy" player
     *
     * @param player who encounters the pokémon
     * @param card the pokémon to encounter
     */
    public static void encounter(Player player, Card card) {
        if (!(player instanceof Npc)) {
            TreeSet<Integer> encountered = new TreeSet<>(player.getUser().getStatistics().getEncounteredPokemon());
            int id = card.getPokemonId();
            encountered.add(id);
            player.getUser().getStatistics().setEncounteredPokemon(new ArrayList<>(encountered));
        }
        // somewhat code duplication from the method doPreStatistics TODO: refactor encounter pokémon
    }

    public static void doPostStatistics(Game game) {
        List<Player> players = game.getPlayers();
        List<Player> winners = game.getWinners();

        // Maybe remove, since precondition
        if (0 == winners.size()) {
            throw new SopraServiceException("The game ended without winners. doStatistics should not be called!");
        }

        // *** TODO: story progress

        // These only change, if there is one winner
        if (1 == winners.size()) {
            // *** Rate
            // subtract for loosing points
            for (Player player: players) {
                Statistics stat = player.getUser().getStatistics();
                // If player is at the bottom, don't go negative
                if (stat.getRating() > 0) {
                    stat.setRating(stat.getRating() - 1);
                }
                // Yes I subtract for all players, the winner gets the point back
            }

            Statistics winnerStats = winners.get(0).getUser().getStatistics();
            winnerStats.setRating(winnerStats.getRating() + 2); // + 2 since one was subtracted before

            // *** games won
            winnerStats.setGamesWon(winnerStats.getGamesWon() + 1);
        }

    }

}
