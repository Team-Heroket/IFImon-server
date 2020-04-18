package ch.uzh.ifi.seal.soprafs20.rest.mapper;

import ch.uzh.ifi.seal.soprafs20.constant.Category;
import ch.uzh.ifi.seal.soprafs20.constant.GameStateEnum;
import ch.uzh.ifi.seal.soprafs20.constant.Mode;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.objects.*;

import ch.uzh.ifi.seal.soprafs20.rest.dto.*;
import com.sun.xml.bind.v2.model.annotation.Quick;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation works.
 */
public class DTOMapperTest {

    @Test
    public void testCreateUser_fromUserPostDTO_toUser_success() {
        // create UserPostDTO
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("username");
        userPostDTO.setPassword("pw1");
        userPostDTO.setAvatarId(1);

        // MAP
        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // check content
        assertEquals(userPostDTO.getUsername(), user.getUsername());
        assertEquals(userPostDTO.getPassword(), user.getPassword());
        assertEquals(userPostDTO.getAvatarId(), user.getAvatarId());
    }

    @Test
    public void testGetUser_fromUser_toUserGetDTO_success() {
        // create User
        User user = new User();
        user.setUsername("username");
        user.setId(1L);
        user.setAvatarId(10);
        user.setCreationDate("today");
        user.setOnline(true);
        Statistics stats = new Statistics();
        user.setStatistics(stats);

        // MAP -> Create UserGetDTO
        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

        // check content
        assertEquals(userGetDTO.getUsername(), user.getUsername());
        assertEquals(userGetDTO.getId(), user.getId());
        assertEquals(userGetDTO.getAvatarId(), user.getAvatarId());
        assertEquals(userGetDTO.getCreationDate(), user.getCreationDate());
        assertEquals(userGetDTO.getOnline(), user.getOnline());
        assertEquals(userGetDTO.getStatistics(), user.getStatistics());
    }


    @Test
    public void testPutUser_fromUserPutDTO_toUser() {
        // create UserPutDTO
        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername("username");
        userPutDTO.setPassword("pw1");
        userPutDTO.setAvatarId(1);
        userPutDTO.setToken("tokenTest");

        // MAP
        User user = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);

        // check content
        assertEquals(userPutDTO.getUsername(), user.getUsername());
        assertEquals(userPutDTO.getPassword(), user.getPassword());
        assertEquals(userPutDTO.getAvatarId(), user.getAvatarId());
        assertEquals(userPutDTO.getToken(), user.getToken());
    }

    @Test
    public void testGetCard_fromCard_toCardGetDTO() {
        // create Card
        Card card = new Card();
        card.setName("Charizard");

        // MAP
        CardGetDTO cardGetDTO = DTOMapper.INSTANCE.convertEntityToCardGetDTO(card);

        // check content
        assertEquals(cardGetDTO.getName(), card.getName());
    }


    @Test
    public void testLoginUser_fromUser_toUserLoginDTO_success() {
        // create User
        User user = new User();
        user.setToken("testToken");
        user.setId(1L);

        // MAP -> Create UserGetDTO
        UserLoginDTO userLoginDTO = DTOMapper.INSTANCE.convertEntityToUserLoginDTO(user);

        // check content
        assertEquals(userLoginDTO.getToken(), user.getToken());
        assertEquals(userLoginDTO.getId(), user.getId());
    }

    @Test
    public void testPostGame_fromGamePostDTO_toGame() {
        // create GamePostDTO
        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setGameName("gameNameTest");
        Mode mode = Mode.QUICK;
        gamePostDTO.setMode(mode);

        // MAP
        Game game = DTOMapper.INSTANCE.convertGamePostDTOToEntity(gamePostDTO);

        // check content
        assertEquals(gamePostDTO.getMode(), game.getMode());
        assertEquals(gamePostDTO.getGameName(), game.getGameName());
    }

    @Test
    public void testPlayerDTO_fromEntity_toPlayerDTO() {
        // create Player
        User testUser = new User();
        testUser.setId(100L);
        Player testPlayer = new Player(testUser);
        testPlayer.setBerries(1);

        // MAP
        PlayerDTO playerDTO = DTOMapper.INSTANCE.convertEntityToPlayerDTO(testPlayer);

        // check content
        assertEquals(playerDTO.getId(), testPlayer.getId());
        assertEquals(playerDTO.getUser().getId(), testUser.getId());
        assertEquals(playerDTO.getBerries(), testPlayer.getBerries());
        assertEquals(playerDTO.getDeck(), null);

    }

    @Test
    public void testGetGame_fromGame_toGameGetDTO() {
        // create Game
        Player player1 = new Player();
        Player player2 = new Player();
        List<Player> players = new ArrayList<>();
        players.add(player1);
        player1.setId(100L);
        players.add(player2);
        player2.setId(2L);

        Game game = new Game(player1);
        game.addPlayer(player2);
        game.setId(1L);
        game.setToken("testToken");
        game.setGameName("firstTest");

        GameStateEnum gameStateEnum = GameStateEnum.RUNNING;
        Mode mode = Mode.QUICK;
        game.setMode(mode);
        game = game.setState(gameStateEnum);


        // MAP
        GameGetDTO gameGetDTO = DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);

        // check content
        assertEquals(gameGetDTO.getGameName(), game.getGameName());
        assertEquals(gameGetDTO.getId(), game.getId());
        assertEquals(gameGetDTO.getToken(), game.getToken());
        assertEquals(gameGetDTO.getPlayers().get(1).getId(), game.getPlayers().get(1).getId());
        assertEquals(gameGetDTO.getCreator().getId(), game.getCreator().getId());
        assertEquals(gameGetDTO.getMode(), game.getMode());
        assertEquals(gameGetDTO.getState(), game.getState());
    }


    @Test
    public void testGetGameTokenDTO_fromGame_toGameTokenDTO() {
        // create Game
        Game game = new Game();
        game.setToken("testToken");

        // MAP
        GameTokenDTO gameTokenDTO = DTOMapper.INSTANCE.convertEntityToGameTokenDTO(game);

        // check content
        assertEquals(gameTokenDTO.getToken(), game.getToken());
    }

}
