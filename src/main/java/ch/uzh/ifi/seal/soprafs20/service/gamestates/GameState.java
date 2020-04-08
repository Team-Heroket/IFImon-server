package ch.uzh.ifi.seal.soprafs20.service.gamestates;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.User;

public interface GameState {
    public void addPlayer(Game game, User user);
    public void removePlayer(Game game, User user);
}
