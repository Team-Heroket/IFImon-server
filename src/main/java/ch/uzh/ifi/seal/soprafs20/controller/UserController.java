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
    public void createUser(@RequestBody UserPostDTO userPostDTO, @RequestHeader("Token") String token) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // create user
        User createdUser = userService.createUser(userInput);
    }


    /**     #2      **/
    /** This request returns a list with all users **/
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public List<UserGetDTO> getUserlist(@RequestHeader("Token") String token) {
        // fetch all users in the internal representation
        List<User> users = userService.getUsers();
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (User user : users) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        return userGetDTOs;
    }



    /**     #3      **/
    /** This request returns a User object corresponding to the input id **/
    @GetMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDTO getUserById(@PathVariable String userId, @RequestHeader("Token") String token) {
        //convert string input to long
        long IDnumber = Long.parseLong(userId);

        //call userservice to get user by id
        User foundUser = userService.getUser(IDnumber);

        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(foundUser);
    }


    /**     #4      **/
    /** This request updates a User **/
    @PutMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void updateUser(@PathVariable String userId, @RequestBody UserPutDTO userPutDTO,  @RequestHeader("Token") String token) {
        //convert string input to long
        long IDnumber = Long.parseLong(userId);
        User userInput = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);

        //update user by id in the userservice
        userService.updateUser(IDnumber, userInput);
    }


    /**     #5      **/
    /** This request logs in a user if the credentials are correct **/
    @PutMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String userLogin(@RequestBody UserPutDTO userPutDTO, @RequestHeader("Token") String token) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);
        String tokenNr = userInput.getToken();

        //check credentials
        //code here

        //log in user
        userService.logUserIn(tokenNr);

        // convert internal representation of user back to API
        return tokenNr;
    }


    /**     #6      **/
    /** This request logs  out a user **/
    @PutMapping("/logout")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String userLogout(@RequestHeader("Token") String token) {
        String test = "test";
        return test;
    }


    /**     #7      **/
    /** This request creates a new lobby and returns the gameToken for sharing**/
    @PostMapping("/games")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String createLobby(@RequestHeader("Token") String token) {
        String test = "test";
        return test;
    }


    /**     #8      **/
    /** This request let's a user join or leave a lobby or kick another player from the lobby **/
    @PutMapping("/games/{gameToken}/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void lobbyOperation(@PathVariable String gameToken, @RequestHeader("Token") String token ) {

    }


    /**     #10      **/
    /** This request starts a game from an existing lobby according to the gameToken **/
    @PutMapping("/games/{gameToken}/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void startGame(@PathVariable String gameToken, @RequestHeader("Token") String token) {

    }



    /**     #11     **/
    /** This request returns the current state of a running game **/
    @GetMapping("/games/{gameToken}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String getGameState(@PathVariable String gameToken, @RequestHeader("Token") String token) {
        String test = "test";
        return test;
    }


    /**     #12     **/
    /** This request returns the current state of a running game **/
    @PutMapping("/games/{gameToken}/categories")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void selectAttribute(@PathVariable String gameToken, @RequestHeader("Token") String token) {

    }


    /**     #13     **/
    /** This request lets the user, whose turn it is, select a category for battle **/
    @PutMapping("/games/{gameToken}/berries")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void berryUpgrade(@PathVariable String gameToken, @RequestHeader("Token") String token) {

    }

    /**     #14     **/
    /** This request returns a map with all Pokémon IDs and Sprite urls **/

    @GetMapping("/cards")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String getCardList(@RequestHeader("Token") String token) {
        String test = "test";
        return test;
    }


    /**     #15     **/
    /** This request returns a complete pkm-card **/

    @GetMapping("/cards/{pokemonId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String getCard(@PathVariable String pokemonId, @RequestHeader("Token") String token) {
        String test = "test";
        return test;
    }
}
