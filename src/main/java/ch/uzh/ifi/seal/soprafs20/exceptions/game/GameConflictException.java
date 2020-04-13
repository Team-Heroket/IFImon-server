package ch.uzh.ifi.seal.soprafs20.exceptions.game;

import ch.uzh.ifi.seal.soprafs20.exceptions.IFImonException;

public class GameConflictException extends IFImonException {

    public GameConflictException(String message) {
        super(message);
    }

    public String getTitle() {
        return "Game conflict";
    }

}
