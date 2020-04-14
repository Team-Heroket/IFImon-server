package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.entity.Statistics;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.*;

public class PlayerDTO {


    private UserGetDTO user;
    private Long id;
    private Integer berries;


    public UserGetDTO getUser() { return user; }
    public void setUser(UserGetDTO user) { this.user = user; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getBerries() {
        return berries;
    }

    public void setBerries(Integer berries) {
        this.berries = berries;
    }
}
