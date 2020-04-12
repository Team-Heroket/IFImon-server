package ch.uzh.ifi.seal.soprafs20.exceptions;

public abstract class IFImonException extends RuntimeException{

    public IFImonException(String message) {
        super(message);
    }

    public abstract String getTitle();
}
