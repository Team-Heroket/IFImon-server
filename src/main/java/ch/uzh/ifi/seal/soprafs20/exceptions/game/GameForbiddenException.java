package ch.uzh.ifi.seal.soprafs20.exceptions.game;

import ch.uzh.ifi.seal.soprafs20.exceptions.IFImonException;

public class GameForbiddenException extends IFImonException {

    public GameForbiddenException(String message) {
        super(message);
    }

    public String getTitle() {
        return "Game forbidden";
    }

}
