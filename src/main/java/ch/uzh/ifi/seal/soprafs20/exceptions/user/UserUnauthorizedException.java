package ch.uzh.ifi.seal.soprafs20.exceptions.user;

import ch.uzh.ifi.seal.soprafs20.exceptions.IFImonException;

public class UserUnauthorizedException extends IFImonException {
    public UserUnauthorizedException(String message) {
        super(message);
    }

    public String getTitle() {
        return "User unauthorized";
    }
}
