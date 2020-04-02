package ch.uzh.ifi.seal.soprafs20.controller;
/**
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPutDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserGetDTO;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
**/

import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPutDTO;
import ch.uzh.ifi.seal.soprafs20.service.CardService;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;


/** can't compile with these imports
 import jdk.internal.vm.annotation.ReservedStackAccess;
 import org.testng.annotations.AfterTest;
 import org.testng.annotations.Test;
 **/
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST request without actually sending them over the network.
 * This tests if the UserController works.
 */

enum mode{
    Social(0), Quick(1), Single(2);

    private int mode;

    mode(int Stat){
        this.mode = Stat;
    }

    public int getMode(){
        return mode;
    }
}

enum category{
    HP(0), ATK(1), Wheight(2);

    private int mode;

    category(int Stat){
        this.mode = Stat;
    }

    public int getCategory(){
        return mode;
    }
}

@WebMvcTest(UserController.class)
public class UserControllerTest {

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
    public void Test_createUser() throws Exception {
        // given
        User user = new User();
        user.setUsername("testUsername");
        user.setStatus(UserStatus.ONLINE);
        String string = "test";

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername");

        given(userService.createUser(Mockito.any())).willReturn(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest)
            .andExpect(status().isCreated());
    }

    @Test
    public void Test_getUserList() throws Exception {
        // given
        User user = new User();
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);

        List<User> allUsers = Collections.singletonList(user);

        // this mocks the UserService -> we define above what the userService should return when getUsers() is called
        given(userService.getUsers()).willReturn(allUsers);

        // when
        MockHttpServletRequestBuilder getRequest = get("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Token", "Test");

        // then
        mockMvc.perform(getRequest).andExpect(status().isCreated())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is(user.getUsername())))
                .andExpect(jsonPath("$[0].status", is(user.getStatus().toString())));
    }

    @Test
    public void Test_getUserByToken() throws Exception {
        // given
        User user = new User();
        user.setUsername("firstname@lastname");
        user.setToken("3");
        user.setStatus(UserStatus.ONLINE);

        // this mocks the UserService -> we define above what the userService should return when getUsers() is called
        given(userService.getUser(Mockito.any())).willReturn(user);

        // when
        MockHttpServletRequestBuilder getRequest = get("/users/3")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Token", "Test");

        // then
        mockMvc.perform(getRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username", is(user.getUsername())))
            .andExpect(jsonPath("$.token", is(user.getToken())));

    }

    @Test
    public void Test_updateUser() throws Exception {
        // given
        User user = new User();
        user.setUsername("firstname@lastname");
        user.setToken("123");

        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername("firstname@lastname");

        // when
        MockHttpServletRequestBuilder putRequest = put("/users/3")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Token", "Test")
                .content(asJsonString(userPutDTO));

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void Test_userLogin() throws Exception {
        // given
        User user = new User();
        user.setUsername("firstname@lastname");
        user.setToken("123");

        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername("firstname@lastname");
        userPutDTO.setPassword("pw");
        userPutDTO.setToken("123");

        // when
        MockHttpServletRequestBuilder putRequest = put("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO));

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("123"));




    }

    @Test
    public void Test_userLogout() throws Exception {

        // when
        MockHttpServletRequestBuilder putRequest = put("/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Token", "Test");
        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void Test_createLobby() throws Exception {
        // given
        String gameToken = "newGameToken";
        String newGamename = "newGame";
        mode gameMode = mode.Quick;


        given(gameService.createGame(Mockito.any(),Mockito.any())).willReturn(gameToken);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Token", "Test")
                .content(newGamename)
                .content(String.valueOf(gameMode));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value("newGameToken"));
    }

    @Test
    public void Test_lobbyOperation() throws Exception {
        // given
        long action= 2;
        String userName = "newUsername";

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/games/1234/users")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Token", "Test")
                .content(userName)
                .content(String.valueOf(action));

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void Test_startGame() throws Exception {
        // given
        int action= 2;

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/games/1234")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Token", "Test")
                .content(String.valueOf(action));

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isOk());
    }


    @Test
    public void Test_getGameState() throws Exception {

        //given
        Tables table = new Tables();
        table.setTimer(123);

        // this mocks the UserService -> we define above what the gameService should return when getUsers() is called
        given(gameService.getGame(Mockito.any())).willReturn(table);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/games/1234")
                .header("Token", "Test");

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timer").value(123));
    }



    @Test
    public void Test_selectAttribute() throws Exception {
        //given
        category chosenCategory = category.HP;


        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/games/1234/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Token", "Test")
                .content(String.valueOf(chosenCategory));

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void Test_berryUpgrade() throws Exception {
        //given
        int amount = 3;
        String userName = "userName";

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/games/1234/berries")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Token", "Test")
                .content(String.valueOf(amount))
                .content(userName);

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void Test_getCards() throws Exception {

        //given
        Map<Long,String> testMap = new HashMap<Long,String>();
        testMap.put(101L,"testMon");

        // this mocks the UserService -> we define above what the gameService should return when getUsers() is called
        given(cardService.getCards()).willReturn(testMap);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/cards")
                .header("Token", "Test");

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk());
        //how to test returned HashMap
    }

    @Test
    public void Test_getCard() throws Exception {

        //given
        Card card = new Card();
        card.setName("firstCard");

        // this mocks the UserService -> we define above what the gameService should return when getUsers() is called
        given(cardService.getCard(1)).willReturn(card);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/cards/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Token", "Test");

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("firstCard"));
    }



    /**
     * Helper Method to convert userPostDTO into a JSON string such that the input can be processed
     * Input will look like this: {"name": "Test User", "username": "testUsername"}
     * @param object
     * @return string
     */

    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The request body could not be created.%s", e.toString()));
        }
    }
}