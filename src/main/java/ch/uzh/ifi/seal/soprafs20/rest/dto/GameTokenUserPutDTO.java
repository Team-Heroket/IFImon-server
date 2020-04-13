package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.constant.LobbyAction;
import ch.uzh.ifi.seal.soprafs20.constant.Mode;

public class GameTokenUserPutDTO {

    private Long id;

    private LobbyAction action;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public LobbyAction getAction() {
        return action;
    }
    public void setAction(LobbyAction action) {
        this.action = action;
    }


}
