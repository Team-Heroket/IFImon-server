package ch.uzh.ifi.seal.soprafs20.rest.mapper;

import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.objects.Board;
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

    @Mapping(source = "username", target = "username")
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "token", target = "token")
    UserGetDTO convertEntityToUserGetDTO(User user);

    @Mapping(source = "username", target = "username")
    User convertUserPutDTOtoEntity(UserPutDTO userPutDTO);

    @Mapping(source = "timer", target = "timer")
    TableGetDTO convertEntityToTableGetDTO(Board board);

    @Mapping(source = "name", target = "name")
    CardGetDTO convertEntityToCardGetDTO(Card card);
}
