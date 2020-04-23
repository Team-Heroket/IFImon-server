package ch.uzh.ifi.seal.soprafs20.service.gamestates;

import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.exceptions.game.GameBadRequestException;
import ch.uzh.ifi.seal.soprafs20.exceptions.game.GameConflictException;
import ch.uzh.ifi.seal.soprafs20.objects.UniqueBaseEvolutionPokemonGenerator;
import ch.uzh.ifi.seal.soprafs20.objects.UniqueTrainerNameGenerator;
import ch.uzh.ifi.seal.soprafs20.constant.*;
import ch.uzh.ifi.seal.soprafs20.service.StatisticsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Lobby implements GameState {

    private final Logger log = LoggerFactory.getLogger(Lobby.class);

    @Override
    public void addPlayer(Game game, User user) {
        log.debug(String.format("%s wants to join.", user.getUsername()));
        List<Player> players = game.getPlayers();

        // Check if the user is already added
        for (Player player: players) {
            if (user.getId().equals(player.getUser().getId())) {
                throw new GameConflictException("This player is already in the game.");
            }
        }

        //init player based on this user and append player
        game.addPlayer(new Player(user));
        log.debug("User joined.");
    }

    @Override
    public void removePlayer(Game game, User user) {
        log.debug(String.format("%s will be removed from lobby.", user.getUsername()));
        List<Player> players = game.getPlayers();

        
        for (Player player: players) {
            if (player.getUser().getId().equals(user.getId())) {
                players.remove(player);
                log.debug("User removed.");
                return;
            }
        }

        // We could also throw nothing, and just ignore the wrong request.
        throw new GameConflictException("You can't remove a player, that does not exist");
    }

    @Override
    public void startGame(Game game, Integer npc) {

        log.info(String.format("Start game request was called on %s. Amount of NPCs: %s.", game.getToken(), npc));


        //for testing here are some parameters we can change
        Integer deckSize= 1;
        Long buffer=15000L; // in milliseconds

        //add npcs
        UniqueTrainerNameGenerator trainerNameGenerator = new UniqueTrainerNameGenerator();
        for (int i = 0; i < npc; i++) {
            game.getPlayers().add(new Npc(trainerNameGenerator.get()));
        }


        //give each player a deck and set turn player = game.creator if not done already
        game.setTurnPlayer(game.getCreator());

        UniqueBaseEvolutionPokemonGenerator uniquePkmId = new UniqueBaseEvolutionPokemonGenerator();
        for (Player player: game.getPlayers()) {
            // # players = # berries
            player.setBerries(game.getPlayers().size());
            player.setDeck(new Deck(uniquePkmId, deckSize));
        }

        // Does the pre statistics
        StatisticsHelper.doPreStatistics(game);

        //change game.state to running so the polling clients see the game has started and start calling "get board"
        game.setState(GameStateEnum.RUNNING);


        //set creation date and time
        //DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //LocalDateTime now = LocalDateTime.now();
        game.setStartTime( String.valueOf(System.currentTimeMillis() + buffer) );
        // TODO: update game entity accordingly

        log.debug(String.format("Game will start at %s.", game.getStartTime()));
        log.debug(String.format("Game created. %s", game));

    }



    @Override
    public void selectCategory(Game game, Category category) {
        throw new GameBadRequestException("Can't select Category in the Lobby");
    }

    @Override
    public void useBerries(Game game, Integer usedBerries, Player player) {
        throw new GameBadRequestException("Can't use Berries in Lobby");
    }

    @Override
    public void nextTurn(Game game) {
        throw new GameBadRequestException("Can't get next Turn when in lobby");
    }

    @Override
    public ArrayList<Player> getWinner(Game game) {
        return null;
    }
}
