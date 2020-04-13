package ch.uzh.ifi.seal.soprafs20.exceptions.user;

import ch.uzh.ifi.seal.soprafs20.exceptions.IFImonException;

public class UserNotFoundException extends IFImonException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public String getTitle() {
        return "User not found";
    }

}
