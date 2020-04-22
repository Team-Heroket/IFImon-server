package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.Category;
import ch.uzh.ifi.seal.soprafs20.constant.Element;

import java.util.List;
import java.util.Map;

public interface ICard {

    int getPokemonId();
    Map<Category,Integer> getCategories();
    String getName();
    String getSpriteURL();
    String getCryURL();
    List<Element> getElements();
    List<String> getEvolutionNames();

}
