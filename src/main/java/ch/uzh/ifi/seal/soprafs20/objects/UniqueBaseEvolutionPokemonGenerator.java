package ch.uzh.ifi.seal.soprafs20.objects;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class UniqueBaseEvolutionPokemonGenerator {

    private LinkedList<Integer> pokemons;

    /**
     * "Generates" an unique PokemonId
     */
    public UniqueBaseEvolutionPokemonGenerator() {
        this.refresh();
    }

    private void refresh() {
        this.pokemons = new LinkedList<>(this.finalPokemons);
        Collections.shuffle(this.pokemons);
    }

    /**
     * Get a unique pokemon id
     *
     * @return a unique pokemon id
     */
    public Integer get() {
        if (this.pokemons.isEmpty()) {
            this.refresh();
        }
        return this.pokemons.pop();
    }

    // Base evolutions of the first 151 Pokémon
    private final List<Integer> finalPokemons = Arrays.asList(1, 4, 7, 10, 13, 16, 19, 21, 23, 25, 27,
            29, 32, 35, 37, 39, 41, 43, 46, 48, 50, 52, 54, 56, 58, 63, 66, 69, 72, 74, 77, 79, 81, 83, 84, 86, 88, 90,
            92, 95, 96, 98, 100, 102, 104, 106, 107, 108, 109, 111, 113, 114, 115, 116, 118, 120, 122, 123, 124, 125,
            126, 127, 128, 129, 131, 132, 133, 151, 150, 147, 146, 145, 144, 143, 142, 140, 138, 137);

}