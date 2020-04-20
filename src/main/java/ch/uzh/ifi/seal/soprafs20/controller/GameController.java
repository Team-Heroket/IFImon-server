package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.constant.LobbyAction;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.rest.dto.*;
import ch.uzh.ifi.seal.soprafs20.constant.*;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.*;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

        LobbyAction action=gameTokenUserPutDTO.getAction();

        if(action==LobbyAction.JOIN){
            Long id=gameTokenUserPutDTO.getId();


            //here check if id==headertoken?
            userService.compareIdWithToken(id, token);
            User joiningUser=this.userService.getUserById(id);

            gameService.addPlayer(gameToken, joiningUser);
        }
        else if(action==LobbyAction.LEAVE){
            Long id=gameTokenUserPutDTO.getId();

            //here check if id==headertoken?
            userService.compareIdWithToken(id, token);
            User leavingUser=this.userService.getUserById(id);

            gameService.removePlayer(gameToken, leavingUser);
        }
        else if(action==LobbyAction.KICK){
            Long id=gameTokenUserPutDTO.getId();

            User kickingUser=this.userService.getUserByToken(token);

            //check if kicking user is creator?
            gameService.validateCreator(gameToken, kickingUser);

            User kickedUser=this.userService.getUserById(id);
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
        //validate gameToken and get object
        gameService.validateGame(gameToken);
        Game game=gameService.getGame(gameToken);

        //authorize user and get object
        userService.validateUser(token);
        User user=userService.getUserByToken(token);

        //check here that only game creator can start game
        gameService.validateCreator(gameToken,user);

        //gets amount of NPCs chosen by client and if none provided, sets it to 0
        Integer npc=gameTokenPutDTO.getNpc();
        if (npc==null){
            npc=0;
        }
        gameService.startGame(npc, game);
    }



    /*     #10     */
    /** This request returns the current state of a running game **/
    @GetMapping("/games/{gameToken}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO getGame(@PathVariable("gameToken") String gameToken, @RequestHeader("Token") String token) {

        //validate gameToken and get object
        gameService.validateGame(gameToken);
        Game game=gameService.getGame(gameToken);

        //authorize user and get object
        userService.validateUser(token);
        User user=userService.getUserByToken(token);

        //check if user part of game
        gameService.validatePlayer(gameToken,user);

        //calculate winner if state is running
        gameService.calculateWinner(game);

        //convert to correct API format to return
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
    }


    /*     #11     */
    /** This request lets the user, whose turn it is, select a category for battle **/
    @PutMapping("/games/{gameToken}/categories")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void selectCategory(@PathVariable String gameToken, @RequestBody CategoryDTO categoryDTO, @RequestHeader("Token") String token) {
        //try to catch invalid categories better?
        Category category=categoryDTO.getCategory();

        //validate gameToken and get object
        gameService.validateGame(gameToken);
        Game game=gameService.getGame(gameToken);

        //authorize user and get object
        userService.validateUser(token);
        User user=userService.getUserByToken(token);

        //check if user is part of game
        gameService.validatePlayer(gameToken,user);

        //check if user is turnPlayer
        gameService.validateTurnPlayer(gameToken,user);


        //TODO: check if "in time"

        //select category
        gameService.selectCategory(category, game);

    }


    /*     #12     */
    /** This request lets a card evolve **/
    @PutMapping("/games/{gameToken}/berries")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void berryUpgrade(@PathVariable String gameToken, @RequestBody BerryDTO berryDTO, @RequestHeader("Token") String token) {

        //get username and amount from body
        Long id=berryDTO.getId();
        Integer amount=berryDTO.getAmount();

        //validate gameToken and get object
        gameService.validateGame(gameToken);
        Game game=gameService.getGame(gameToken);

        //authorize user and get object
        userService.validateUser(token);
        User user=userService.getUserById(id);

        //check if user is part of game
        gameService.validatePlayer(gameToken,user);

        //get player associated with user
        Player player=gameService.getPlayerFromUser(game,user);

        //use berry
        gameService.useBerries(game, player, amount);

    }

    /** This request lets a card evolve **/
    @PutMapping("/games/{gameToken}/next")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void nextTurn(@PathVariable String gameToken, @RequestHeader("Token") String token) {
        //validate gameToken and get object
        gameService.validateGame(gameToken);
        Game game=gameService.getGame(gameToken);

        //authorize user and get object
        userService.validateUser(token);
        User user=userService.getUserByToken(token);

        //check if user is part of game
        gameService.validatePlayer(gameToken,user);

        //check if user is turnPlayer
        gameService.validateCreator(gameToken,user);

        gameService.nextTurn(game);


    }


}
