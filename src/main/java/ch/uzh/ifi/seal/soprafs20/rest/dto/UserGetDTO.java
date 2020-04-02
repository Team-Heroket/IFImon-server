package ch.uzh.ifi.seal.soprafs20.rest.dto;

public class UserGetDTO {

    private String token;
    private String username;
    private UserStatus status;

    public String getToken() {
        return token;
    }

    public void setToken(String token) { this.token = token; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
}
