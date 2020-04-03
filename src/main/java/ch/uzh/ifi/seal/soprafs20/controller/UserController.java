package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.User;

import ch.uzh.ifi.seal.soprafs20.rest.dto.*;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.CardService;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    private final UserService userService;

    UserController(UserService userService, GameService gameService, CardService cardService) {
        this.userService = userService;
    }


    /*     #1      **/
    /** This request creates a new user, if the username is not already used **/
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void createUser(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // create user
        userService.createUser(userInput);
    }


    /*     #2      **/
    /** This request returns a list with all users **/
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public List<UserGetDTO> getUserList(@RequestHeader("Token") String token) {
        // fetch all users in the internal representation
        List<User> users = userService.getUsers(token);
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (User user : users) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        return userGetDTOs;
    }



    /*     #3      **/
    /** This request returns a User object corresponding to the input id **/
    @GetMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO getUserByToken(@PathVariable String userId, @RequestHeader("Token") String token) {

        //get user by token in the userservice
        User foundUser = userService.getUser(token);

        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(foundUser);
    }


    /*     #4      **/
    /** This request updates a User **/
    @PutMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void updateUser(@PathVariable String userId, @RequestBody UserPutDTO userPutDTO,  @RequestHeader("Token") String token) {
        User userInput = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);
        //check if authorized
        if(userService.compareHeaderWithUser(userInput, token)){
            //update user
            userService.updateUser(userInput);
        }
    }


    /*     #5      **/
    /** This request logs in a user if the credentials are correct **/
    @PutMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String userLogin(@RequestBody UserPutDTO userPutDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);

        User loggedInUser = userService.logUserIn(userInput);

        // convert internal representation of user back to API
        // TODO: Maybe write a DTO for that
        return "{\"Id\": " + loggedInUser.getId() + ", \"Token\": \"" + loggedInUser.getToken() + "\"}";
    }


    /*     #6      **/
    /** This request logs  out a user **/
    @PutMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void userLogout(@RequestHeader("Token") String token) {
        userService.logUserOut(token);
    }

}
