package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Statistics;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.repository.StatisticRepository;
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
    private UserRepository userRepository;
    private StatisticRepository statisticRepository;


    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository,
                       @Qualifier("statisticRepository") StatisticRepository statisticRepository) {
        this.userRepository = userRepository;
        this.statisticRepository = statisticRepository;
    }


    public void createUser(User newUser) {

        //check for duplicate usernames
        checkIfUserExists(newUser);

        //set user object offline
        newUser.setOnline(false);

        //set creation date on user object
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDateTime now = LocalDateTime.now();
        newUser.setCreationDate(pattern.format(now));

        //init statistics
        newUser.setStatistics(new Statistics());

        // saves the given entity
        newUser = userRepository.save(newUser);

        log.debug("Created Information for User: {}", newUser);
    }

    public List<User> getUsers(String tokenInput){
        //returns list of all users
        return this.userRepository.findAll();
    }

    public User getUser(long id){
        //get single user with this id
        return getUserById(id);
    }


    public void updateUser(User user){
        //get user by id
        User changingUser = getUserById(user.getId());

        //change username
        if (user.getUsername() != null){
            changingUser.setUsername(user.getUsername());
        }
        //change password
        if (user.getPassword() != null){
            changingUser.setPassword(user.getPassword());
        }
        //change avatarId
        if (user.getAvatarId() != null && !user.getAvatarId().equals(changingUser.getAvatarId())){
            changingUser.setAvatarId(user.getAvatarId());
        }

        /*
        // Changing ranting on user
        changingUser = userRepository.save(changingUser);

        Optional<Statistics> optionalStatistics = this.statisticRepository.findById(changingUser.getId());
        if (optionalStatistics.isEmpty()) {
            throw new SopraServiceException("Could not load statistics from user");
        }

        Statistics changingStatistics = optionalStatistics.get();

        changingStatistics.setRating(changingStatistics.getRating()+10);

        this.statisticRepository.save(changingStatistics);
         */

        userRepository.save(changingUser);
    }

    public User logUserIn(User user){
        //find the user in the data base
        User loggingUser = getUserByUsername(user.getUsername());

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

        //get user, who wants to log out
        User departingUser = userRepository.findByToken(userToken);

        //set user offline and set his token to NULL
        departingUser.setToken(null);
        departingUser.setOnline(false);
        departingUser = userRepository.save(departingUser);
    }

    /**
     * Checks if the User is the same user as shown in token
     *
     * @param id Id of user to be checked
     * @param token Token of user to be checked
     */
    public void compareIdWithToken(Long id, String token){
        //get all users with id
        Optional<User> optionalUser = this.userRepository.findById(id);

        //check if optional object is empty
        if (optionalUser.isEmpty()) {
            throw new SopraServiceException("This user does not exist.");
        }
        //get user from result
        User chosenUser = optionalUser.get();

        //extract the found user's token
        String originalToken = chosenUser.getToken();

        //check if the token of the user found by id is null, meaning he is offline
        if(originalToken==null){
            throw new SopraServiceException(String.format("Unauthorized user access"));
        }

        //checks if header token is null
        if(token==null){
            throw new SopraServiceException(String.format("Unauthorized user access"));
        }

        //checks if the header token is identical to the corresponding user token
        if(!originalToken.equals(token)){
            throw new SopraServiceException(String.format("Unauthorized user access"));
        }
    }

    /**
     * Check if this token is allowed to access
     *
     * @param token Token to check
     */
    public void validateUser(String token) {
        if(token == null || token.isEmpty()){
            throw new SopraServiceException("Bad request");
        }
        User isAuthorized = this.userRepository.findByToken(token);
        if (isAuthorized==null){
            throw new SopraServiceException("Unauthorized");
        }
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

    User getUserById(Long id){
        Optional<User> optionalUser = this.userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new SopraServiceException("This user does not exist.");
        }
        //get single user
        User foundUser = optionalUser.get();
        //check if it is null
        if(foundUser==null){
            throw new SopraServiceException("This user does not exist.");
        }
        return foundUser;
    }

    public User getUserByUsername(String username){
        User foundUser = this.userRepository.findByUsername(username);
        //check if it is null
        if(foundUser==null){
            throw new SopraServiceException("This user does not exist.");
        }
        return foundUser;
    }

    public User getUserByToken(String token) {
        User foundUser = this.userRepository.findByToken(token);
        //check if none found
        if(foundUser==null){
            throw new SopraServiceException("This user does not exist.");
        }
        return foundUser;
    }

}
