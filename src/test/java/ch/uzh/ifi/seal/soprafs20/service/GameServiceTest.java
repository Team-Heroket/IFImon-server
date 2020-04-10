package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.GameStateEnum;
import ch.uzh.ifi.seal.soprafs20.constant.Mode;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import junit.framework.AssertionFailedError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    private Game testGame;
    private User testUser;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        // given
        testGame = new Game(new Player(testUser));
        testGame.setToken("testGameToken");
        testGame.setState(GameStateEnum.LOBBY);
        testGame.setGameName("helloGame");
        testGame.setMode(Mode.SOCIAL);

        // when -> any object is being save in the userRepository -> return the dummy testUser
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);
    }

    @Test
    public void Test_createLobby() {
        // create lobby with setup inputs
        Game returnedGame = gameService.createLobby(testGame, testUser);
        assertEquals(returnedGame.getGameName(),testGame.getGameName());
        Mockito.verify(gameRepository, Mockito.times(1)).save(Mockito.any());
    }


    @Test
    public void Test_addUser() {
        // given
        User testUser = new User();
        testUser.setId(100L);
        testGame = new Game(new Player(testUser));
        testGame.setToken("testGameToken");
        testGame.setState(GameStateEnum.LOBBY);
        testGame.setGameName("helloGame");
        testGame.setMode(Mode.SOCIAL);

        User secondTestUser = new User();
        secondTestUser.setId(200L);
        // when
        Mockito.when(gameRepository.findByToken(Mockito.anyString())).thenReturn(testGame);

        // add user
        gameService.addPlayer("testToken",secondTestUser);
        assertEquals(testGame.getPlayers().get(1).getUser().getId(),200L);
    }

    @Test
    public void Test_removeUser() {
        // given
        User testUser = new User();
        testUser.setId(100L);
        testGame = new Game(new Player(testUser));
        testGame.setToken("testGameToken");
        testGame.setState(GameStateEnum.LOBBY);
        testGame.setGameName("helloGame");
        testGame.setMode(Mode.SOCIAL);
        User secondTestUser = new User();
        secondTestUser.setId(200L);

        // when
        Mockito.when(gameRepository.findByToken(Mockito.anyString())).thenReturn(testGame);

        // add player and remove player and then check if a player is saved at index 1
        gameService.addPlayer("testToken",secondTestUser);
        gameService.removePlayer("testToken",secondTestUser);
        assertThrows(java.lang.IndexOutOfBoundsException.class, () -> testGame.getPlayers().get(1));
    }

    //test start game
    //TODO TODO TODO


    @Test
    public void Test_getGame() {
        // given
        User testUser = new User();
        testUser.setId(100L);

        testGame = new Game(new Player(testUser));
        testGame.setToken("testGameToken");
        testGame.setState(GameStateEnum.LOBBY);
        testGame.setGameName("helloGame");
        testGame.setMode(Mode.SOCIAL);
        User secondTestUser = new User();
        secondTestUser.setId(200L);

        // when
        Mockito.when(gameRepository.findByToken(Mockito.anyString())).thenReturn(testGame);

        // then
        Game returnedGame = gameService.getGame("testToken");
        assertEquals(returnedGame.getToken(),testGame.getToken());
    }

    @Test
    public void Test_validateGame_invalidInputs() {
        // when
        Mockito.when(gameRepository.findByToken(Mockito.anyString())).thenReturn(null);

        // then
        assertThrows(SopraServiceException.class, () -> gameService.validateGame("testToken"));
    }

    @Test
    public void Test_validateCreator_invalidInputs() {
        // given a game with a different creator than testUser
        User differentCreator = new User();
        differentCreator.setToken("otherToken");
        differentCreator.setToken("testToken");
        User testUser = new User();
        testUser.setToken("thisToken");
        Game differentGame = new Game(new Player(differentCreator));


        // when
        Mockito.when(gameRepository.findByToken(Mockito.anyString())).thenReturn(differentGame);

        // then
        assertThrows(SopraServiceException.class, () -> gameService.validateCreator("testToken", testUser));
    }

    //test for getstate
    //TODO TODO TODO



}
