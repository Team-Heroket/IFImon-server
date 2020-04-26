package ch.uzh.ifi.seal.soprafs20.service.gamestates;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.exceptions.game.GameBadRequestException;
import ch.uzh.ifi.seal.soprafs20.exceptions.game.GameConflictException;
import ch.uzh.ifi.seal.soprafs20.service.gamestates.GameState;
import ch.uzh.ifi.seal.soprafs20.constant.*;
import org.hibernate.cfg.NotYetImplementedException;

import java.util.ArrayList;
import java.util.List;

public class Finished implements GameState {
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
    public void startGame(Game game, Integer npc) {
        //TODO: maybe rematch
        throw new NotYetImplementedException("Rematch?");
    }

    @Override
    public ArrayList<Player> getWinner(Game game) {
        return null;
    }
}
