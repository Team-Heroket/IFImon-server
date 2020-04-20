package ch.uzh.ifi.seal.soprafs20.service.gamestates;

import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.exceptions.game.GameBadRequestException;
import ch.uzh.ifi.seal.soprafs20.exceptions.game.GameConflictException;
import ch.uzh.ifi.seal.soprafs20.objects.UniqueBaseEvolutionPokemonGenerator;
import ch.uzh.ifi.seal.soprafs20.objects.UniqueTrainerNameGenerator;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.constant.*;
import ch.uzh.ifi.seal.soprafs20.service.StatisticsHelper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Lobby implements GameState {

    @Override
    public void addPlayer(Game game, User user) {
        List<Player> players = game.getPlayers();

        // Check if the user is already added
        for (Player player: players) {
            if (user.getId().equals(player.getUser().getId())) {
                throw new GameConflictException("This player is already in the game.");
            }
        }

        //init player based on this user and append player
        game.addPlayer(new Player(user));
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
    public void startGame(Game game, Integer npc) {
        //loop (from 0 to npc): render NPCs and add them to game
        // TODO: Sprint 4 create NPCs

        //for testing here are some parameters we can change
        Integer deckSize= 5;
        Long buffer=35L;

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
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        game.setStartTime(pattern.format(now.plusSeconds(buffer)));

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
