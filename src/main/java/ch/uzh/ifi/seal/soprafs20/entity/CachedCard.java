package ch.uzh.ifi.seal.soprafs20.entity;


import ch.uzh.ifi.seal.soprafs20.constant.Category;
import ch.uzh.ifi.seal.soprafs20.constant.Element;
import ch.uzh.ifi.seal.soprafs20.rest.PokeAPI;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collection;

@Entity
@Table(name = "CACHED_CARD")
public class CachedCard implements Serializable, ICard {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int pokemonId;

    @Column(nullable = false)
    @ElementCollection(fetch = FetchType.EAGER)
    private Map<Category,Integer> categories;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String spriteURL;

    @Column(nullable = true)
    private String cryURL;

    @Column(nullable = false)
    @ElementCollection(fetch = FetchType.EAGER, targetClass=Element.class)
    private List<Element> elements;

    @Column(nullable = false)
    @ElementCollection(targetClass=String.class)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<String> evolutionNames;

    // For the DTOs
    public CachedCard() {}

    public CachedCard(ICard card) {
        this.pokemonId = card.getPokemonId();
        this.categories = new HashMap<>(card.getCategories());
        this.name = card.getName();
        this.spriteURL = card.getSpriteURL();
        this.cryURL = card.getCryURL();
        this.elements = new ArrayList<>(card.getElements());
        this.evolutionNames = new ArrayList<>(card.getEvolutionNames());
    }

    public Long getId() {
        return id;
    }

    @Override
    public int getPokemonId() {
        return pokemonId;
    }

    @Override
    public Map<Category, Integer> getCategories() {
        return categories;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSpriteURL() {
        return spriteURL;
    }

    @Override
    public String getCryURL() {
        return cryURL;
    }

    @Override
    public List<Element> getElements() {
        return elements;
    }

    @Override
    public List<String> getEvolutionNames() {
        return evolutionNames;
    }
}
