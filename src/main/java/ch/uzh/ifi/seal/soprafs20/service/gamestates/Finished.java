package ch.uzh.ifi.seal.soprafs20.service.gamestates;

import ch.uzh.ifi.seal.soprafs20.entity.Deck;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.exceptions.game.GameBadRequestException;
import ch.uzh.ifi.seal.soprafs20.exceptions.game.GameConflictException;
import ch.uzh.ifi.seal.soprafs20.objects.UniqueBaseEvolutionPokemonGenerator;
import ch.uzh.ifi.seal.soprafs20.service.StatisticsHelper;
import ch.uzh.ifi.seal.soprafs20.service.gamestates.GameState;
import ch.uzh.ifi.seal.soprafs20.constant.*;
import org.hibernate.cfg.NotYetImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Finished implements GameState {

    private final Logger log = LoggerFactory.getLogger(Finished.class);

    @Override
    public void addPlayer(Game game, User user) {
        throw new GameBadRequestException("You can't add new players to a finished game.");
    }
    @Override
    public void removePlayer(Game game, User user) {
        List<Player> players = game.getPlayers();

        for (Player player: players) {
            if (player.getUser().getId().equals(user.getId())) {
                players.remove(player);
                return;
            }
        }

        // We could also throw nothing, and just ignore the wrong request.
        throw new GameConflictException("You can't remove a player, that does not exist");
    }

    @Override
    public void selectCategory(Game game, Category category) {
        throw new GameBadRequestException("Can't select Category when game is done");
    }

    @Override
    public void useBerries(Game game, Integer usedBerries, Player player) {
        throw new GameBadRequestException("Can't use Berries when game is done");
    }

    @Override
    public void nextTurn(Game game) {
        throw new GameBadRequestException("Can't get next Turn when game is over");
    }

    @Override
    public void startGame(Game game, Integer npc, int deckSize, long buffer) {

        log.debug("Rematch requested.");

        // create new decks
        UniqueBaseEvolutionPokemonGenerator uniquePkmId = new UniqueBaseEvolutionPokemonGenerator();
        for (Player player: game.getPlayers()) {
            player.setBerries(game.getPlayers().size());
            player.setDeck(new Deck(uniquePkmId, deckSize));
        }
        log.debug("Regenerated deck.");

        // reset winners
        game.setWinners(new ArrayList<>());
        log.debug("Emptied winners");

        // set turnplayer to creator
        game.setTurnPlayer(game.getCreator());
        log.debug("Reset turn-player to creator");

        // reset category
        game.resetCategory();
        log.debug("Reset category");

        // do pre statistics
        StatisticsHelper.doPreStatistics(game);
        log.debug("Did pre-statistics");

        // set state to running
        game.setState(GameStateEnum.RUNNING);
        log.debug("Game state is RUNNING");

        // set start time
        game.setStartTime( String.valueOf(System.currentTimeMillis() + buffer) );
        log.debug("New start time set.");

    }

    @Override
    public ArrayList<Player> getWinner(Game game) {
        return null;
    }
}
