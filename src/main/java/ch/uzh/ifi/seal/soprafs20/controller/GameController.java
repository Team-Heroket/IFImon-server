package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.*;
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
    public String createLobby(@RequestBody GamePostDTO gamePostDTO, @RequestHeader("Token") String token) {
        //Authorize user
        userService.validateUser(token);

        //wrap user input as a game entity
        Game game = DTOMapper.INSTANCE.convertGamePostDTOToEntity(gamePostDTO);

        //creates a Lobby with the given gamemode and gamename
        String lobbyToken = gameService.createLobby(game);

        return lobbyToken;
    }


    /*     #8      */
    /** This request let's a user join or leave a lobby or kick another player from the lobby **/
    @PutMapping("/games/{gameToken}/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void lobbyOperation(@PathVariable String gameToken, @RequestBody GameTokenUserPutDTO gameTokenUserPutDTO, @RequestHeader("Token") String token ) {
        //authorize user from header
        userService.validateUser(token);

        //TODO: #0 check if gamerepository.findbyToken != null

        String username=gameTokenUserPutDTO.getUsername();
        int action=gameTokenUserPutDTO.getAction();

        if(action==0){
            //TODO: #1 check here that players are only able to add themselves and not other players: compare "header.token" with "username.token"
            gameService.addPlayer(username,gameToken);
        }
        else if(action==1){
            //TODO: #1 check here that players are only able to remove themselves and not other players: compare "header.token" with "username.token"
            gameService.removePlayer(username,gameToken);
        }
        else if(action==2){
            //TODO: #2 check here that only game creator can kick players: compare "header.token.username" with "gameToken.creator.username"
            gameService.removePlayer(username,gameToken);
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
        //TODO: #0 check if gamerepository.findbyToken != null
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
    public BoardGetDTO getGameState(@PathVariable String gameToken, @RequestHeader("Token") String token) {

        //TODO: #0 check if gamerepository.findbyToken != null

        //TODO: #3 maybe move this to a "boardservice"? imo not necessary though

        //authorize user from header
        //TODO: #4 this way every user with a valid token can access every gamestate, given he knows the gametoken.
        // Maybe only authorize users that are part of a certain game to get the board
        userService.validateUser(token);



        Board board = gameService.getBoard(gameToken);

        //convert to correct API format to return
        return DTOMapper.INSTANCE.convertBoardToBoardGetDTO(board);
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
