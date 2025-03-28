package pl.smyk.gympassbackend.auth.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pl.smyk.gympassbackend.auth.model.Trainer;
import pl.smyk.gympassbackend.auth.dto.TrainerDto;

@Mapper(componentModel = "spring")
public interface TrainerMapper {

    TrainerMapper INSTANCE = Mappers.getMapper(TrainerMapper.class);

    TrainerDto trainerToTrainerDto(Trainer trainer);
}