package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.entity.Statistics;

import javax.persistence.*;
import java.io.Serializable;


/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes the primary key
 */
@Entity
@Table(name = "USER")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    //username
    //password
    //id
    //token
    //avatarId
    //statistics
    //creationDate
    //online

    @Id
    @GeneratedValue
    private Long id;

    //add " unique = false" in parameters to make usernames unique
    @Column(nullable = false, unique=true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private String token;

    @Column(nullable = false)
    private int avatarId;

    @OneToOne(targetEntity = Statistics.class, cascade = {CascadeType.ALL})
    private Statistics statistics;

    @Column(nullable = false)
    private String creationDate;

    @Column(nullable = false)
    private boolean online;



    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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
    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public int getAvatarId() {
        return avatarId;
    }
    public void setAvatarId(int avatarId) {
        this.avatarId = avatarId;
    }


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
