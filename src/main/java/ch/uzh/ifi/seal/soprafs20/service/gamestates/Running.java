package ch.uzh.ifi.seal.soprafs20.service.gamestates;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.exceptions.game.GameBadRequestException;
import org.hibernate.cfg.NotYetImplementedException;

public class Running implements GameState {
    @Override
    public void addPlayer(Game game, User user) {
        throw new GameBadRequestException("You can't add new players to a running game.");
    }
    @Override
    public void removePlayer(Game game, User user) {
        // TODO: Do this, when the user leaves.
        throw new NotYetImplementedException("Will be implemented in Sprint 3");
    }
}
