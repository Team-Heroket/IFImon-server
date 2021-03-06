package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.GameStateEnum;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.entity.Deck;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.exceptions.game.GameConflictException;
import ch.uzh.ifi.seal.soprafs20.exceptions.game.GameForbiddenException;
import ch.uzh.ifi.seal.soprafs20.exceptions.game.GameNotFoundException;
import ch.uzh.ifi.seal.soprafs20.objects.*;
import ch.uzh.ifi.seal.soprafs20.repository.*;
import ch.uzh.ifi.seal.soprafs20.repository.PokeAPICacheRepository;
import ch.uzh.ifi.seal.soprafs20.service.gamestates.*;
import ch.uzh.ifi.seal.soprafs20.constant.*;

import org.hibernate.cfg.NotYetImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final UniquePokemonNameGenerator uniquePokemonNameGenerator;
    private final PokeAPICacheRepository pokeAPICacheRepository;

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository, @Qualifier("pokeAPICacheRepository") PokeAPICacheRepository pokeAPICacheRepository, @Qualifier("userRepository") UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.uniquePokemonNameGenerator = new UniquePokemonNameGenerator();
        this.pokeAPICacheRepository = pokeAPICacheRepository;
        PokeAPICacheService.setRepository(pokeAPICacheRepository);
    }

    /**
     * Creates a new game and put its into lobby state
     *
     * @param game
     * @param creatingUser
     * @return
     */
    public Game createLobby(Game game, User creatingUser){

        log.debug(String.format("Create Lobby request send by %s.", creatingUser.getUsername()));

        // throw exception if a game with same token already exists
        if (null != this.gameRepository.findByToken(game.getToken())) {
            throw new GameConflictException("This Game-Token already exists!");
        }

        //init and save game entity with token=input, mode=input, gamename=input, creator=rendered player from token, state=lobby, board=null
        Game newGame = new Game(new Player(creatingUser));

        //set creation date and time
        //DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //LocalDateTime now = LocalDateTime.now();
        newGame.setCreationTime(String.valueOf(System.currentTimeMillis()));

        newGame.setToken(this.uniquePokemonNameGenerator.get())
                .setState(GameStateEnum.LOBBY)
                .setGameName(game.getGameName())
                .setMode(game.getMode());

        newGame = this.gameRepository.save(newGame);

        log.debug(String.format("Lobby created. Token: %s.", newGame.getToken()));

        //returns token so controller can send back to client
        return newGame;
    }

    public void addPlayer(String gameToken, User user){
        // Hmm, vielleicht wäre es trotzdem besser, wenn die validation auch hier gemacht wird...
        Game game = this.gameRepository.findByToken(gameToken);
        GameState state = this.getState(game);

        // This handles the addPlayer depending on the state the game is in.
        state.addPlayer(game, user);
        this.gameRepository.save(game);
    }

    public void removePlayer(String gameToken, User user){
        //get game corresponding to gameToken
        Game game = this.gameRepository.findByToken(gameToken);
        GameState state = this.getState(game);

        //remove player from this game.players[] with the provided username, if he exists
        state.removePlayer(game, user);

        //if the removed player is the creator, close the lobby
        if(null == game.getCreator() || game.getCreator().getUser().getId().equals(user.getId())){
            this.deleteGame(game);
            log.debug(String.format("Game deleted. Token: %s.", game.getToken()));
        }
        else{
            this.gameRepository.save(game);
            this.gameRepository.flush();
        }

    }


    public void startGame(Integer npc, Game game, int deckSize, int generation){
        // get the current state of the game
        GameState state = this.getState(game);

        // try to start the game in the current state
        state.startGame(game, npc, deckSize, 15000L, generation, this.gameRepository);

        // save changes
        this.gameRepository.save(game);
    }

    public void selectCategory(Category category, Game game){
        // get the current state of the game
        GameState state = this.getState(game);

        // selects category and calculates winner
        state.selectCategory(game, category);

        // save changes
        this.gameRepository.save(game);
    }

    public void useBerries(Game game, Player player, Integer amount){
        // get the current state of the game
        GameState state = this.getState(game);

        // uses berries if possible
        state.useBerries(game, amount, player);

        // save changes
        this.gameRepository.save(game);
    }

    public void nextTurn(Game game){
        // get the current state of the game
        GameState state = this.getState(game);

        // changes turn of game to next turn in the current state
        state.nextTurn(game);

        // save changes
        this.gameRepository.save(game);

    }

    public void putEmote(Game game, User user, Integer emote){
        // get the current state of the game
        GameState state = this.getState(game);

        // puts emote in the current state of the game
        state.putEmote(getPlayerFromUser(game,user), emote);

        // save changes
        this.gameRepository.save(game);

    }

    // getter from repository
    public Game getGame(String gameToken){
        return this.gameRepository.findByToken(gameToken);
    }

    //********* Validators

    /**
     * Checks if game exists
     *
     * @param gameToken token of game to validate
     * @throws GameNotFoundException
     */
    public void validateGame(String gameToken) {
        // checks if a game with given token exists
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
        // checks whether given user is the creator of the game with given gametoken
        Game game = this.gameRepository.findByToken(gameToken);

        if (!game.getCreator().getUser().getToken().equals(user.getToken())) {
            throw new GameForbiddenException("This user is not the game-creator.");
        }
    }

    /**
     * Validates if the user is the turn player
     *
     * @param gameToken token of the game to be validated
     * @param user User to be validated
     * @throws GameForbiddenException
     */
    public void validateTurnPlayer(String gameToken, User user) {
        // checks whether given user is the turnplayer of the game with given gametoken

        Game game = this.gameRepository.findByToken(gameToken);
        if (!game.getTurnPlayer().getUser().getToken().equals(user.getToken())) {
            throw new GameForbiddenException("This user is not the turn player.");
        }
    }

    /**
     * Validates if the user is part of game as player
     *
     * @param gameToken token of the game to be validated
     * @param user User to be validated
     * @throws GameForbiddenException
     */
    public void validatePlayer(String gameToken, User user) {
        // get game by token
        Game game = this.gameRepository.findByToken(gameToken);

        // check if given user is involved in the given game
        for (Player validPlayer : game.getPlayers()){
            if (validPlayer.getUser().getId().equals(user.getId())){
                return;
            }
        }
        throw new GameForbiddenException("This user is not part of this game (anymore).");
    }

    /**
     * Returns player associated with user

     */
    public Player getPlayerFromUser(Game game, User user) {
        // returns the player corresponding to a given user and a given game
        for(Player player : game.getPlayers()){
            if(user.getId().equals(player.getUser().getId())){
                return player;
            }
        }
        throw new GameForbiddenException("This user is not associated with a player");
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
        //make player entities in game entity null, so deleting the game wont delete all associated users
        game.resetCreator();
        game.resetPlayers();
        game.resetTurnPlayer();
        game.resetWinners();

        //deletes the game entity and all associated entities, watch out!
        long deletedGames = gameRepository.deleteByToken(game.getToken());

        //deleted Games is the number of deleted entities
        if (deletedGames==0){
            //throw new SopraServiceException("Can't delete game");
            throw new GameNotFoundException("Game to delete not found."); // Probably
        }
        else if (deletedGames!=1){
            throw new SopraServiceException("Unhandled Error when deleting game");
        }

    }

}
