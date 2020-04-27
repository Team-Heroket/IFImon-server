package ch.uzh.ifi.seal.soprafs20.rest.dto;


import ch.uzh.ifi.seal.soprafs20.constant.Category;
import ch.uzh.ifi.seal.soprafs20.constant.Element;

import java.util.List;
import java.util.Map;

public class CardGetDTO {

    private int pokemonId;

    private Map<Category,Integer> categories;

    private String name;

    private String spriteURL;

    private String cryURL;

    private List<Element> elements;

    //private List<String> evolutionNames;

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

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    /*
    public List<String> getEvolutionNames() {
        return evolutionNames;
    }

    public void setEvolutionNames(List<String> evolutionNames) {
        this.evolutionNames = evolutionNames;
    }
     */
}
