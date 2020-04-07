package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.GameState;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "GAME")
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String token;

    // Should only be readable!
    @OneToOne
    private Player creator;

    @OneToMany
    private List<Player> players;

    // Removed mode, since we don't need to keep track of that actually

    // Cool, gets converted to string for database and back to enum for java
    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private GameState state;

    // TODO: table

    // TODO: chat

}
