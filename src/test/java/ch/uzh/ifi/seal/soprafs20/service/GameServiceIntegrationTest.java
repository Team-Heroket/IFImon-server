package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.exceptions.game.*;
import ch.uzh.ifi.seal.soprafs20.exceptions.user.UserUnauthorizedException;
import ch.uzh.ifi.seal.soprafs20.repository.*;
import ch.uzh.ifi.seal.soprafs20.constant.*;

import ch.uzh.ifi.seal.soprafs20.service.gamestates.Finished;
import ch.uzh.ifi.seal.soprafs20.service.gamestates.Lobby;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the GameRessource REST resource.
 *
 * @see GameService
 */
@WebAppConfiguration
@SpringBootTest
public class GameServiceIntegrationTest {

    @Qualifier("gameRepository")
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameService gameService;

    @BeforeEach
    public void setup() {
        gameRepository.deleteAll();
    }

    @Test
    public void createLobby_validInputs_success() {
        // a testuser with essential parameteres assigned
        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setAvatarId(5);
        testUser.setPassword("passWordTest");
        testUser.setCreationDate("tday");
        // and a testgame
        Game game = new Game();
        game.setGameName("testGameName");

        // when
        Game createdGame = gameService.createLobby(game,testUser);

        // then
        assertEquals(createdGame.getCreator().getUser().getUsername(),testUser.getUsername());
        assertNotNull(createdGame.getCreationTime());
        assertNotNull(createdGame.getToken());
        assertEquals(createdGame.getGameName(),game.getGameName());
        assertEquals(createdGame.getState(),GameStateEnum.LOBBY);
    }

    @Test
    public void createLobby_gameAlreadyExists_throwException() {
        // a testuser with essential parameteres assigned
        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setAvatarId(5);
        testUser.setPassword("passWordTest");
        testUser.setCreationDate("tday");
        // and a testgame
        Game game = new Game();
        game.setGameName("testGameName");
        game.setCreationTime("tday");
        game.setState(GameStateEnum.LOBBY);
        game.setToken("gameToken");
        game.setMode(Mode.SOCIAL);

        // when
        gameRepository.save(game);

        // then
        assertThrows(GameConflictException.class, () -> gameService.createLobby(game,testUser));

    }

    @Test
    public void startGame() {
        // a testuser with essential parameteres assigned
        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setAvatarId(5);
        testUser.setPassword("passWordTest");
        testUser.setCreationDate("tday");
        testUser.setStatistics(new Statistics());
        // and a testgame
        Game testGame = new Game();
        testGame.setGameName("testGameName");
        testGame.setCreationTime("tday");

        // save game in repository
        testGame = gameService.createLobby(testGame,testUser);

        // then start game in lobby state
        Lobby lobby = new Lobby();
        lobby.startGame(testGame, 0, 2, 15000L, 1, this.gameRepository);
        int i=0;
        while(i<65){
            // check if thread is done
            if(testGame.getState()==GameStateEnum.RUNNING){
                break;
            }
            else{
                try{
                    Thread.sleep(1000L);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
            i += 1;
        }
        assertEquals(GameStateEnum.RUNNING,testGame.getState());
    }

    @Test
    public void startGame_rematchInFinishedState() {
        // a testuser with essential parameteres assigned
        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setAvatarId(5);
        testUser.setPassword("passWordTest");
        testUser.setCreationDate("tday");
        testUser.setStatistics(new Statistics());
        // and a testgame
        Game testGame = new Game();
        testGame.setGameName("testGameName");
        testGame.setCreationTime("tday");

        // save game in repository
        testGame = gameService.createLobby(testGame,testUser);
        testGame.setGeneration(1);
        testGame.setState(GameStateEnum.FINISHED);

        // then start game in lobby state
        Finished finished = new Finished();
        finished.startGame(testGame, 0, 2, 15000L, -1, this.gameRepository);
        int i=0;
        while(i<65){
            // check if thread is done
            if(testGame.getState()==GameStateEnum.RUNNING){
                break;
            }
            else{
                try{
                    Thread.sleep(1000L);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
            i += 1;
        }
        assertEquals(GameStateEnum.RUNNING,testGame.getState());
    }

}
