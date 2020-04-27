package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.exceptions.user.UserNotFoundException;
import ch.uzh.ifi.seal.soprafs20.exceptions.user.UserUnauthorizedException;
import ch.uzh.ifi.seal.soprafs20.repository.*;
import ch.uzh.ifi.seal.soprafs20.constant.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@SpringBootTest
public class UserServiceIntegrationTest {

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }


    @Test
    public void createUser_validInputs_success() {
        // given
        assertNull(userRepository.findByUsername("testUsername"));

        //a testuser with essential parameteres assigned
        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setAvatarId(5);
        testUser.setPassword("passWordTest");

        // when
        userService.createUser(testUser);
        User returnedUser = userRepository.findByUsername("testUser");

        assertEquals(returnedUser.getAvatarId(),testUser.getAvatarId());
        assertEquals(returnedUser.getUsername(),testUser.getUsername());
        assertEquals(returnedUser.getPassword(),testUser.getPassword());
    }


    @Test
    public void createUser_duplicateUsername_throwsException() {
        //given
        assertNull(userRepository.findByUsername("testUsername"));

        //a testuser with essential parameteres assigned
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setAvatarId(5);
        testUser.setPassword("passWordTest");

        // create testUser
        userService.createUser(testUser);

        // attempt to create second user with same username
        User testUser2 = new User();

        // change the password and avatarId but forget about the username
        testUser2.setUsername("testUsername");
        testUser2.setAvatarId(2);
        testUser2.setPassword("passWordTest2");

        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser2));
    }

    @Test
    public void loginUser_validInputs_success() {
        //a testuser with essential parameteres assigned
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setAvatarId(5);
        testUser.setPassword("passWordTest");

        // create testUser
        userService.createUser(testUser);

        // login user and get the returned logged in user
        User loggedInUser = userService.logUserIn(testUser);

        // check if loggedInUser is correct
        assertTrue(loggedInUser.getOnline());
        assertEquals(loggedInUser.getUsername(),testUser.getUsername());
        assertEquals(loggedInUser.getAvatarId(),testUser.getAvatarId());
    }

    @Test
    public void loginUser_nonexistingUser_throwException() {
        //a testuser with essential parameteres assigned
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setAvatarId(5);
        testUser.setPassword("passWordTest");

        // de DON'T create the user

        // check exception
        assertThrows(UserNotFoundException.class, () -> userService.logUserIn(testUser));
    }

    @Test
    public void loginUser_invalidPassword_throwException() {
        //a testuser with essential parameteres assigned
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setAvatarId(5);
        testUser.setPassword("passWordTest");

        // create testUser
        userService.createUser(testUser);

        // change our testUser's password
        testUser.setPassword("wrongPassword");

        // check exception
        assertThrows(UserUnauthorizedException.class, () -> userService.logUserIn(testUser));
    }


}
