package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.CachedCard;
import ch.uzh.ifi.seal.soprafs20.entity.Card;

import ch.uzh.ifi.seal.soprafs20.repository.PokeAPICacheRepository;
import ch.uzh.ifi.seal.soprafs20.repository.StatisticRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class CardService {

    private final Logger log = LoggerFactory.getLogger(CardService.class);

    private PokeAPICacheRepository pokeAPICacheRepository;


    @Autowired
    public CardService(@Qualifier("pokeAPICacheRepository") PokeAPICacheRepository pokeAPICacheRepository) {
        this.pokeAPICacheRepository = pokeAPICacheRepository;
    }

    public Card getCard(int id){

        // Danger! Evolutions Array contains previous evolutions too.

        log.debug("Card requested");
        // first check if card was already pulled from poke api
        CachedCard cachedCard = PokeAPICacheService.getCachedCard(id);

        // if not, then pull card data and cache it
        if (null == cachedCard) {
            Card newCard = new Card(id);
            PokeAPICacheService.cacheCard(newCard);
            return newCard;
        }

        log.debug(String.format("Cached card found! %s", cachedCard.getName()));
        return new Card(cachedCard);
    }
}
