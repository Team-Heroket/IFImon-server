package ch.uzh.ifi.seal.soprafs20.rest.dto;

public class UserPostDTO {

    private String username;

    private String password;

    private Integer avatarId;


    public Integer getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(Integer input) {
        this.avatarId = input;
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
}
