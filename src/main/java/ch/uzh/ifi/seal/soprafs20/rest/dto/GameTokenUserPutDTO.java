package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.constant.Mode;

public class GameTokenUserPutDTO {

    private String username;

    private int action;


    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public int getAction() {
        return action;
    }
    public void setAction(int action) {
        this.action = action;
    }


}
