package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Card;

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

    public Map<Long,String> getCards(){
        Map<Long,String> testMap = new HashMap<Long,String>();
        return testMap;
    }

    public Card getCard(long id){
        Card card = new Card();
        card.setName("testCard");
        return card;
    }
}
