package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.objects.UniquePokemonNameGenerator;
import ch.uzh.ifi.seal.soprafs20.objects.UniqueTrainerNameGenerator;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * The temporary values of a player in a game
 */
@Entity
@Table(name = "NPC")
public class Npc extends Player{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cascade since we update statistics trough player
    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    private Deck deck;

    @Column
    private Integer berries;

    public Npc() {

    };
    public Npc(String name) {
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("none");
        userPostDTO.setUsername(name + "_BOT");
        //set random avatar
        Random random=new Random();
        userPostDTO.setAvatarId(random.nextInt(59)+1);
        User npcUser = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        //set user object online
        npcUser.setOnline(true);

        //set creation date on user object
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDateTime now = LocalDateTime.now();
        npcUser.setCreationDate(pattern.format(now));

        //init statistics
        npcUser.setStatistics(new Statistics());

        //set npc flag
        npcUser.setNpc(true);

        // saves the given entity
        this.user=npcUser;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public Integer getBerries() {
        return berries;
    }

    public void setBerries(Integer berries) {
        this.berries = berries;
    }
}
