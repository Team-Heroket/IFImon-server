package ch.uzh.ifi.seal.soprafs20.service.gamestates;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.exceptions.game.GameBadRequestException;
import ch.uzh.ifi.seal.soprafs20.service.gamestates.GameState;

public class Finished implements GameState {
    @Override
    public void addPlayer(Game game, User user) {
        throw new GameBadRequestException("You can't add new players to a finished game.");
    }
    @Override
    public void removePlayer(Game game, User user) {
        throw new GameBadRequestException("A player can't leave a finished game.");
    }
}
