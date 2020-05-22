package ch.uzh.ifi.seal.soprafs20.controller;


import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.repository.CardRepository;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PokeAPICacheRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs20.service.CardService;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.PokeAPICacheService;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardController.class)
public class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @Mock
    private GameRepository gameRepository;

    @MockBean
    private GameService gameService;

    @Mock
    private CardRepository cardRepository;

    @MockBean
    private CardService cardService;

    @Mock
    private PokeAPICacheRepository pokeAPICacheRepository;

    @MockBean
    private PokeAPICacheService pokeAPICacheService;


    @Test
    public void Test_getCard() throws Exception {
        // given
        User testUser = new User();
        Card card = new Card("ivysaur");


        // this mocks the cardservice
        given(cardService.getCard(Mockito.anyInt())).willReturn(card);
        given(userRepository.findByToken(Mockito.any())).willReturn(testUser);


        // when
        MockHttpServletRequestBuilder getRequest = get("/cards/2")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Token", "Test");

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(card.getName())));
    }


}
