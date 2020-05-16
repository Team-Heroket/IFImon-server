package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.constant.GameStateEnum;
import ch.uzh.ifi.seal.soprafs20.constant.*;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.*;
import ch.uzh.ifi.seal.soprafs20.rest.dto.*;
import ch.uzh.ifi.seal.soprafs20.service.*;

import ch.uzh.ifi.seal.soprafs20.service.gamestates.GameState;
import ch.uzh.ifi.seal.soprafs20.service.gamestates.Lobby;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(GameController.class)
public class GameControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Mock
        private UserRepository userRepository;

        @MockBean
        private UserService userService;

        @Mock
        private GameRepository gameRepository;

        @MockBean
        private GameService gameService;

        @MockBean
        private CardService cardService;


    @Test
    public void Test_createLobby() throws Exception {
        // given
        GamePostDTO gamePostDTO = new GamePostDTO();
        Mode mode = Mode.SOCIAL;
        gamePostDTO.setMode(mode);
        gamePostDTO.setGameName("testGameName");

        Game game = new Game();
        game.setState(GameStateEnum.LOBBY);
        game.setMode(Mode.SOCIAL);
        game.setGameName("testGameName");
        game.setToken("testGameToken");

        User creator = new User();

        // when
        given(userService.getUserByToken(Mockito.anyString())).willReturn(creator);
        given(userRepository.findByToken(Mockito.anyString())).willReturn(creator);
        given(gameService.createLobby(Mockito.any(),Mockito.any())).willReturn(game);

        // when
        MockHttpServletRequestBuilder postRequest = post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Token", "Test")
                .content(asJsonString(gamePostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token", is("testGameToken")));
    }


     @Test
     public void Test_lobbyOperation_Join() throws Exception {
        //given
         User joiningUser = new User();
         joiningUser.setToken("Test");
         Optional<User> op = Optional.of(joiningUser);
         Game game = new Game();
         game.setToken("1234");
         game.setState(GameStateEnum.LOBBY);
         GameTokenUserPutDTO gameTokenUserPutDTO = new GameTokenUserPutDTO();
         gameTokenUserPutDTO.setAction(LobbyAction.JOIN);
         gameTokenUserPutDTO.setId(100L);


         //when
         given(userRepository.findByToken(Mockito.anyString())).willReturn(joiningUser);
         given(userRepository.findById(Mockito.any())).willReturn(op);
         given(gameRepository.findByToken(Mockito.anyString())).willReturn(game);
         given(userService.getUserById(Mockito.any())).willReturn(joiningUser);


         // when/then -> do the request + validate the result
         MockHttpServletRequestBuilder putRequest = put("/games/1234/players")
                 .contentType(MediaType.APPLICATION_JSON)
                 .header("Token", "Test")
                 .content(asJsonString(gameTokenUserPutDTO));


         // then
         mockMvc.perform(putRequest)
                 .andExpect(status().isNoContent());
     }


    @Test
    public void Test_lobbyOperation_Leave() throws Exception {
        //given
        User joiningUser = new User();
        joiningUser.setToken("Test");
        Optional<User> op = Optional.of(joiningUser);
        Game game = new Game();
        game.setToken("1234");
        game.setState(GameStateEnum.LOBBY);
        GameTokenUserPutDTO gameTokenUserPutDTO = new GameTokenUserPutDTO();
        gameTokenUserPutDTO.setAction(LobbyAction.LEAVE);
        gameTokenUserPutDTO.setId(100L);


        //when
        given(userRepository.findByToken(Mockito.anyString())).willReturn(joiningUser);
        given(userRepository.findById(Mockito.any())).willReturn(op);
        given(gameRepository.findByToken(Mockito.anyString())).willReturn(game);
        given(userService.getUserById(Mockito.any())).willReturn(joiningUser);


        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/games/1234/players")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Token", "Test")
                .content(asJsonString(gameTokenUserPutDTO));


        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());
    }


    @Test
    public void Test_lobbyOperation_Kick() throws Exception {
        //given
        User joiningUser = new User();
        joiningUser.setToken("Test");
        Optional<User> op = Optional.of(joiningUser);
        Game game = new Game();
        game.setToken("1234");
        game.setState(GameStateEnum.LOBBY);
        GameTokenUserPutDTO gameTokenUserPutDTO = new GameTokenUserPutDTO();
        gameTokenUserPutDTO.setAction(LobbyAction.KICK);
        gameTokenUserPutDTO.setId(100L);


        //when
        given(userRepository.findByToken(Mockito.anyString())).willReturn(joiningUser);
        given(userRepository.findById(Mockito.any())).willReturn(op);
        given(gameRepository.findByToken(Mockito.anyString())).willReturn(game);
        given(userService.getUserById(Mockito.any())).willReturn(joiningUser);


        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/games/1234/players")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Token", "Test")
                .content(asJsonString(gameTokenUserPutDTO));


        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());
    }

    @Test
    public void Test_startGame() throws Exception {
        // given
        GameTokenPutDTO gameTokenPutDTO = new GameTokenPutDTO();
        gameTokenPutDTO.setNpc(10);

        User testUser = new User();
        testUser.setUsername("testUser");
        Game game = new Game(new Player(testUser));

        // when
        given(gameRepository.findByToken(Mockito.anyString())).willReturn(game);
        given(gameService.getGame(Mockito.anyString())).willReturn(game);
        given(userRepository.findByToken(Mockito.anyString())).willReturn(testUser);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/games/1234")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Token", "Test")
                .content(asJsonString(gameTokenPutDTO));

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isCreated());
    }

    @Test
    public void Test_getGameState() throws Exception {
        //given a testuser and a game
        User testUser = new User();
        testUser.setUsername("testUser");

        Game game = new Game(new Player(testUser));
        game.setState(GameStateEnum.LOBBY);
        game.setMode(Mode.SOCIAL);
        game.setGameName("testGameName");
        game.setToken("12345");
        game.setId(100L);
        game.setStartTime("now");
        game.setCreationTime("testDay");

        // when repositories are called for validation
        given(userRepository.findByToken(Mockito.anyString())).willReturn(testUser);
        given(gameRepository.findByToken(Mockito.anyString())).willReturn(game);
        // when service is called to get the game
        given(gameService.getGame(Mockito.anyString())).willReturn(game);


        // when we do a request use this request parameters
        MockHttpServletRequestBuilder getRequest = get("/games/12345")
                .header("Token", "Test");

        // then do request and check returned parameters
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameName").value(game.getGameName()))
                .andExpect(jsonPath("$.state").value("LOBBY"))
                .andExpect(jsonPath("$.mode").value("SOCIAL"))
                .andExpect(jsonPath("$.token").value(game.getToken()))
                .andExpect(jsonPath("$.startTime").value(game.getStartTime()))
                .andExpect(jsonPath("$.id").value(game.getId()))
                .andExpect(jsonPath("$.creationTime").value(game.getCreationTime()));
    }

    @Test
    public void Test_selectAttribute() throws Exception {
        //given
        CategoryDTO chosenCategory = new CategoryDTO();
        chosenCategory.setCategory(Category.HP);

        User testUser = new User();
        testUser.setUsername("testUser");
        Game game = new Game(new Player(testUser));
        game.setState(GameStateEnum.LOBBY);
        game.setMode(Mode.SOCIAL);
        game.setGameName("testGameName");
        game.setToken("1234");
        game.setId(100L);
        game.setCreationTime("testDay");

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/games/1234/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Token", "Test")
                .content(asJsonString(chosenCategory));

        // when
        given(userRepository.findByToken(Mockito.anyString())).willReturn(testUser);
        given(gameRepository.findByToken(Mockito.anyString())).willReturn(game);
        given(gameService.getGame(Mockito.anyString())).willReturn(game);

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void Test_userBerries() throws Exception {
        //given
        BerryDTO berryAmount = new BerryDTO();
        berryAmount.setAmount(0);

        User testUser = new User();
        testUser.setUsername("testUser");
        Game game = new Game(new Player(testUser));
        game.setState(GameStateEnum.LOBBY);
        game.setMode(Mode.SOCIAL);
        game.setGameName("testGameName");
        game.setToken("1234");
        game.setId(100L);
        game.setCreationTime("testDay");

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/games/1234/berries")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Token", "Test")
                .content(asJsonString(berryAmount));

        // when
        given(userRepository.findByToken(Mockito.anyString())).willReturn(testUser);
        given(gameRepository.findByToken(Mockito.anyString())).willReturn(game);
        given(gameService.getGame(Mockito.anyString())).willReturn(game);

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void Test_nextTurn() throws Exception {
        //given
        User testUser = new User();
        testUser.setUsername("testUser");
        Game game = new Game(new Player(testUser));
        game.setState(GameStateEnum.LOBBY);
        game.setMode(Mode.SOCIAL);
        game.setGameName("testGameName");
        game.setToken("1234");
        game.setId(100L);
        game.setCreationTime("testDay");

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/games/1234/next")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Token", "Test");

        // when
        given(userRepository.findByToken(Mockito.anyString())).willReturn(testUser);
        given(gameRepository.findByToken(Mockito.anyString())).willReturn(game);
        given(gameService.getGame(Mockito.anyString())).willReturn(game);

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void Test_emote() throws Exception {
        //given
        EmoteDTO emoteDTO = new EmoteDTO();
        emoteDTO.setEmote(3);

        User testUser = new User();
        testUser.setUsername("testUser");
        Game game = new Game(new Player(testUser));
        game.setState(GameStateEnum.FINISHED);
        game.setMode(Mode.SOCIAL);
        game.setGameName("testGameName");
        game.setToken("1234");
        game.setId(100L);
        game.setCreationTime("testDay");

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/games/1234/emotes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(emoteDTO))
                .header("Token", "Test");

        // when
        given(userRepository.findByToken(Mockito.anyString())).willReturn(testUser);
        given(gameRepository.findByToken(Mockito.anyString())).willReturn(game);
        given(gameService.getGame(Mockito.anyString())).willReturn(game);

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isOk());
    }


 private String asJsonString(final Object object) {
     try {
         return new ObjectMapper().writeValueAsString(object);
     }
     catch (JsonProcessingException e) {
         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The request body could not be created.%s", e.toString()));
     }
 }

 }