package pl.smyk.gympassbackend.auth.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import pl.smyk.gympassbackend.auth.model.Trainer;
import pl.smyk.gympassbackend.auth.dto.TrainerDto;
import pl.smyk.gympassbackend.training.model.TrainingType;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrainerMapper {

    TrainerMapper INSTANCE = Mappers.getMapper(TrainerMapper.class);

    @Mapping(source = "certifications", target = "certifications")
    @Mapping(source = "trainingTypes", target = "trainingTypes")
    @Mapping(source = "membership", target = "membership")
    @Mapping(source = "profileImage", target = "profileImage")
    TrainerDto trainerToTrainerDto(Trainer trainer);

    default List<String> mapTrainingTypes(List<TrainingType> trainingTypes) {
        return trainingTypes != null ? trainingTypes.stream().map(TrainingType::name).toList() : null;
    }
}