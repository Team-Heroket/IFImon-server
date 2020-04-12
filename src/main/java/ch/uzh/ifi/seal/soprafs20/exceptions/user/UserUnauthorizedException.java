package ch.uzh.ifi.seal.soprafs20.exceptions.user;

public class UserUnauthorizedException extends RuntimeException {
    public UserUnauthorizedException(String message) {
        super(message);
    }

    public String getTitle() {
        return "User unauthorized";
    }
}
