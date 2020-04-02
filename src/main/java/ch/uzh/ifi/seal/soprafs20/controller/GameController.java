package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.Tables;
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
public class GameController {

    private final GameService gameService;

    GameController(UserService userService, GameService gameService, CardService cardService) {
        this.gameService = gameService;
    }


    /*     #7      */
    /** This request creates a new lobby and returns the gameToken for sharing**/
    @PostMapping("/games")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String createLobby(String gameName, Enum mode, @RequestHeader("Token") String token) {

        //creates a game with the given gamemode and gamename
        String newGameToken = gameService.createGame(gameName,mode);

        return newGameToken;
    }


    /*     #8      */
    /** This request let's a user join or leave a lobby or kick another player from the lobby **/
    @PutMapping("/games/{gameToken}/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void lobbyOperation(@PathVariable String gameToken, String userName, Integer action, @RequestHeader("Token") String token ) {

        gameService.addUser(userName,gameToken);

        /*
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


    /*     #9      */
    /** This request starts a game from an existing lobby according to the gameToken **/
    @PutMapping("/games/{gameToken}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void startGame(@PathVariable String gameToken, Integer amount, @RequestHeader("Token") String token) {
        //sets amount of npcs and starts the game
        gameService.setNPCAmount(amount, gameToken);
    }



    /*     #10     */
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


    /*     #11     */
    /** This request lets the user, whose turn it is, select a category for battle **/
    @PutMapping("/games/{gameToken}/categories")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void selectAttribute(@PathVariable String gameToken, Enum category, @RequestHeader("Token") String token) {

        //call gameservice method for selecting a category
        gameService.selectCategory(category, gameToken);

    }


    /*     #12     */
    /** This request lets a card evolve **/
    @PutMapping("/games/{gameToken}/berries")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void berryUpgrade(@PathVariable String gameToken, Integer amount, String userName, @RequestHeader("Token") String token) {

        //call gameservice method for berry usage
        gameService.userBerries(amount, userName);
    }
}
