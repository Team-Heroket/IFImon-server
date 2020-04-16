package ch.uzh.ifi.seal.soprafs20.service.gamestates;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.exceptions.game.GameBadRequestException;
import ch.uzh.ifi.seal.soprafs20.exceptions.game.GameConflictException;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.constant.*;

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
    public void selectCategory(Game game, Category category) {
        throw new GameBadRequestException("Can't select Category in the Lobby");
    }
}
