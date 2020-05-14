package ch.uzh.ifi.seal.soprafs20.service.gamestates;

import ch.uzh.ifi.seal.soprafs20.entity.Deck;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.exceptions.game.GameBadRequestException;
import ch.uzh.ifi.seal.soprafs20.exceptions.game.GameConflictException;
import ch.uzh.ifi.seal.soprafs20.objects.UniqueBaseEvolutionPokemonGenerator;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.service.StatisticsHelper;
import ch.uzh.ifi.seal.soprafs20.service.gamestates.GameState;
import ch.uzh.ifi.seal.soprafs20.constant.*;
import org.hibernate.cfg.NotYetImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
    public void startGame(Game game, Integer npc, int deckSize, long buffer, int generation, GameRepository gameRepository) {

        log.debug("Rematch requested.");

        // create new decks
        UniqueBaseEvolutionPokemonGenerator uniquePkmId = new UniqueBaseEvolutionPokemonGenerator(game.getGeneration());
        for (Player player: game.getPlayers()) {
            player.setBerries(game.getPlayers().size());
            player.setEmote(0);
        }


        Runnable generateDeck = () -> {

            log.debug("Start creating cards.");

            for (int i = 0; i < game.getPlayers().size() ; i++) {
                game.getPlayers().get(i).setDeck(new Deck(uniquePkmId, deckSize));
            }

            log.debug("Start creating statistics.");

            // Does the pre statistics
            StatisticsHelper.doPreStatistics(game);

            log.debug("Set game to running.");
            //change game.state to running so the polling clients see the game has started and start calling "get board"
            game.setState(GameStateEnum.RUNNING);

            log.debug("Add start time.");
            //set creation date and time
            //DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            //LocalDateTime now = LocalDateTime.now();
            game.setStartTime( String.valueOf(System.currentTimeMillis() + buffer) );

            log.debug(String.format("Game will start at %s.", game.getStartTime()));

            log.debug("Finished concurrent task.");

            gameRepository.save(game);
            gameRepository.flush();

            log.debug("Regenerated deck.");
            log.debug("Changes saved.");
        };

        ExecutorService executorService =
                new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>());

        executorService.execute(generateDeck);


        // reset winners
        game.setWinners(new ArrayList<>());
        log.debug("Emptied winners");

        // set turnplayer to creator
        game.setTurnPlayer(game.getCreator());
        log.debug("Reset turn-player to creator");

        // reset category
        game.resetCategory();
        log.debug("Reset category");



    }

    @Override
    public void putEmote(Player player, Integer emote){
        player.setEmote(emote);
    }

    @Override
    public ArrayList<Player> getWinner(Game game) {
        return null;
    }
}
