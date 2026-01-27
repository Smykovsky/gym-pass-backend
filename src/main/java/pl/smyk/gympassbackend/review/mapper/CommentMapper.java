package pl.smyk.gympassbackend.review.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import pl.smyk.gympassbackend.review.model.Comment;
import pl.smyk.gympassbackend.review.dto.CommentDto;

@Mapper
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(source = "user.email", target = "userEmail")
    @Mapping(source = "user.profileImage", target = "userProfileImage")
    @Mapping(source = "user.firstName", target = "userFirstName")
    @Mapping(source = "user.lastName", target = "userLastName")
    CommentDto commentToCommentDto(Comment comment);

    @Mapping(source = "userEmail", target = "user.email")
    @Mapping(source = "userProfileImage", target = "user.profileImage")
    @Mapping(source = "userFirstName", target = "user.firstName")
    @Mapping(source = "userLastName", target = "user.lastName")
    Comment commentDtoToComment(CommentDto commentDto);
}