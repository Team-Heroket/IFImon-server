package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.constant.LobbyAction;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.objects.Board;
import ch.uzh.ifi.seal.soprafs20.rest.dto.*;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.*;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class GameController {

    private final GameService gameService;
    private final UserService userService;

    GameController(UserService userService, GameService gameService) {
        this.gameService = gameService;
        this.userService = userService;
    }


    /*     #7      */
    /** This request creates a new lobby and returns the gameToken for sharing**/
    @PostMapping("/games")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameTokenDTO createLobby(@RequestBody GamePostDTO gamePostDTO, @RequestHeader("Token") String token) {
        //Authorize user
        userService.validateUser(token);
        // The Controller has to do this step, since you have the userService instance
        User creator = this.userService.getUserByToken(token);

        //wrap user input as a game entity
        Game game = DTOMapper.INSTANCE.convertGamePostDTOToEntity(gamePostDTO);

        //creates a Lobby with the given gamemode and gamename
        Game createdGame = gameService.createLobby(game, creator);

        return DTOMapper.INSTANCE.convertEntityToGameTokenDTO(createdGame);
    }


    /*     #8      */
    /** This request let's a user join or leave a lobby or kick another player from the lobby **/
    @PutMapping("/games/{gameToken}/players")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void lobbyOperation(@PathVariable String gameToken, @RequestBody GameTokenUserPutDTO gameTokenUserPutDTO, @RequestHeader("Token") String token ) {
        //authorize user from header & validate the game token
        userService.validateUser(token);
        gameService.validateGame(gameToken);

        //TODO: #0 check if gamerepository.findbyToken != null

        LobbyAction action=gameTokenUserPutDTO.getAction();

        if(action==LobbyAction.JOIN){
            //TODO: #1 check here that players are only able to add themselves and not other players: compare "header.token" with "username.token"
            String username=gameTokenUserPutDTO.getUsername();
            User joiningUser=this.userService.getUserByUsername(username);

            //here check if username==headertoken?
            userService.compareUsernameWithToken(username, token);

            gameService.addPlayer(gameToken, joiningUser);
        }
        else if(action==LobbyAction.LEAVE){
            //TODO: #1 check here that players are only able to remove themselves and not other players: compare "header.token" with "username.token
            String username=gameTokenUserPutDTO.getUsername();
            User leavingUser=this.userService.getUserByUsername(username);

            //here check if username==headertoken?
            userService.compareUsernameWithToken(username, token);

            gameService.removePlayer(gameToken, leavingUser);
        }
        else if(action==LobbyAction.KICK){
            //TODO: #2 check here that only game creator can kick players: compare "header.token.username" with "gameToken.creator.username
            String username=gameTokenUserPutDTO.getUsername();

            User kickingUser=this.userService.getUserByToken(token);

            //check if kicking user is creator?
            gameService.validateCreator(gameToken, kickingUser);

            User kickedUser=this.userService.getUserByUsername(username);
            gameService.removePlayer(gameToken, kickedUser);
        }
        else{
            throw new SopraServiceException("Bad request: Illegal action code");
        }
    }


    /*     #9      */
    /** This request starts a game from an existing lobby according to the gameToken **/
    @PutMapping("/games/{gameToken}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void startGame(@PathVariable String gameToken, @RequestBody GameTokenPutDTO gameTokenPutDTO, @RequestHeader("Token") String token) {
        userService.validateUser(token);
        gameService.validateGame(gameToken);
        //TODO: #2 check here that only game creator can start game: compare "header.token.username" with "gameToken.creator.username"

        //gets amount of NPCs chosen by client and if none provided, sets it to 0
        Integer npc=gameTokenPutDTO.getNpc();
        if (npc==null){
            npc=0;
        }
        gameService.startGame(npc, gameToken);
    }



    /*     #10     */
    /** This request returns the current state of a running game **/
    @GetMapping("/games/{gameToken}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO getGame(@PathVariable("gameToken") String gameToken, @RequestHeader("Token") String token) {

        //authorize user from header
        userService.validateUser(token);
        gameService.validateGame(gameToken);

        Game game = gameService.getGame(gameToken);

        //convert to correct API format to return
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
    }



//    SPRINT 3:
//
//
//
//    /*     #11     */
//    /** This request lets the user, whose turn it is, select a category for battle **/
//    @PutMapping("/games/{gameToken}/categories")
//    @ResponseStatus(HttpStatus.OK)
//    @ResponseBody
//    public void selectAttribute(@PathVariable String gameToken, Enum category, @RequestHeader("Token") String token) {
//
//        //call gameservice method for selecting a category
//        gameService.selectCategory(category, gameToken);
//
//    }
//
//
//    /*     #12     */
//    /** This request lets a card evolve **/
//    @PutMapping("/games/{gameToken}/berries")
//    @ResponseStatus(HttpStatus.OK)
//    @ResponseBody
//    public void berryUpgrade(@PathVariable String gameToken, Integer amount, String userName, @RequestHeader("Token") String token) {
//
//        //call gameservice method for berry usage
//        gameService.userBerries(amount, userName);
//    }


}
