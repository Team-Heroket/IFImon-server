package ch.uzh.ifi.seal.soprafs20.exceptions.game;

import ch.uzh.ifi.seal.soprafs20.exceptions.IFImonException;

public class GameBadRequestException extends IFImonException {

    public GameBadRequestException(String message) {
        super(message);
    }

    public String getTitle() {
        return "Game bad request";
    }

}
