package ch.uzh.ifi.seal.soprafs20.rest.dto;

public class UserPutDTO {

    private String username;

    private String password;

    private String token;

    private Integer avatarId;

    public Integer getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(Integer avatarId) {
        this.avatarId = avatarId;
    }

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
