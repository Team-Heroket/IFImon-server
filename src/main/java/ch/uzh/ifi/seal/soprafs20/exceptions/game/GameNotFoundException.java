package ch.uzh.ifi.seal.soprafs20.exceptions.game;

import ch.uzh.ifi.seal.soprafs20.exceptions.IFImonException;

public class GameNotFoundException extends IFImonException {

    public GameNotFoundException(String message) {
        super(message);
    }

    public String getTitle() {
        return "Game not found";
    }

}
