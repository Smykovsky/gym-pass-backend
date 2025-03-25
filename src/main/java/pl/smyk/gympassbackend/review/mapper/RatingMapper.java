package pl.smyk.gympassbackend.review.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import pl.smyk.gympassbackend.review.dto.RatingDto;
import pl.smyk.gympassbackend.review.model.Rating;

@Mapper
public interface RatingMapper {

    RatingMapper INSTANCE = Mappers.getMapper(RatingMapper.class);

    @Mapping(source = "user.email", target = "userEmail")
    RatingDto ratingToRatingDto(Rating rating);

    @Mapping(source = "userEmail", target = "user.email")
    Rating ratingDtoToRating(RatingDto ratingDto);
}