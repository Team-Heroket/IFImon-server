package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.Category;
import ch.uzh.ifi.seal.soprafs20.constant.GameStateEnum;
import ch.uzh.ifi.seal.soprafs20.constant.Mode;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.exceptions.game.GameBadRequestException;
import ch.uzh.ifi.seal.soprafs20.exceptions.game.GameForbiddenException;
import ch.uzh.ifi.seal.soprafs20.exceptions.game.GameNotFoundException;
import ch.uzh.ifi.seal.soprafs20.objects.UniqueBaseEvolutionPokemonGenerator;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PokeAPICacheRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs20.service.gamestates.Finished;
import ch.uzh.ifi.seal.soprafs20.service.gamestates.GameState;
import ch.uzh.ifi.seal.soprafs20.service.gamestates.Lobby;
import ch.uzh.ifi.seal.soprafs20.service.gamestates.Running;
import junit.framework.AssertionFailedError;
import org.hibernate.cfg.NotYetImplementedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;



    private Game testGame;
    private User testUser;

    @Mock
    private PokeAPICacheRepository pokeAPICacheRepository;

    @MockBean
    private PokeAPICacheService pokeAPICacheService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        // given
        testGame = new Game(new Player(testUser));
        testGame.setToken("testGameToken");
        testGame.setState(GameStateEnum.LOBBY);
        testGame.setGameName("helloGame");
        testGame.setMode(Mode.SOCIAL);

        // when -> any object is being save in the userRepository -> return the dummy testUser
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);
    }

    @Test
    public void Test_createLobby() {
        //given a user with a username
        User testUser = new User();
        testUser.setUsername("testUserName");


        // when gamerepository is called
        Mockito.when(gameRepository.findByToken(Mockito.anyString())).thenReturn(null);


        // create lobby with setup inputs
        Game returnedGame = gameService.createLobby(testGame, testUser);
        assertEquals(returnedGame.getGameName(),testGame.getGameName());
        Mockito.verify(gameRepository, Mockito.times(1)).save(Mockito.any());
    }


    @Test
    public void Test_addUser() {
        // given a game and a creator
        User testUser = new User();
        testUser.setId(100L);
        testGame = new Game(new Player(testUser));
        testGame.setToken("testGameToken");
        testGame.setState(GameStateEnum.LOBBY);
        testGame.setGameName("helloGame");
        testGame.setMode(Mode.SOCIAL);

        // ... and a second user to add
        User secondTestUser = new User();
        secondTestUser.setId(200L);

        // when findByToken
        Mockito.when(gameRepository.findByToken(Mockito.anyString())).thenReturn(testGame);

        // then check if user is added
        gameService.addPlayer("testToken",secondTestUser);
        assertEquals(testGame.getPlayers().get(1).getUser().getId(),200L);
    }

    @Test
    public void Test_addUser_invalidStateRunning() {
        // given a game and a creator
        User testUser = new User();
        testUser.setId(100L);
        testGame = new Game(new Player(testUser));
        testGame.setToken("testGameToken");
        testGame.setState(GameStateEnum.RUNNING);
        testGame.setGameName("helloGame");
        testGame.setMode(Mode.SOCIAL);

        // ... and a second user to add
        User secondTestUser = new User();
        secondTestUser.setId(200L);

        // when findByToken
        Mockito.when(gameRepository.findByToken(Mockito.anyString())).thenReturn(testGame);

        // then check if correct exception is thrown in running state
        assertThrows(GameBadRequestException.class, () -> gameService.addPlayer("testToken",secondTestUser));
    }

    @Test
    public void Test_addUser_invalidStateFinished() {
        // given a game and a creator
        User testUser = new User();
        testUser.setId(100L);
        testGame = new Game(new Player(testUser));
        testGame.setToken("testGameToken");
        testGame.setState(GameStateEnum.FINISHED);
        testGame.setGameName("helloGame");
        testGame.setMode(Mode.SOCIAL);

        // ... and a second user to add
        User secondTestUser = new User();
        secondTestUser.setId(200L);

        // when findByToken
        Mockito.when(gameRepository.findByToken(Mockito.anyString())).thenReturn(testGame);

        // then check if correct exception is thrown in finished state
        assertThrows(GameBadRequestException.class, () -> gameService.addPlayer("testToken",secondTestUser));
    }

    @Test
    public void Test_removeUser_LobbyState() {
        // given a game and a creator
        User testUser = new User();
        testUser.setId(100L);
        testGame = new Game(new Player(testUser));
        testGame.setToken("testGameToken");
        testGame.setState(GameStateEnum.LOBBY);
        testGame.setGameName("helloGame");
        testGame.setMode(Mode.SOCIAL);

        // ... and a second user to add and remove
        User secondTestUser = new User();
        secondTestUser.setId(200L);

        // when findByToken
        Mockito.when(gameRepository.findByToken(Mockito.anyString())).thenReturn(testGame);

        // add player and remove player and then check if a player is saved at index 1 --> exception
        gameService.addPlayer("testToken",secondTestUser);
        gameService.removePlayer("testToken",secondTestUser);
        assertThrows(java.lang.IndexOutOfBoundsException.class, () -> testGame.getPlayers().get(1));
    }

    @Test
    public void Test_removeUser_whileRunningState() {
        // given a game and a creator
        User testUser = new User();
        testUser.setId(100L);
        Player player1 = new Player(testUser);
        testGame = new Game(player1);
        testGame.setToken("testGameToken");
        testGame.setState(GameStateEnum.LOBBY);
        testGame.setGameName("helloGame");
        testGame.setMode(Mode.SOCIAL);
        testGame.setTurnPlayer(player1);

        // ... and a second user to add and remove
        User secondTestUser = new User();
        secondTestUser.setUsername("leaver");
        secondTestUser.setId(200L);

        // when findByToken
        Mockito.when(gameRepository.findByToken(Mockito.anyString())).thenReturn(testGame);

        // now set game to running state and then remove a player
        gameService.addPlayer("testToken",secondTestUser);
        testGame.setState(GameStateEnum.RUNNING);
        gameService.removePlayer("testToken",secondTestUser);
        assertThrows(java.lang.IndexOutOfBoundsException.class, () -> testGame.getPlayers().get(1));
    }

    @Test
    public void startGame_invalidStateRunning() {
        // given a game in runningState
        User testUser = new User();
        testUser.setId(100L);
        testGame = new Game();
        testGame.setState(GameStateEnum.RUNNING);

        // then if exception if game is already running
        assertThrows(GameBadRequestException.class, () -> gameService.startGame(0,testGame,2,1));
    }

    @Test
    public void Test_getGame() {
        // given a game
        User testUser = new User();
        testUser.setId(100L);
        testGame = new Game(new Player(testUser));
        testGame.setToken("testGameToken");
        testGame.setState(GameStateEnum.LOBBY);
        testGame.setGameName("helloGame");
        testGame.setMode(Mode.SOCIAL);
        User secondTestUser = new User();
        secondTestUser.setId(200L);

        // when findByToken finds a game
        Mockito.when(gameRepository.findByToken(Mockito.anyString())).thenReturn(testGame);

        // then check if correct game is returned
        Game returnedGame = gameService.getGame("testToken");
        assertEquals(returnedGame.getToken(),testGame.getToken());
    }

    @Test
    public void Test_validateGame_invalidInputs() {
        // when findByToken returns null
        Mockito.when(gameRepository.findByToken(Mockito.anyString())).thenReturn(null);

        // then check for exception
        assertThrows(GameNotFoundException.class, () -> gameService.validateGame("testToken"));
    }

    @Test
    public void Test_validateCreator_invalidInputs() {
        // given a game and a creator
        User differentCreator = new User();
        differentCreator.setToken("otherToken");
        differentCreator.setToken("testToken");
        User testUser = new User();
        testUser.setToken("thisToken");
        Game differentGame = new Game(new Player(differentCreator));

        // when findByToken returns a game with a creator
        Mockito.when(gameRepository.findByToken(Mockito.anyString())).thenReturn(differentGame);

        // then check if exception is thrown for unauthorized creator access
        assertThrows(GameForbiddenException.class, () -> gameService.validateCreator("testToken", testUser));
    }


    @Test
    public void Test_validateTurnplayer_invalidInputs() {
        // given a game and a creator
        User turnPlayer = new User();
        turnPlayer.setToken("turnToken");
        User testUser = new User();
        testUser.setToken("testToken");
        Game game = new Game(new Player(turnPlayer));
        game.setTurnPlayer(new Player(turnPlayer));

        // when findByToken returns game
        Mockito.when(gameRepository.findByToken(Mockito.anyString())).thenReturn(game);

        // then check if exception is thrown for unauthorized turnplayer access
        assertThrows(GameForbiddenException.class, () -> gameService.validateTurnPlayer("testToken", testUser));
    }


    @Test
    public void Test_validatePlayer_invalidInputs() {
        // given a game and a creator and a second player
        User alienUser = new User();
        alienUser.setId(100000L);
        User testUser = new User();
        testUser.setId(100L);
        Game game = new Game(new Player(testUser));

        // when findByToken returns game with only one player
        Mockito.when(gameRepository.findByToken(Mockito.anyString())).thenReturn(game);

        // then check if exception is thrown for unauthorized player access
        assertThrows(GameForbiddenException.class, () -> gameService.validatePlayer("testToken", alienUser));
    }

    @Test
    public void Test_getUserFromPlayer() {
        // given a game and a user (creator)
        User alienUser = new User();
        alienUser.setId(100000L);
        Player alienPlayer = new Player(alienUser);
        Game game = new Game(alienPlayer);

        // then check if alienUser is part of the getPlayer()
        Player returnedPlayer = gameService.getPlayerFromUser(game,alienUser);
        assertEquals(returnedPlayer.getUser().getId(),alienUser.getId());
    }

    @Test
    public void Test_deleteGame() {
        // given a game
        Player player1 = new Player(testUser);
        Game game = new Game(player1);
        game.setToken("123");
        game.setTurnPlayer(player1);
        List<Player> list = new ArrayList<>();
        list.add(player1);
        game.setWinners(list);

        // when deleteByToken returns one (deleted game)
        Mockito.when(gameRepository.deleteByToken(Mockito.anyString())).thenReturn(1L);

        // then check for resetted parameters
        gameService.deleteGame(game);
        assertEquals(game.getPlayers().size(),0);
        assertNull(game.getTurnPlayer());
        assertNull(game.getCreator());
        assertEquals(game.getWinners().size(),0);
        Mockito.verify(gameRepository, Mockito.times(1)).deleteByToken(Mockito.any());
    }

    @Test
    public void Test_deleteGame_noGameDeleted() {
        // given a game
        Player player1 = new Player(testUser);
        Game game = new Game(player1);
        game.setToken("123");
        game.setTurnPlayer(player1);
        List<Player> list = new ArrayList<>();
        list.add(player1);
        game.setWinners(list);

        // when deleteByToken returns zero (deleted game)
        Mockito.when(gameRepository.deleteByToken(Mockito.anyString())).thenReturn(0L);

        // then check if exception if no game is deleted
        assertThrows(GameNotFoundException.class, () -> gameService.deleteGame(game));
        Mockito.verify(gameRepository, Mockito.times(1)).deleteByToken(Mockito.any());
    }

    @Test
    public void Test_deleteGame_multipleGamesDeleted() {
        // given a game
        Player player1 = new Player(testUser);
        Game game = new Game(player1);
        game.setToken("123");
        game.setTurnPlayer(player1);
        List<Player> list = new ArrayList<>();
        list.add(player1);
        game.setWinners(list);

        // when deleteByToken returns more than one (deleted game)
        Mockito.when(gameRepository.deleteByToken(Mockito.anyString())).thenReturn(2L);

        // then check if exception if multiple games are deleted
        assertThrows(SopraServiceException.class, () -> gameService.deleteGame(game));
        Mockito.verify(gameRepository, Mockito.times(1)).deleteByToken(Mockito.any());
    }

    @Test
    public void Test_nextTurn_invalidStateLobby() {
        //give a game in lobby state
        Player player1 = new Player(testUser);
        Game game = new Game(player1);
        game.setState(GameStateEnum.LOBBY);

        // then check if lobby throws an exception bc of invalid state
        assertThrows(GameBadRequestException.class, () -> gameService.nextTurn(game));
        Mockito.verify(gameRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    public void Test_nextTurn_invalidStateFinished() {
        //give a game in finished state
        Player player1 = new Player(testUser);
        Game game = new Game(player1);
        game.setState(GameStateEnum.FINISHED);

        // then check if finished throws an exception because of invalid state
        assertThrows(GameBadRequestException.class, () -> gameService.nextTurn(game));
        Mockito.verify(gameRepository, Mockito.times(0)).save(Mockito.any());
    }


    @Test
    public void Test_nextTurn_unfinishedGame() {
        //give a game in running state and two users with 2 cards each
        User testUser = new User();
        testUser.setId(100L);
        testUser.setUsername("turnplayer");
        Player player1 = new Player(testUser);
        List<Player> winners = new ArrayList<>();
        UniqueBaseEvolutionPokemonGenerator uniquePkmId = new UniqueBaseEvolutionPokemonGenerator(1);
        player1.setDeck(new Deck(uniquePkmId,2));

        User secondUser = new User();
        secondUser.setId(200L);
        secondUser.setUsername("hansundheiri");
        Player player2 = new Player(secondUser);
        UniqueBaseEvolutionPokemonGenerator uniquePkmId2 = new UniqueBaseEvolutionPokemonGenerator(1);
        player2.setDeck(new Deck(uniquePkmId2,2));

        Game game = new Game(player1);
        game.setWinners(winners);
        game.setState(GameStateEnum.RUNNING);
        game.addPlayer(player2);
        game.setTurnPlayer(player1);


        // then check if parameters changed correctly
        gameService.nextTurn(game);
        assertEquals(game.getWinners().size(),0);
        Mockito.verify(gameRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void Test_nextTurn_finishedGame() {
        //give a game in running state and two users with 1 cards each + --> game will be finished after
        User testUser = new User();
        testUser.setId(10L);
        testUser.setStatistics(new Statistics());
        Player player1 = new Player(testUser);
        List<Player> winners = new ArrayList<>();
        winners.add(player1);
        UniqueBaseEvolutionPokemonGenerator uniquePkmId = new UniqueBaseEvolutionPokemonGenerator(1);
        player1.setDeck(new Deck(uniquePkmId,1));

        User secondUser = new User();
        secondUser.setId(20L);
        secondUser.setStatistics(new Statistics());
        Player player2 = new Player(secondUser);
        UniqueBaseEvolutionPokemonGenerator uniquePkmId2 = new UniqueBaseEvolutionPokemonGenerator(1);
        player2.setDeck(new Deck(uniquePkmId2,1));

        Game game = new Game(player1);
        game.setWinners(winners);
        game.setState(GameStateEnum.RUNNING);
        game.addPlayer(player2);


        // then check if parameters changed correctly
        gameService.nextTurn(game);
        assertEquals(game.getWinners(),winners);
        assertEquals(game.getState(),GameStateEnum.FINISHED);
        Mockito.verify(gameRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void Test_getWinner_whileRunningState() {
        //give a game in running state and two users with 1 cards each
        Player player1 = new Player(testUser);
        UniqueBaseEvolutionPokemonGenerator uniquePkmId = new UniqueBaseEvolutionPokemonGenerator(1);
        player1.setDeck(new Deck(uniquePkmId,1));

        User secondUser = new User();
        Player player2 = new Player(secondUser);
        UniqueBaseEvolutionPokemonGenerator uniquePkmId2 = new UniqueBaseEvolutionPokemonGenerator(1);
        player2.setDeck(new Deck(uniquePkmId2,1));

        Game game = new Game(player1);
        game.setState(GameStateEnum.RUNNING);
        game.addPlayer(player2);
        game.setCategory(Category.ATK);

        // initiate a running state
        Running running = new Running();

        // then check if parameters changed correctly
        ArrayList<Player> winners = running.getWinner(game);
        assertTrue(winners.size()>0);
    }

    @Test
    public void Test_getWinner_whileFinishedState() {
        //give a game in running state and two users with 1 cards each
        Player player1 = new Player(testUser);
        UniqueBaseEvolutionPokemonGenerator uniquePkmId = new UniqueBaseEvolutionPokemonGenerator(1);
        player1.setDeck(new Deck(uniquePkmId,1));

        User secondUser = new User();
        Player player2 = new Player(secondUser);
        UniqueBaseEvolutionPokemonGenerator uniquePkmId2 = new UniqueBaseEvolutionPokemonGenerator(1);
        player2.setDeck(new Deck(uniquePkmId2,1));

        Game game = new Game(player1);
        game.setState(GameStateEnum.RUNNING);
        game.addPlayer(player2);
        game.setCategory(Category.ATK);

        // initiate a running state
        Finished finished = new Finished();

        // then check if parameters changed correctly
        ArrayList<Player> winners = finished.getWinner(game);
        assertNull(winners);
    }

    @Test
    public void Test_selectCategory_whileRunningState() {
        //give a game in running state and two users with 1 cards each
        Player player1 = new Player(testUser);
        UniqueBaseEvolutionPokemonGenerator uniquePkmId = new UniqueBaseEvolutionPokemonGenerator(1);
        player1.setDeck(new Deck(uniquePkmId,1));

        User secondUser = new User();
        Player player2 = new Player(secondUser);
        UniqueBaseEvolutionPokemonGenerator uniquePkmId2 = new UniqueBaseEvolutionPokemonGenerator(1);
        player2.setDeck(new Deck(uniquePkmId2,1));

        Game game = new Game(player1);
        game.setState(GameStateEnum.RUNNING);
        game.addPlayer(player2);

        // initiate a running state
        Running running = new Running();

        // then check if parameters changed correctly
        running.selectCategory(game,Category.ATK);
        assertNotEquals(game.getWinners(),null);
        assertNotEquals(game.getCategory(),null);
    }

    @Test
    public void Test_selectCategory_invalidStateLobby() {
        //give a game in lobby state and two users with 1 cards each
        Player player1 = new Player(testUser);
        UniqueBaseEvolutionPokemonGenerator uniquePkmId = new UniqueBaseEvolutionPokemonGenerator(1);
        player1.setDeck(new Deck(uniquePkmId,1));

        User secondUser = new User();
        Player player2 = new Player(secondUser);
        UniqueBaseEvolutionPokemonGenerator uniquePkmId2 = new UniqueBaseEvolutionPokemonGenerator(1);
        player2.setDeck(new Deck(uniquePkmId2,1));

        Game game = new Game(player1);
        game.setState(GameStateEnum.RUNNING);
        game.addPlayer(player2);

        // initiate a running state
        Lobby lobby = new Lobby();
        assertThrows(GameBadRequestException.class, () -> lobby.selectCategory(game,Category.ATK));
    }

    @Test
    public void Test_selectCategory_invalidStateFinished() {
        //give a game in running state and two users with 1 cards each
        Player player1 = new Player(testUser);
        UniqueBaseEvolutionPokemonGenerator uniquePkmId = new UniqueBaseEvolutionPokemonGenerator(1);
        player1.setDeck(new Deck(uniquePkmId,1));

        User secondUser = new User();
        Player player2 = new Player(secondUser);
        UniqueBaseEvolutionPokemonGenerator uniquePkmId2 = new UniqueBaseEvolutionPokemonGenerator(1);
        player2.setDeck(new Deck(uniquePkmId2,1));

        Game game = new Game(player1);
        game.setState(GameStateEnum.RUNNING);
        game.addPlayer(player2);

        // initiate a running state
        Finished finished = new Finished();
        assertThrows(GameBadRequestException.class, () -> finished.selectCategory(game,Category.ATK));
    }

    @Test
    public void Test_userBerries_validInput_ValidState() {
        //give a game in running state and a user with one card
        User testUser = new User();
        testUser.setUsername("testUserName");
        Player player1 = new Player(testUser);
        Card bulbasaur = new Card("bulbasaur");
        Deck bulbasaurOnlyDeck = new Deck();
        bulbasaurOnlyDeck.addCard(bulbasaur);
        player1.setDeck(bulbasaurOnlyDeck);
        player1.setBerries(1);
        testUser.setStatistics(new Statistics());
        player1.getUser().getStatistics().setEncounteredPokemon(new ArrayList<>());

        Game game = new Game(player1);
        game.setState(GameStateEnum.RUNNING);

        // initiate a running state
        Running running = new Running();

        // then check if the only card has evolved
            //bulbasaur id is 1 in our database
        assertEquals(game.getPlayers().get(0).getDeck().getCards().get(0).getPokemonId(),1);
        running.useBerries(game,1,player1);
            //ivysaur id is 2 in our database
        assertEquals(game.getPlayers().get(0).getDeck().getCards().get(0).getPokemonId(),2);
    }

    @Test
    public void Test_userBerries_tooMuchBerriesForEvolution_ValidState() {
        //give a game in running state and a user with one card
        User testUser = new User();
        testUser.setUsername("testUserName");
        Player player1 = new Player(testUser);
        Card bulbasaur = new Card("bulbasaur");
        Deck bulbasaurOnlyDeck = new Deck();
        bulbasaurOnlyDeck.addCard(bulbasaur);
        player1.setDeck(bulbasaurOnlyDeck);
        player1.setBerries(3);

        Game game = new Game(player1);
        game.setState(GameStateEnum.RUNNING);

        // initiate a running state
        Running running = new Running();

        // then check if exception is thrown because this pokemon can not evolve 3 times
        assertThrows(GameBadRequestException.class, () -> running.useBerries(game,3, player1));
    }

    @Test
    public void Test_userBerries_tooFewBerriesAvailable_ValidState() {
        //give a game in running state a user with no berries available
        User testUser = new User();
        testUser.setUsername("testUserName");
        Player player1 = new Player(testUser);
        Card bulbasaur = new Card("bulbasaur");
        Deck bulbasaurOnlyDeck = new Deck();
        bulbasaurOnlyDeck.addCard(bulbasaur);
        player1.setDeck(bulbasaurOnlyDeck);
        player1.setBerries(0);

        Game game = new Game(player1);
        game.setState(GameStateEnum.RUNNING);

        // initiate a running state
        Running running = new Running();

        // then check if exception is thrown because no berries are available
        assertThrows(GameBadRequestException.class, () -> running.useBerries(game,3, player1));
    }

    @Test
    public void Test_userBerries_emptyDeck_ValidState() {
        //give a game in running state a user with an empty deck
        User testUser = new User();
        testUser.setUsername("testUserName");
        Player player1 = new Player(testUser);
        player1.setBerries(3);
        player1.setDeck(new Deck());

        Game game = new Game(player1);
        game.setState(GameStateEnum.RUNNING);

        // initiate a running state
        Running running = new Running();

        // then check if exception is thrown because there is no card to evolve
        //bulbasaur id is 1 in our database
        assertThrows(GameBadRequestException.class, () -> running.useBerries(game,0, player1));
    }

    @Test
    public void Test_userBerries_validInput_invalidStateLobby() {
        //give a game in lobby state and a user with one card
        User testUser = new User();
        testUser.setUsername("testUserName");
        Player player1 = new Player(testUser);
        Card bulbasaur = new Card("bulbasaur");
        Deck bulbasaurOnlyDeck = new Deck();
        bulbasaurOnlyDeck.addCard(bulbasaur);
        player1.setDeck(bulbasaurOnlyDeck);
        player1.setBerries(1);

        Game game = new Game(player1);
        game.setState(GameStateEnum.LOBBY);

        // initiate a lobby state
        Lobby lobby = new Lobby();

        // then check if exception is thrown because the game state is illegal
        assertThrows(GameBadRequestException.class, () -> lobby.useBerries(game,1, player1));
    }

    @Test
    public void Test_userBerries_validInput_invalidStateFinished() {
        //give a game in finished state a user with one card
        User testUser = new User();
        testUser.setUsername("testUserName");
        Player player1 = new Player(testUser);
        Card bulbasaur = new Card("bulbasaur");
        Deck bulbasaurOnlyDeck = new Deck();
        bulbasaurOnlyDeck.addCard(bulbasaur);
        player1.setDeck(bulbasaurOnlyDeck);
        player1.setBerries(1);

        Game game = new Game(player1);
        game.setState(GameStateEnum.FINISHED);

        // initiate a finished state
        Finished finished = new Finished();

        // then check if exception is thrown because the game state is illegal
        assertThrows(GameBadRequestException.class, () -> finished.useBerries(game,1, player1));
    }

    @Test
    public void Test_distributeCards_inRunning() {
        // given 2 players
        User testUser = new User();
        testUser.setId(10L);
        testUser.setUsername("testUserName");
        Player player1 = new Player(testUser);
        testUser.setStatistics(new Statistics());
        testUser.getStatistics().setEncounteredPokemon(new ArrayList<>());
        User secondUser = new User();
        secondUser.setId(20L);
        secondUser.setUsername("testUserName2");
        Player player2 = new Player(secondUser);
        secondUser.setStatistics(new Statistics());
        secondUser.getStatistics().setEncounteredPokemon(new ArrayList<>());


        // given 1 card each player
        Deck deck1 = new Deck();
        deck1.addCard(new Card("ivysaur"));
        player1.setDeck(deck1);
        Deck deck2 = new Deck();
        deck2.addCard(new Card("bulbasaur"));
        player2.setDeck(deck2);

        // given a game with the two players and player1 as winner
        Game game = new Game(player1);
        game.setState(GameStateEnum.RUNNING);
        game.addPlayer(player2);
        List<Player> winners = new ArrayList<>();
        winners.add(player1);
        game.setWinners(winners);

        // initiate a running state
        Running running = new Running();

        // then distribute the played/peak cards to the winner (player1)
        running.distributeCards(game);

        //check if ivysaur (pokemonId 2) is on top of player1 deck again
        // and bulbasaur (pokemonId 1) is below ivysaur
        assertEquals(player1.getDeck().getCards().get(0).getPokemonId(),2);
        assertEquals(player1.getDeck().getCards().get(1).getPokemonId(),1);
        assertTrue(player2.getDeck().isEmpty());
    }

    @Test
    public void Test_newTurnPlayer_inRunning() {
        //give a game in running state and 2 players with player2 as winner and player1 as turnplayer
        User testUser = new User();
        testUser.setId(20L);
        Player player1 = new Player(testUser);

        User secondUser = new User();
        secondUser.setId(1000L);
        secondUser.setUsername("secondUserTest");
        Player player2 = new Player(secondUser);

        List<Player> winners = new ArrayList<>();
        winners.add(player2);

        Game game = new Game(player1);
        game.setWinners(winners);
        game.setTurnPlayer(player1);
        game.setState(GameStateEnum.RUNNING);
        game.addPlayer(player2);

        // initiate a running state
        Running running = new Running();

        // then check if player2 (=winner) is now the turnplayer
        running.setNewTurnPlayer(game);
        assertTrue(game.getTurnPlayer().getUser().getId()==secondUser.getId());
    }

    @Test
    public void Test_isWinner_inRunning() {
        //give a game in running state and 2 players with player2 as winner
        Player player1 = new Player(testUser);

        User secondUser = new User();
        Player player2 = new Player(secondUser);

        List<Player> winners = new ArrayList<>();
        winners.add(player2);

        Game game = new Game(player1);
        game.setWinners(winners);
        game.setState(GameStateEnum.RUNNING);
        game.addPlayer(player2);

        // initiate a running state
        Running running = new Running();

        // then check if player2 is in winner
        assertTrue(running.isWinner(game, player2));
    }

    @Test
    public void Test_isFinished_inRunning() {
        //give a game in running state and 2 players with player2 as winner
        Player player1 = new Player(testUser);
        player1.setDeck(new Deck());
        User secondUser = new User();
        Player player2 = new Player(secondUser);
        player2.setDeck(new Deck());

        List<Player> winners = new ArrayList<>();
        winners.add(player1);

        Game game = new Game(player1);
        game.setWinners(winners);
        game.setState(GameStateEnum.RUNNING);
        game.addPlayer(player2);

        // initiate a running state
        Running running = new Running();

        // then check if game is finished when players have no cards
        assertTrue(running.isFinished(game));
    }
}

                                                                                                                                                                                                                                                                                                                                                /*░░░░░░░░░░░░░░░█████████░░░░░░░░░░░░░░░░
                                                                                                                                                                                                                                                                                                                                                ░░░░░░░░░░░░░░██░░░░█░░░█░░░░░░░░░░░░░░░
                                                                                                                                                                                                                                                                                                                                                ░░░░░░░░░░░░░██░░░░░░░░░█░░░░░░░░░░░░░░░
                                                                                                                                                                                                                                                                                                                                                ░░░░░░░░░░░░█░░░░░░░░░░░█░░░░░░░░░░░░░░░
                                                                                                                                                                                                                                                                                                                                                ░░░░░░░░░░░░█░░░░░░░░░░░█░░░░░░░░░░░░░░░
                                                                                                                                                                                                                                                                                                                                                ░░░░░░░░░░░██████████████░░░░░░░░░░░░░░░
                                                                                                                                                                                                                                                                                                                                                ░░░░░░░░░░░█░░░░░░░░░░░░█░░░░░░░░░░░░░░░
                                                                                                                                                                                                                                                                                                                                                ░░░░░░░░░░░█░░░░░░░░░░░██░░░░░░░░░░░░░░░
                                                                                                                                                                                                                                                                                                                                                ░░░░░░░░░░█░░░░░░░░░░░░░█░░░░░░░░░░░░░░░
                                                                                                                                                                                                                                                                                                                                                ░░░░░░░░░░█░░░░░░░░░░░░░█░░░░░░░░░░░░░░░
                                                                                                                                                                                                                                                                                                                                                ░░░░░░░░░░█░░░░░░░░░░░░░█░░░░░░░░░░░░░░░
                                                                                                                                                                                                                                                                                                                                                ░░░░░░░░░░█░░░░░░░█░░░░░█░░░░░░░░░░░░░░░
                                                                                                                                                                                                                                                                                                                                                ░░░░░░░░░░██░░░░░░░░░░░░█░░██████░░░░░░░
                                                                                                                                                                                                                                                                                                                                                ░░░░░░░░░░░█░░░░░░░░░░░█████░░░░██░░░░░░
                                                                                                                                                                                                                                                                                                                                                ░░░░░░░░░░░█░░░░░░░░░░██░░░░░░░░░█░░░░░░
                                                                                                                                                                                                                                                                                                                                                ░░░░░░██████░░░░░░░░░░██░░░░░░░░░██░░░░░
                                                                                                                                                                                                                                                                                                                                                ░░░░███░░░░██░░░░░░░░░░░░░░█░░░░░░█░░░░░
                                                                                                                                                                                                                                                                                                                                                ░░░░█░░░░░░░░░░░░░░░░░░░░░░░░░░░░██░░░░░
                                                                                                                                                                                                                                                                                                                                                ░░░█░░░░░░░░░░░█░░░░░█░░░░░░░░░░░█░░░░░░
                                                                                                                                                                                                                                                                                                                                                ░░░██░█░░░░░░░░░░░░░░░░░░░░█░░░░██░░░░░░
                                                                                                                                                                                                                                                                                                                                                ░░░░██░░░░░█░░░░░░░░░░░░░░░░░░██░░░░░░░░
                                                                                                                                                                                                                                                                                                                                                ░░░░░███░░░░░░░░█░░░░█░░░█████░░░░░░░░░░
                                                                                                                                                                                                                                                                                                                                                ░░░░░░░██░░░░░░░░░░░░░░███░░░░░░░░░░░░░░
                                                                                                                                                                                                                                                                                                                                                ░░░░░░░░░░░░░░░░░░░░░░██░░░░░░░░░░░░░░░░
                                                                                                                                                                                                                                                                                                                                                ░░░░░░░░░░░░░░░░░░░░░██░░░░░░░░░░░░░░░░░
                                                                                                                                                                                                                                                                                                                                                ░░░░░░░░░░░░░░░██░░░░█░░░░░░░░░░░░░░░░░░
                                                                                                                                                                                                                                                                                                                                                ░░░░░░░░░░░░░░░░██████░░░░░░░░░░░░░░░░░░
                                                                                                                                                                                                                                                                                                                                                ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
                                                                                                                                                                                                                                                                                                                                                ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
                                                                                                                                                                                                                                                                                                                                                ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░*/
