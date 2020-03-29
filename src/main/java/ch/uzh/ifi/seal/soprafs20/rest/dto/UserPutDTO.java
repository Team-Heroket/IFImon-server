package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;

public class UserPutDTO {

    private String username;

    private String password;

    private String token;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String input) {
        this.password = input;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String input) {
        this.token = input;
    }
}
