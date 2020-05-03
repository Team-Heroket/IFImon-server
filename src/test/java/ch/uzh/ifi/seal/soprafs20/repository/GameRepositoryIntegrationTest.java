package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class GameRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GameRepository gameRepository;


    @Test
    public void findByToken_success() {
        // given
       Game game = new Game();
       game.setToken("testToken");
       game.setGameName("testGameName");
       game.setCreationTime("tday");

        entityManager.persist(game);
        entityManager.flush();

        // when
        Game found = gameRepository.findByToken(game.getToken());

        // then check if found game is correct
        assertNotNull(found.getId());
        assertEquals(found.getGameName(), game.getGameName());
        assertEquals(found.getToken(), game.getToken());
    }

    @Test
    public void deleteByToken_success() {
        // given
        Game game = new Game();
        game.setToken("testToken");
        game.setGameName("testGameName");
        game.setCreationTime("tday");

        entityManager.persist(game);
        entityManager.flush();

        // when
        long number = gameRepository.deleteByToken(game.getToken());

        // then check if found game is correct
        assertEquals(number,1);
        assertNull(gameRepository.findByToken(game.getToken()));
    }


}
