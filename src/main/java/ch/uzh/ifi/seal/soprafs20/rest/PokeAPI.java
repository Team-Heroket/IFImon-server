package ch.uzh.ifi.seal.soprafs20.rest;

import ch.uzh.ifi.seal.soprafs20.service.GameService;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class communicates with the PokéAPI
 */
public class PokeAPI {
    // Not an object, just use static methods

    private final static Logger log = LoggerFactory.getLogger(PokeAPI.class);

    // **** Settings
    private static String baseUrl = "https://pokeapi.co/api/v2/%s/%s/";

    public static JSONObject getPokemon(int id) {
        return getFromURL(String.format(baseUrl, "pokemon", id));
    }

    public static JSONObject getPokemon(String name) {
        return getFromURL(String.format(baseUrl, "pokemon", name));
    }

    public static JSONObject getSpecies(int id) {
        return getFromURL(String.format(baseUrl, "pokemon-species", id));
    }

    public static JSONObject getSpecies(String name) {
        return getFromURL(String.format(baseUrl, "pokemon-species", name));
    }

    public static JSONObject getFromURL(String strURL) {
        log.debug(String.format("PokéAPI call %s.", strURL));
        JSONObject root = null;
        try {
            URL url = new URL(strURL);
            JSONTokener tokener = new JSONTokener(url.openStream());
            root = new JSONObject(tokener);
        }
        // TODO: Handle this
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        log.debug("Call finished.");
        return root;
    }

    public static void main(String[] args) {
        // For testing purposes
        JSONObject test = getPokemon(151);
        System.out.println(test);
    }

}
