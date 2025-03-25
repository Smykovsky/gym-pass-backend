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
    CommentDto commentToCommentDto(Comment comment);

    @Mapping(source = "userEmail", target = "user.email")
    Comment commentDtoToComment(CommentDto commentDto);
}