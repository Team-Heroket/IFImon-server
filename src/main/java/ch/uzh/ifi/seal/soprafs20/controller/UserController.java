package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.entity.Tables;

import ch.uzh.ifi.seal.soprafs20.rest.dto.*;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.CardService;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


@RestController
public class UserController {

    private final UserService userService;

    private final GameService gameService;

    private final CardService cardService;


    UserController(UserService userService, GameService gameService, CardService cardService) {
        this.userService = userService;
        this.gameService = gameService;
        this.cardService = cardService;
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
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO getUserByToken(@PathVariable String userId, @RequestHeader("Token") String token) {

        //call userservice to get user by token
        User foundUser = userService.getUser(userId);

        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(foundUser);
    }


    /**     #4      **/
    /** This request updates a User **/
    @PutMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void updateUser(@PathVariable String userId, @RequestBody UserPutDTO userPutDTO,  @RequestHeader("Token") String token) {
        User userInput = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);

        //update user by id in the userservice
        userService.updateUser(userId, userInput);
    }


    /**     #5      **/
    /** This request logs in a user if the credentials are correct **/
    @PutMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String userLogin(@RequestBody UserPutDTO userPutDTO, @RequestHeader("Token") String token) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);
        String tokenNr = userInput.getToken();

        //check credentials
        //CODE HERE

        //log in user
        userService.logUserIn(tokenNr, userInput);

        // convert internal representation of user back to API
        return tokenNr;
    }


    /**     #6      **/
    /** This request logs  out a user **/
    @PutMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void userLogout(@RequestHeader("Token") String token) {
        userService.logUserOut(token);
    }


    /**     #7      **/
    /** This request creates a new lobby and returns the gameToken for sharing**/
    @PostMapping("/games")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String createLobby(String gameName, Enum mode, @RequestHeader("Token") String token) {

        //creates a game with the given gamemode and gamename
        String newGameToken = gameService.createGame(gameName,mode);

        return newGameToken;
    }


    /**     #8      **/
    /** This request let's a user join or leave a lobby or kick another player from the lobby **/
    @PutMapping("/games/{gameToken}/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void lobbyOperation(@PathVariable String gameToken, String userName, Integer action, @RequestHeader("Token") String token ) {

        gameService.addUser(userName,gameToken);

        /**
        long value = action;

        if(value==0){
            gameService.addUser(userName,gameToken);
        }
        else if(value==1){
            // for leaving, the own username was inputted
            gameService.removeUser(userName,gameToken);
        }
        else if(value==2){
            gameService.addUser(userName,gameToken);
        }**/
    }


    /**     #9      **/
    /** This request starts a game from an existing lobby according to the gameToken **/
    @PutMapping("/games/{gameToken}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void startGame(@PathVariable String gameToken, Integer amount, @RequestHeader("Token") String token) {
        //sets amount of npcs and starts the game
        gameService.setNPCAmount(amount, gameToken);
    }



    /**     #10     **/
    /** This request returns the current state of a running game **/
    @GetMapping("/games/{gameToken}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public TableGetDTO getGameState(@PathVariable String gameToken, @RequestHeader("Token") String token) {

        //gets gamestate as a table object
        Tables table = gameService.getGame(gameToken);

        //convert to correct API format to return
        return DTOMapper.INSTANCE.convertEntityToTableGetDTO(table);
    }


    /**     #11     **/
    /** This request lets the user, whose turn it is, select a category for battle **/
    @PutMapping("/games/{gameToken}/categories")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void selectAttribute(@PathVariable String gameToken, Enum category, @RequestHeader("Token") String token) {

        //call gameservice method for selecting a category
        gameService.selectCategory(category, gameToken);

    }


    /**     #12     **/
    /** This request lets a card evolve **/
    @PutMapping("/games/{gameToken}/berries")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void berryUpgrade(@PathVariable String gameToken, Integer amount, String userName, @RequestHeader("Token") String token) {

        //call gameservice method for berry usage
        gameService.userBerries(amount, userName);
    }

    /**     #13     **/
    /** This request returns a map with all Pokémon IDs and Sprite urls **/

    @GetMapping("/cards")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Map<Long,String> getCardList(@RequestHeader("Token") String token) {

        //get a map with all ids and urls of the cards
        Map<Long,String> map = cardService.getCards();

        return map;
    }


    /**     #14     **/
    /** This request returns a complete pkm-card **/

    @GetMapping("/cards/{pokemonId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public CardGetDTO getCard(@PathVariable String pokemonId, @RequestHeader("Token") String token) {

        //convert pathvaraible to long
        long cardId = Long.parseLong(pokemonId);

        //get card from service
        Card card = cardService.getCard(cardId);

        return DTOMapper.INSTANCE.convertEntityToCardGetDTO(card);
    }
}
