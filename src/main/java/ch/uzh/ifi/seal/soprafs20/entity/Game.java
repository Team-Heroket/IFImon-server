package ch.uzh.ifi.seal.soprafs20.entity;


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

    public String getToken() { return token; }

    public void setToken(String input) {
        this.token = input;
    }

}
