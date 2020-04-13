package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.GameStateEnum;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.exceptions.game.GameConflictException;
import ch.uzh.ifi.seal.soprafs20.exceptions.game.GameForbiddenException;
import ch.uzh.ifi.seal.soprafs20.exceptions.game.GameNotFoundException;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    public Game createLobby(Game game, User creatingUser){
        if (null != this.gameRepository.findByToken(game.getToken())) {
            throw new GameConflictException("This Game-Token already exists!");
        }

        //init and save game entity with token=input, mode=input, gamename=input, creator=rendered player from token, state=lobby, board=null
        Game newGame = new Game(new Player(creatingUser));

        //set creation date and time
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        newGame.setCreationTime(pattern.format(now));

        newGame.setToken(this.uniquePokemonNameGenerator.get())
                .setState(GameStateEnum.LOBBY)
                .setGameName(game.getGameName())
                .setMode(game.getMode());
        // This indian pattern is perfect for copy and pasting :)
        // TODO: find correct name for that

        newGame = this.gameRepository.save(newGame);

        //returns token so controller can send back to client
        return newGame;
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

        //if the removed player is the creator, close the lobby
        if((game.getCreator().getUser().getId().equals(user.getId())) && game.getState()==GameStateEnum.LOBBY){
            this.deleteGame(game);
        }
    }


    public void startGame(Integer npc, String gameToken){
        throw new NotYetImplementedException(); // Sprint 3
        //get game from gameToken

        //loop (from 0 to npc): render NPCs and add them to game

        //give each player a deck and set turn player = game.creator if not done already

        //change game.state to running so the polling clients see the game has started and start calling "get board"

    }

    public Game getGame(String gameToken){
        return this.gameRepository.findByToken(gameToken);
    }

    //TODO: helper methods like render player from user, maybe authentification?

    //********* Validators

    /**
     * Checks if game exists
     *
     * @param gameToken token of game to validate
     * @throws GameNotFoundException
     */
    public void validateGame(String gameToken) {
        Game game = this.gameRepository.findByToken(gameToken);
        if (null == game) {
            throw new GameNotFoundException("This game token doesn't exist (anymore)");
        }
    }

    /**
     * Validates if the user is the game creator
     *
     * @param gameToken token of the game to be validated
     * @param user User to be validated
     * @throws GameForbiddenException
     */
    public void validateCreator(String gameToken, User user) {
        Game game = this.gameRepository.findByToken(gameToken);
        if (!game.getCreator().getUser().getToken().equals(user.getToken())) {
            throw new GameForbiddenException("This user is not the game-creator.");
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

    public void deleteGame(Game game){
        //make players and creators null, so deleting the game wont delete all associated users
        game.resetCreator();
        game.resetPlayers();
        //deletes the game entity and all associated entities, watch out!
        long deletedGames = gameRepository.deleteByToken(game.getToken());

        //deleted Games is the number of deleted entities
        if (deletedGames==0){
            //throw new SopraServiceException("Can't delete game");
            throw new GameNotFoundException("Game to delete not found."); // Probably
        }
        else if (deletedGames!=0 && deletedGames!=1){
            throw new SopraServiceException("Unhandled Error when deleting game");
            // huh? then TODO: handle this
            // Should this ever happen?
        }

    }

}
