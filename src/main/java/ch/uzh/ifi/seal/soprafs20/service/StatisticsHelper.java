package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.entity.Statistics;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;

import java.util.List;

public class StatisticsHelper {

    public static void doPreStatistics(Game game) {
        // This exists (for now) only for the encountered pokémons, since the initial deck is important

        List<Player> players = game.getPlayers();

        for (Player player: players) {
            List<Integer> encountered = player.getUser().getStatistics().getEncounteredPokemon();

            // This way the IDs should be add ordered to the list, which is helpful for later
            for (Card card: player.getDeck().getCards()) {
                int id = card.getPokemonId();
                // If list is empty just add it
                if (encountered.isEmpty()) {
                    encountered.add(id);
                } else {
                    // Go trough list
                    for (int i = 0; i < encountered.size(); i++) {
                        // Add element before the next bigger number
                        if (id < encountered.get(i)) {
                            encountered.add(i, id);
                            // Stop the loop!
                            break;
                        }
                    }
                    // Add id if its bigger as the last value (or else it is the same number)
                    if (id > encountered.get(encountered.size()-1)){
                        encountered.add(id);
                    }
                }

            }

        }

    }

    public static void doPostStatistics(Game game) {
        List<Player> players = game.getPlayers();
        List<Player> winners = game.getWinners();

        // Maybe remove, since precondition
        if (0 == winners.size()) {
            throw new SopraServiceException("The game ended without winners. doStatistics should not be called!");
        }

        // *** TODO: story progress

        // *** games played
        for (Player player: players) {
            Statistics stat = player.getUser().getStatistics();
            stat.setGamesPlayed(stat.getGamesPlayed()+1);
        }

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
