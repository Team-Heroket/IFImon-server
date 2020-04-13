package ch.uzh.ifi.seal.soprafs20.exceptions;

public class APIError {

    private String error;
    private String message;

    public String getError() {
        return error;
    }

    public APIError setError(String error) {
        this.error = error;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public APIError setMessage(String message) {
        this.message = message;
        return this;
    }
}
