package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.constant.GameStateEnum;
import ch.uzh.ifi.seal.soprafs20.constant.*;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Statistics;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.*;
import ch.uzh.ifi.seal.soprafs20.rest.dto.*;
import ch.uzh.ifi.seal.soprafs20.service.*;

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


/** can't compile with these imports
 import jdk.internal.vm.annotation.ReservedStackAccess;
 import org.testng.annotations.AfterTest;
 import org.testng.annotations.Test;
 **/
import java.util.Collections;
import java.util.List;

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
        user.setOnline(true);
        user.setAvatarId(1);

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername");

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
        user.setOnline(true);

        List<User> allUsers = Collections.singletonList(user);

        // this mocks the UserService -> we define above what the userService should return when getUsers() is called
        given(userService.getUsers(Mockito.any())).willReturn(allUsers);

        // when
        MockHttpServletRequestBuilder getRequest = get("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Token", "Test");

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is(user.getUsername())))
                .andExpect(jsonPath("$[0].online", is(user.getOnline())));
    }



    @Test
    public void Test_getUserByToken() throws Exception {
        // given
        User user = new User();
        user.setUsername("firstname@lastname");
        user.setToken("3");
        user.setOnline(true);
        user.setCreationDate("today");
        user.setId(25L);
        user.setAvatarId(1);
        user.setStatistics(new Statistics());



        // this mocks the UserService -> we define above what the userService should return when getUsers() is called
        given(userService.getUser(Mockito.anyLong())).willReturn(user);

        // when
        MockHttpServletRequestBuilder getRequest = get("/users/3")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Token", "Test");

        // then
        mockMvc.perform(getRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username", is(user.getUsername())));
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
                .andExpect(status().isNoContent());
    }



    @Test
    public void Test_userLogin() throws Exception {
        // given
        User user = new User();
        user.setUsername("firstname@lastname");
        user.setPassword(("pw_uwu"));
        user.setToken("123");
        user.setId(2020L);


        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername("firstname@lastname");
        userPutDTO.setPassword("pw_uwu");
        userPutDTO.setAvatarId(1);
        userPutDTO.setToken("123");

        given(userService.logUserIn(Mockito.any())).willReturn(user);


        // when
        MockHttpServletRequestBuilder putRequest = put("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO));


        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2020)))
                .andExpect(jsonPath("$.token", is("123")));

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

// SPRINT 3


/**    @Test
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
**/


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