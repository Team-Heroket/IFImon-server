package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.GameStateEnum;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.objects.*;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.service.gamestates.*;

import org.hibernate.cfg.NotYetImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(GameService.class);

    // Repositories
    //TODO: maybe need to add user repository to render players and later update statistics etc.
    private final GameRepository gameRepository;
    private final UniquePokemonNameGenerator uniquePokemonNameGenerator;

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository) {
        this.gameRepository = gameRepository;
        this.uniquePokemonNameGenerator = new UniquePokemonNameGenerator();
    }

    /**
     * Creates a new game and put its into lobby state
     *
     * @param game
     * @param creatingUser
     * @return
     */
    public String createLobby(Game game, User creatingUser){
        if (null != this.gameRepository.findByToken(game.getToken())) {
            throw new SopraServiceException("This Game-Token already exists!");
        }

        //init and save game entity with token=input, mode=input, gamename=input, creator=rendered player from token, state=lobby, board=null
        Game newGame = new Game(new Player(creatingUser));
        newGame.setToken(this.uniquePokemonNameGenerator.get())
                .setState(GameStateEnum.LOBBY)
                .setGameName(game.getGameName())
                .setMode(game.getMode());
        // This indian pattern is perfect for copy and pasting :)
        // TODO: find correct name for that

        newGame = this.gameRepository.save(newGame);

        //returns token so controller can send back to client
        return newGame.getToken();
    }

    public void addPlayer(String gameToken, User user){
        // Hmm, vielleicht wäre es trotzdem besser, wenn die validation auch hier gemacht wird...
        Game game = this.gameRepository.findByToken(gameToken);
        GameState state = this.getState(game);

        // This handles the addPlayer depending on the state the game is in.
        state.addPlayer(game, user);
    }

    public void removePlayer(String gameToken, User user){
        //get game corresponding to gameToken
        Game game = this.gameRepository.findByToken(gameToken);
        GameState state = this.getState(game);

        //remove player from this game.players[] with the provided username, if he exists
        state.removePlayer(game, user);
    }


    public void startGame(Integer npc, String gameToken){
        throw new NotYetImplementedException(); // Sprint 3
        //get game from gameToken

        //loop (from 0 to npc): render NPCs and add them to game

        //give each player a deck and set turn player = game.creator if not done already

        //change game.state to running so the polling clients see the game has started and start calling "get board"

    }

    public Board getBoard(String gameToken){
        throw new NotYetImplementedException(); // Sprint 3

        //get game from gameToken

        //get board of game

        //return board
        //return new Board();
    }

    //TODO: helper methods like render player from user, maybe authentification?

    //********* Validators

    /**
     * Checks if game exists
     *
     * @param inputGame to validate
     * @throws SopraServiceException
     */
    public void validateGame(Game inputGame) {
        Game game = this.gameRepository.findByToken(inputGame.getToken());
        if (null == game) {
            throw new SopraServiceException("This game token doesn't exist (anymore)");
        }
    }


    //********* Helpers

    /**
     * Reads the enum of the Game-Entity and returns correct State
     *
     * Lets call this 'cripple State-Pattern'
     * By Dominik
     *
     * @param game loaded Game-Entity
     * @return the state the game is in
     */
    private GameState getState(Game game) {
        /*
            Anti-pattern detected!
            But how can I squeeze the state-pattern into the MVC-Pattern?

            Problems:
                - We have states, which contradicts REST
                    - But we can't leave the state to the client, since they could fake it
                - We can't save an Object (precisely methods) into the DB

            In this way I can at least use the Strategy pattern...

            Note by Dominik
         */
        switch (game.getState()) {
            case LOBBY:
                return new Lobby();
            case RUNNING:
                return new Running();
            case FINISHED:
                return new Finished();
            default:
                throw new RuntimeException("Did you insert a new state?");
        }
    }

}
