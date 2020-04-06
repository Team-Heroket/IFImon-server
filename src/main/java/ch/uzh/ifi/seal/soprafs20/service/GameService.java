package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.objects.*;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(GameService.class);

    //TODO: maybe need to add user repository to render players and later update statistics etc.
    private final GameRepository gameRepository;

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public String createLobby(Game game){
        //randomly generate token

        //get user from token and create a player entity/object from it

        //init and save game entity with token=input, mode=input, gamename=input, creator=rendered player from token, state=lobby, board=null

        //returns token so controller can send back to client
        return "qwer";
    }

    public void addPlayer(String username, String gameToken){
        //get user corresponding to username

        //init player based on this user

        //append player to game.players[] of game corresponding to gameToken
    }

    public void removePlayer(String username, String gameToken){
        //get game corresponding to gameToken

        //remove player from this game.players[] with the provided username, if he exists
    }


    public void startGame(Integer npc, String gameToken){
        //get game from gameToken

        //loop (from 0 to npc): render NPCs and add them to game

        //give each player a deck and set turn player = game.creator if not done already

        //change game.state to running so the polling clients see the game has started and start calling "get board"

    }

    public Board getBoard(String gameToken){
        //get game from gameToken

        //get board of game

        //return board
        return new Board();
    }

    //TODO: helper methods like render player from user, maybe authentification?


}
