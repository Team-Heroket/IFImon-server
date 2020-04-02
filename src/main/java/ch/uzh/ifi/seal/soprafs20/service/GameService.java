package ch.uzh.ifi.seal.soprafs20.service;

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

    private final GameRepository gameRepository;

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public String createGame(String gameName, Enum mode){
        return "testToken";
    }

    public void addUser(String userName, String gameToken){

    }

    public void removeUser(String userName, String gameToken){

    }

    public void setNPCAmount(Integer amount, String gameToken){

    }

    public Tables getGame(String gameToken){
        Tables table = new Tables();
        return table;
    }

    public void selectCategory(Enum category, String gameToken){
        //class diagram has mispelled name
    }

    public void userBerries(Integer amount, String userName){

    }


    //CODE Methods

}
