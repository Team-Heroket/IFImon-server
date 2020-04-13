package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import java.util.*;
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
        //no need for authorization

        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // create user
        userService.createUser(userInput);
    }


    /*     #2      **/
    /** This request returns a list with all users **/
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getUserList(@RequestHeader("Token") String token) {
        //authorize user
        userService.validateUser(token);

        // fetch all users in the internal representation
        List<User> users = userService.getUsers(token);

        //sort users by rating in descending order but only if list is not empty (NPE)
        if(!users.isEmpty()) {
            Collections.sort(users);
        }

        // convert each user to the API representation
        List<UserGetDTO> userGetDTOs = new ArrayList<>();
        for (User user : users) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        //return list
        return userGetDTOs;
    }



    /*     #3      **/
    /** This request returns a User object corresponding to the input id **/
    @GetMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO getUserByToken(@PathVariable("userId") long userId, @RequestHeader("Token") String token) {
        //authorize user
        userService.validateUser(token);

        //get user by token in the userservice
        User foundUser = userService.getUser(userId);

        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(foundUser);
    }


    /*     #4      **/
    /** This request updates a User **/
    @PutMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void updateUser(@PathVariable("userId") long userId, @RequestBody UserPutDTO userPutDTO,  @RequestHeader("Token") String token) {
        // Takes changes from Body
        User userInput = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);
        // Merges userId from path into the user
        userInput.setId(userId);

        //check if authorized to change this user
        userService.compareIdWithToken(userInput.getId(), token);

        //update user
        userService.updateUser(userInput);
    }


    /*     #5      **/
    /** This request logs in a user if the credentials are correct **/
    @PutMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserLoginDTO userLogin(@RequestBody UserPutDTO userPutDTO) {
        //no need for authorization of token

        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);

        //log in user
        User loggedInUser = userService.logUserIn(userInput);

        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToUserLoginDTO(loggedInUser);
    }


    /*     #6      **/
    /** This request logs  out a user **/
    @PutMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void userLogout(@RequestHeader("Token") String token) {
        //check if token represents a valid user
        userService.validateUser(token);

        //log user out
        userService.logUserOut(token);
    }

}
