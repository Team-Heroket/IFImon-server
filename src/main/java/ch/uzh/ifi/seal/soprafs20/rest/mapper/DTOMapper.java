package ch.uzh.ifi.seal.soprafs20.rest.mapper;

import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.constant.*;
import ch.uzh.ifi.seal.soprafs20.rest.dto.*;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g., UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for creating information (POST).
 */
@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);
    @Mapping(source = "avatarId", target = "avatarId")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "username", target = "username")
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);


    @Mapping(source = "username", target = "username")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "avatarId", target = "avatarId")
    @Mapping(source = "creationDate", target = "creationDate")
    @Mapping(source = "statistics", target = "statistics")
    @Mapping(source = "online", target = "online")
    @Mapping(source = "npc", target = "npc")
    @Mapping(source = "seenTutorial", target = "seenTutorial")
    UserGetDTO convertEntityToUserGetDTO(User user);


    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "avatarId", target = "avatarId")
    @Mapping(source = "token", target = "token")
    @Mapping(source = "seenTutorial", target = "seenTutorial")
    User convertUserPutDTOtoEntity(UserPutDTO userPutDTO);

    @Mapping(source = "pokemonId", target = "pokemonId")
    @Mapping(source = "categories", target = "categories")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "spriteURL", target = "spriteURL")
    @Mapping(source = "cryURL", target = "cryURL")
    @Mapping(source = "elements", target = "elements")
    @Mapping(source = "evolutionNames", target = "evolutionNames")
    CardGetDTO convertEntityToCardGetDTO(Card card);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "token", target = "token")
    UserLoginDTO convertEntityToUserLoginDTO(User user);


    @Mapping(source = "gameName", target = "gameName")
    @Mapping(source = "mode", target = "mode")
    Game convertGamePostDTOToEntity(GamePostDTO gamePostDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "deck", target = "deck")
    @Mapping(source = "berries", target = "berries")
    @Mapping(source = "emote", target = "emote")
    PlayerDTO convertEntityToPlayerDTO(Player player);


    @Mapping(source = "id", target = "id")
    @Mapping(source = "gameName", target = "gameName")
    @Mapping(source = "token", target = "token")
    @Mapping(source = "creator", target = "creator")
    @Mapping(source = "players", target = "players")
    @Mapping(source = "mode", target = "mode")
    @Mapping(source = "state", target = "state")
    @Mapping(source = "creationTime", target = "creationTime")
    @Mapping(source = "turnPlayer", target = "turnPlayer")
    @Mapping(source = "startTime", target = "startTime")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "winners", target = "winners")

    GameGetDTO convertEntityToGameGetDTO(Game game);

    @Mapping(source = "token", target = "token")
    GameTokenDTO convertEntityToGameTokenDTO(Game game);



}
