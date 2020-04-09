package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.entity.Statistics;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.*;

public class PlayerDTO {


    private UserGetDTO user;
    private Long id;
    


    public UserGetDTO getUserGetDTO() { return user; }
    public void setUserGetDTO(UserGetDTO userGetDTO) { this.user = user; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

}
