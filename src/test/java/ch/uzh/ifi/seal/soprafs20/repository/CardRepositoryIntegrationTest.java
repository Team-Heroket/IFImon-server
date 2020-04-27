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
public class CardRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CardRepository cardRepository;


    @Test
    public void findByToken_success() {
        // given
        Card card = new Card();
        card.setName("ifiMon");

        entityManager.persist(card);
        entityManager.flush();

        // when
        Card found = cardRepository.findByName(card.getName());

        // then check if found game is correct
        assertNotNull(found.getId());
        assertEquals(found.getName(), card.getName());
    }

    @Test
    public void findById_success() {
        // given
        Card card = new Card();
        card.setName("ifiMon");

        entityManager.persist(card);
        entityManager.flush();

        // when
        Optional<Card> foundOp = cardRepository.findById(card.getId());
        Card found = foundOp.get();

        // then check if found game is correct
        assertNotNull(found.getId());
        assertEquals(found.getName(), card.getName());
        assertEquals(found.getId(), card.getId());
    }
}
