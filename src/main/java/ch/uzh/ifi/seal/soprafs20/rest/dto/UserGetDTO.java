package ch.uzh.ifi.seal.soprafs20.rest.dto;

public class UserGetDTO {

    private String username;

    private long avatarId;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public long getAvatar() { return avatarId; }
    public void setAvatar(long avatar) { this.avatarId = avatar; }
}
