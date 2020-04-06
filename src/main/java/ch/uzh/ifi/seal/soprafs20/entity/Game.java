package ch.uzh.ifi.seal.soprafs20.entity;


import ch.uzh.ifi.seal.soprafs20.constant.Mode;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "GAME")
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private String gameName;

    @Column(nullable = false)
    private Mode mode;

    //TODO: Add board entity




    public String getGameName() {
        return gameName;
    }
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public Mode getMode() {
        return mode;
    }
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}
}
