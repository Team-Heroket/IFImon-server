package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User newUser) {
        //set user object online
        newUser.setOnline(true);

        //check for duplicate usernames
        checkIfUserExists(newUser);

        //set creation date on user object
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDateTime now = LocalDateTime.now();
        newUser.setCreationDate(pattern.format(now));

        // saves the given entity but data is only persisted in the database once flush() is called
        newUser = userRepository.save(newUser);
        userRepository.flush();

        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public List<User> getUsers(String tokenInput){
        //checks if a valid user is requesting
        if(tokenInput==null){
            throw new SopraServiceException(String.format("Bad request"));
        }
        return this.userRepository.findAll();
    }

    public User getUser(long id, String inputToken){
        //check if token is null
        if(inputToken==null){
            throw new SopraServiceException(String.format("Bad request"));
        }

        //check if optional object is empty
        Optional<User> optionalUser = this.userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new SopraServiceException("This user does not exist.");
        }

        //find the user with the given token
        User foundUser = optionalUser.get();
        if (foundUser==null){
            throw new SopraServiceException(String.format("User not found"));
        }
        return foundUser;
    }

    public void updateUser(User user){
        //check if optional object is empty
        Optional<User> optionalUser = this.userRepository.findById(user.getId());
        if (optionalUser.isEmpty()) {
            throw new SopraServiceException("This user does not exist.");
        }

        //check if optional object is empty
        if (optionalUser.isEmpty()){
            throw new SopraServiceException(String.format("Updating user not found"));
        }
        User changingUser = optionalUser.get();

        //change username
        changingUser.setUsername(user.getUsername());

        //change password
        changingUser.setPassword(user.getPassword());

        //change profile picture
        changingUser.setAvatarId(user.getAvatarId());
        userRepository.save(changingUser);
    }

    public User logUserIn(User user){
        //find the user in the data base
        User loggingUser = userRepository.findByUsername(user.getUsername());

        if (loggingUser==null){
            throw new SopraServiceException(String.format("User not found"));
        }

        //check password of the found user
        if(!(user.getPassword().equals(loggingUser.getPassword()))){
            throw new SopraServiceException(String.format("Wrong password"));
        }

        //set user online and create new token
        loggingUser.setToken(UUID.randomUUID().toString());
        loggingUser.setOnline(true);
        loggingUser = userRepository.save(loggingUser);

        return loggingUser;
    }

    public void logUserOut(String userToken){
        //checks if token is null
        if(userToken==null){
            throw new SopraServiceException(String.format("Bad request"));
        }
        //get user, who wants to log out
        User departingUser = userRepository.findByToken(userToken);

        //set user offline and set his token to NULL
        departingUser.setToken(null);
        departingUser.setOnline(false);
        departingUser = userRepository.save(departingUser);
    }

    public boolean compareHeaderWithUser(User userInput, String tokenInput){
        //check if optional object is empty
        Optional<User> optionalUser = this.userRepository.findById(userInput.getId());
        if (optionalUser.isEmpty()) {
            throw new SopraServiceException("This user does not exist.");
        }

        User chosenUser = optionalUser.get();
        //extract the user's x token
        String originalToken = chosenUser.getToken();

        //checks if token is null
        if(tokenInput==null){
            throw new SopraServiceException(String.format("Unauthorized user access"));
        }
        //checks if the header token is identical to the correct user token
        if(originalToken.equals(tokenInput)){
            return true;
        }
        throw new SopraServiceException(String.format("Unauthorized user access"));
    }

    /**
     * This is a helper method that will check the uniqueness criteria of the username and the name
     * defined in the User entity. The method will do nothing if the input is unique and throw an error otherwise.
     *
     * @param userToBeCreated
     * @throws org.springframework.web.server.ResponseStatusException
     * @see User
     */
    private void checkIfUserExists(User userToBeCreated) {
        User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());

        String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
        if (userByUsername != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "username", "is"));
        }
    }
}
