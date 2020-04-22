package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.CachedCard;
import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.repository.PokeAPICacheRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PokeAPICacheService {

    private static PokeAPICacheRepository pokeAPICacheRepository;

    public static void setRepository(PokeAPICacheRepository pACR) {
        pokeAPICacheRepository = pACR;
    }

    public static void cacheCard(Card card) {
        pokeAPICacheRepository.save(new CachedCard(card));
        pokeAPICacheRepository.flush();
    }

    public static CachedCard getCachedCard(int id) {
        if (null == pokeAPICacheRepository) {
            throw new SopraServiceException("PokeAPICacheRepository got not set.");
        }

        return pokeAPICacheRepository.findByPokemonId(id);
    }

}
