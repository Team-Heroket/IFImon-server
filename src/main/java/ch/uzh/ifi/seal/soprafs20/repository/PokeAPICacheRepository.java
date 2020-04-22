package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.entity.CachedCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pokeAPICacheRepository")
public interface PokeAPICacheRepository extends JpaRepository<CachedCard, Long> {
    CachedCard findByPokemonId(int pokemonId);
    CachedCard findByName(String name);
}
