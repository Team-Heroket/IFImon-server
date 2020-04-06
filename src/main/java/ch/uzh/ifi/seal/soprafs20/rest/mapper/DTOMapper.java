package ch.uzh.ifi.seal.soprafs20.rest.mapper;

import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.objects.*;
import ch.uzh.ifi.seal.soprafs20.rest.dto.*;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

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
    UserGetDTO convertEntityToUserGetDTO(User user);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "avatarId", target = "avatarId")
    @Mapping(source = "token", target = "token")
    User convertUserPutDTOtoEntity(UserPutDTO userPutDTO);

    @Mapping(source = "name", target = "name")
    CardGetDTO convertEntityToCardGetDTO(Card card);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "token", target = "token")
    UserLoginDTO convertEntityToUserLoginDTO(User user);


    @Mapping(source = "gameName", target = "gameName")
    @Mapping(source = "mode", target = "mode")
    Game convertGamePostDTOToEntity(GamePostDTO gamePostDTO);

    @Mapping(source = "players", target = "players")
    @Mapping(source = "turnPlayer", target = "turnPlayer")
    @Mapping(source = "chosenCategory", target = "chosenCategory")
    @Mapping(source = "timer", target = "timer")
    BoardGetDTO convertBoardToBoardGetDTO(Board board);

}
