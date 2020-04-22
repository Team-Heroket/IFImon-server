package ch.uzh.ifi.seal.soprafs20.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import ch.uzh.ifi.seal.soprafs20.constant.Category;
import ch.uzh.ifi.seal.soprafs20.constant.Element;
import ch.uzh.ifi.seal.soprafs20.repository.PokeAPICacheRepository;
import ch.uzh.ifi.seal.soprafs20.rest.PokeAPI;
import org.json.JSONArray;
import org.json.JSONObject;

@Entity
@Table(name = "CARD")
public class Card implements Serializable, ICard {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int pokemonId;

    @Column(nullable = false)
    @ElementCollection
    private Map<Category,Integer> categories;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String spriteURL;

    @Column(nullable = true)
    private String cryURL;

    @Column(nullable = false)
    @ElementCollection(targetClass=Element.class)
    private List<Element> elements;

    @Column(nullable = false)
    @ElementCollection(targetClass=String.class)
    private List<String> evolutionNames;

    // For the DTOs
    public Card() {}

    public Card(ICard card) {
        this.pokemonId = card.getPokemonId();
        this.categories = new HashMap<>(card.getCategories());
        this.name = card.getName();
        this.spriteURL = card.getSpriteURL();
        this.cryURL = card.getCryURL();
        this.elements = new ArrayList<>(card.getElements());
        this.evolutionNames = new ArrayList<>(card.getEvolutionNames());
    }

    // For the real use

    /**
     * Gets the card by id
     *
     * @param pokemonId Id of Pokémon
     */
    public Card(int pokemonId) {
        JSONObject pokemon = PokeAPI.getPokemon(pokemonId);
        JSONObject species = PokeAPI.getSpecies(pokemonId);
        this.initialise(pokemon, species);
    }

    /**
     * Gets the card by name
     *
     * @param pokemonName Name of Pokémon
     */
    public Card(String pokemonName) {
        JSONObject pokemon = PokeAPI.getPokemon(pokemonName);
        JSONObject species = PokeAPI.getSpecies(pokemonName);
        this.initialise(pokemon, species);
    }

    // Initialises the card
    private void initialise(JSONObject pokemon, JSONObject species) {
        JSONObject evolutionChain = PokeAPI.getFromURL(species.getJSONObject("evolution_chain").getString("url"));

        // Set ID
        this.pokemonId = pokemon.getInt("id");

        // Categories
        this.categories = new HashMap<>();
        JSONArray stats = pokemon.getJSONArray("stats");
        this.categories.put(Category.SPEED, stats.getJSONObject(0).getInt("base_stat"));
        this.categories.put(Category.DEF, stats.getJSONObject(3).getInt("base_stat"));
        this.categories.put(Category.ATK, stats.getJSONObject(4).getInt("base_stat"));
        this.categories.put(Category.HP, stats.getJSONObject(5).getInt("base_stat"));

        this.categories.put(Category.WEIGHT, pokemon.getInt("weight"));
        this.categories.put(Category.CAPTURE_RATING, species.getInt("capture_rate"));

        // Name, first char to uppercase
        String name = species.getString("name");
        this.name = name.substring(0, 1).toUpperCase() + name.substring(1);
        // Sprite URL
        JSONObject sprites = pokemon.getJSONObject("sprites");
        this.spriteURL = sprites.getString("front_default");

        //check for valid?
        this.cryURL = String.format("https://play.pokemonshowdown.com/audio/cries/%s.mp3",this.name.toLowerCase());

        // Element converts to ENUM
        JSONArray types = pokemon.getJSONArray("types");
        this.elements = new ArrayList<>();
        for (int i = 0; i < types.length(); i++) {
            JSONObject type = types.getJSONObject(i);
            String typeName = type.getJSONObject("type").getString("name").toUpperCase();
            this.elements.add(Element.valueOf(typeName));
        }

        // Saves all possible evolutions
        JSONArray evolvesTo = evolutionChain.getJSONObject("chain").getJSONArray("evolves_to");
        this.evolutionNames = new ArrayList<>();
        while (!evolvesTo.isEmpty()) {
            int random = (int) (Math.random() * evolvesTo.length());
            JSONObject evolution = evolvesTo.getJSONObject(random);
            this.evolutionNames.add(evolution.getJSONObject("species").getString("name"));
            evolvesTo = evolution.getJSONArray("evolves_to");
        }

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPokemonId() {
        return pokemonId;
    }

    public void setPokemonId(int pokemonId) {
        this.pokemonId = pokemonId;
    }

    public Map<Category, Integer> getCategories() {
        return categories;
    }

    public void setCategories(Map<Category, Integer> categories) {
        this.categories = categories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpriteURL() {
        return spriteURL;
    }

    public void setSpriteURL(String spriteURL) {
        this.spriteURL = spriteURL;
    }

    public String getCryURL() {
        return cryURL;
    }

    public void setCryURL(String cryURL) {
        this.cryURL = cryURL;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(ArrayList<Element> elements) {
        this.elements = elements;
    }

    public List<String> getEvolutionNames() {
        return evolutionNames;
    }

    public void setEvolutionNames(ArrayList<String> evolutionNames) {
        this.evolutionNames = evolutionNames;
    }
}
