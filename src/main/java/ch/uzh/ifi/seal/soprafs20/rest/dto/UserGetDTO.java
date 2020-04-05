package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.entity.Statistics;

import javax.persistence.Column;
import javax.persistence.OneToOne;

public class UserGetDTO {


    private String username;

    private int avatarId;

    private Statistics statistics;

    private String creationDate;

    private boolean online;

    private Long id;




    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public int getAvatarId() { return avatarId; }
    public void setAvatarId(int avatarId) { this.avatarId = avatarId; }

    public Long getId() { return id; }
    public void setId(Long avatarId) { this.id = id; }


    public Statistics getStatistics() {
        return statistics;
    }
    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public String getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public boolean getOnline() {
        return online;
    }
    public void setOnline(boolean online) {
        this.online = online;
    }
}
