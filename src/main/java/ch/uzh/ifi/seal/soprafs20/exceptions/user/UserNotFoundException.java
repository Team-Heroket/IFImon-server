package ch.uzh.ifi.seal.soprafs20.exceptions.user;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public String getTitle() {
        return "User not found";
    }

}
