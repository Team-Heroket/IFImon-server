package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.exceptions.user.UserUnauthorizedException;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        // given
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUsername");
        testUser.setPassword("loginPW");
        testUser.setToken("12345");

        // when -> any object is being save in the userRepository -> return the dummy testUser
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
    }

    @Test
    public void createUser_validInputs_success() {
        // when
        userService.createUser(testUser);

        // then
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void createUser_duplicateUsername_throwsException() {
        // given -> a first user has already been created
        userService.createUser(testUser);

        // when -> setup additional mocks for UserRepository
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

        // then -> attempt to create second user with same user -> check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
    }

    @Test
    public void Test_getUsers() {
        //given
        List<User> myUsers = new ArrayList<>();
        myUsers.add(testUser);

        //when
        Mockito.when(userRepository.findAll()).thenReturn(myUsers);

        // then compare returned list
        assertEquals(userService.getUsers("123").get(0).getUsername(), testUser.getUsername());
    }

    @Test
    public void Test_getUser(){
        // given
        Long schlong = 3L;
        Optional<User> opList = Optional.of(testUser);;

        // when
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(opList);

        // then compare
        Optional<User> optionalUser = userRepository.findById(schlong);
        User foundUser = optionalUser.get();

        assertEquals(foundUser.getUsername(),testUser.getUsername());

    }

    @Test
    public void Test_getUpdateUser(){
        // given
        User nullUser = new User();
        nullUser.setId(1L);
        Optional<User> opList = Optional.of(testUser);;

        // when
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(opList);

        // then
        userService.updateUser(nullUser);
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void Test_logUserOut(){
        //when
        Mockito.when(userRepository.findByToken(Mockito.any())).thenReturn(testUser);

        //then
        userService.logUserOut("testToken");
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void Test_logUserIn_validInputs(){
        //when
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

        //then
        User returnedUser = userService.logUserIn(testUser);
        assertEquals(testUser.getUsername(),returnedUser.getUsername());
        assertTrue(returnedUser.getOnline());
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void Test_logUserIn_invalidInputs(){
        //given another testaccount with changed password
        User testUser2 = new User();
        testUser2.setId(1L);
        testUser2.setUsername("testUsername");
        testUser2.setPassword("loginPW2");
        testUser2.setToken("12345");

        //when
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

        //then
        // TODO: @David please check if this is what you wanted
        assertThrows(UserUnauthorizedException.class, () -> userService.logUserIn(testUser2));
    }

    @Test
    public void Test_compareIdWithToken(){
        //given
        Optional<User> opList = Optional.of(testUser);;

        //when
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(opList);

        //then
        userService.compareIdWithToken(1L,"12345");
        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.any());
    }

}
