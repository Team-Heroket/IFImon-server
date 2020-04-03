package ch.uzh.ifi.seal.soprafs20.rest.dto;

public class UserGetDTO {

    private String username;

    private long avatar;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public long getAvatar() { return avatar; }
    public void setAvatar(long avatar) { this.avatar = avatar; }
}
