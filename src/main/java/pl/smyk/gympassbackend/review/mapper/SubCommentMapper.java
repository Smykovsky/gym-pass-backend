package pl.smyk.gympassbackend.review.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import pl.smyk.gympassbackend.review.dto.SubCommentDto;
import pl.smyk.gympassbackend.review.model.SubComment;

@Mapper
public interface SubCommentMapper {
    SubCommentMapper INSTANCE = Mappers.getMapper(SubCommentMapper.class);

    @Mapping(source = "user.email", target = "userEmail")
    @Mapping(source = "parentCommentId", target = "parentCommentId")
    SubCommentDto subCommentToSubCommentDto(SubComment subComment);

    @Mapping(source = "userEmail", target = "user.email")
    @Mapping(source = "parentCommentId", target = "parentCommentId")
    SubComment subCommentDtoToSubComment(SubCommentDto subCommentDto);
}