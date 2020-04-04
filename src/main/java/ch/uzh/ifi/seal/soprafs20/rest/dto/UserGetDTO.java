package ch.uzh.ifi.seal.soprafs20.rest.dto;

public class UserGetDTO {

    private String username;

    private int avatarId;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public int getAvatarId() { return avatarId; }
    public void setAvatarId(int avatarId) { this.avatarId = avatarId; }
}
