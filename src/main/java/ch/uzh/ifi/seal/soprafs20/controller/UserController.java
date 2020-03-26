package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPutDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to the user.
 * The controller will receive the request and delegate the execution to the UserService and finally return the result.
 */
@RestController
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }


    /**     #1      **/
    /** This request creates a new user, if the username is not already used **/
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserPostDTO createUser(@RequestBody UserPostDTO userPostDTO) {
        return userPostDTO;
    }


    /**     #2      **/
    /** This request returns a list with all users **/
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDTO getUserlist(@RequestBody UserGetDTO userGetDTO) {
        return userGetDTO;
    }


    /**     #3      **/
    /** This request returns a User object corresponding to the input id **/
    @GetMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String getUserById(@PathVariable String userId) {
        String test = "test";
        return test;
    }


    /**     #4      **/
    /** This request updates a User **/
    @PutMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void updateUser(@PathVariable String userId) {

    }


    /**     #5      **/
    /** This request logs in a user if the credentials are correct **/
    @PutMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String userLogin() {
        String test = "test";
        return test;
    }


    /**     #6      **/
    /** This request logs  out a user **/
    @PutMapping("/logout")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String userLogout() {
        String test = "test";
        return test;
    }


    /**     #7      **/
    /** This request creates a new lobby and returns the gameToken for sharing**/
    @PostMapping("/games")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String createLobby() {
        String test = "test";
        return test;
    }


    /**     #8      **/
    /** This request let's a user join or leave a lobby or kick another player from the lobby **/
    @PutMapping("/games/{gameToken}/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void lobbyOperation(@PathVariable String gameToken) {

    }


    /**     #10      **/
    /** This request starts a game from an existing lobby according to the gameToken **/
    @PutMapping("/games/{gameToken}/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void startGame(@PathVariable String gameToken) {

    }



    /**     #11     **/
    /** This request returns the current state of a running game **/
    @GetMapping("/games/{gameToken}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String getGameState(@PathVariable String gameToken) {
        String test = "test";
        return test;
    }


    /**     #12     **/
    /** This request returns the current state of a running game **/
    @PutMapping("/games/{gameToken}/categories")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void selectAttribute(@PathVariable String gameToken) {

    }


    /**     #13     **/
    /** This request lets the user, whose turn it is, select a category for battle **/
    @PutMapping("/games/{gameToken}/berries")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void berryUpgrade(@PathVariable String gameToken) {

    }

    /**     #14     **/
    /** This request returns a map with all Pokémon IDs and Sprite urls **/

    @GetMapping("/cards")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String getCardList() {
        String test = "test";
        return test;
    }


    /**     #15     **/
    /** This request returns a complete pkm-card **/

    @GetMapping("/cards/{pokemonId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String getCard(@PathVariable String pokemonId) {
        String test = "test";
        return test;
    }
}
