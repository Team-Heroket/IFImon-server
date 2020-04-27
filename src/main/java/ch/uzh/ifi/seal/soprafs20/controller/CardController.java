package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.rest.dto.CardGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.CardService;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class CardController {

    private final UserService userService;
    private final CardService cardService;

    CardController(UserService userService, CardService cardService) {
        this.userService = userService;
        this.cardService = cardService;
    }

    /*     #14     */
    /** This request returns a complete pkm-card **/

    @GetMapping("/cards/{pokemonId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public CardGetDTO getCard(@PathVariable String pokemonId, @RequestHeader("Token") String token) {
        //convert pathvaraible to long
        int cardId = Integer.parseInt(pokemonId);

        // If uyer is allowed to do that call
        this.userService.validateUser(token);

        //get card from service
        Card card = cardService.getCard(cardId);

        return DTOMapper.INSTANCE.convertEntityToCardGetDTO(card);
    }
}
