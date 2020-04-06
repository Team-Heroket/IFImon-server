package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.rest.dto.CardGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.BoardGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.CardService;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class CardController {

    private final CardService cardService;

    CardController(UserService userService, GameService gameService, CardService cardService) {
        this.cardService = cardService;
    }


    /*     #13     */
    /** This request returns a map with all Pokémon IDs and Sprite urls **/

    @GetMapping("/cards")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Map<Long,String> getCardList(@RequestHeader("Token") String token) {

        //get a map with all ids and urls of the cards
        Map<Long,String> map = cardService.getCards();

        return map;
    }


    /*     #14     */
    /** This request returns a complete pkm-card **/

    @GetMapping("/cards/{pokemonId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public CardGetDTO getCard(@PathVariable String pokemonId, @RequestHeader("Token") String token) {

        //convert pathvaraible to long
        long cardId = Long.parseLong(pokemonId);

        //get card from service
        Card card = cardService.getCard(cardId);

        return DTOMapper.INSTANCE.convertEntityToCardGetDTO(card);
    }
}
