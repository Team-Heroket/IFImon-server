package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.constant.LobbyAction;
import ch.uzh.ifi.seal.soprafs20.constant.Mode;

public class GameTokenUserPutDTO {

    private String username;

    private LobbyAction action;


    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public LobbyAction getAction() {
        return action;
    }
    public void setAction(LobbyAction action) {
        this.action = action;
    }


}
