package pl.smyk.gympassbackend.training.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import pl.smyk.gympassbackend.auth.model.Trainer;
import pl.smyk.gympassbackend.auth.model.User;
import pl.smyk.gympassbackend.training.dto.ScheduledTrainingDto;
import pl.smyk.gympassbackend.training.model.ScheduledTraining;
import pl.smyk.gympassbackend.training.model.TrainingStatus;
import pl.smyk.gympassbackend.training.model.TrainingType;

@Mapper(componentModel = "spring")
public interface ScheduledTrainingMapper {
    ScheduledTrainingMapper INSTANCE = Mappers.getMapper(ScheduledTrainingMapper.class);

    @Mapping(source = "trainer.email", target = "trainerEmail")
    @Mapping(source = "user.email", target = "userEmail")
    @Mapping(source = "trainingType", target = "trainingType", qualifiedByName = "mapTrainingTypeToString")
    @Mapping(source = "trainingStatus", target = "trainingStatus", qualifiedByName = "mapTrainingStatusToString")
    ScheduledTrainingDto toDto(ScheduledTraining scheduledTraining);

    @Mapping(source = "trainerEmail", target = "trainer", qualifiedByName = "mapTrainerEmail")
    @Mapping(source = "userEmail", target = "user", qualifiedByName = "mapUserEmail")
    @Mapping(source = "trainingType", target = "trainingType", qualifiedByName = "mapStringToTrainingType")
    @Mapping(source = "trainingStatus", target = "trainingStatus", qualifiedByName = "mapStringToTrainingStatus")
    ScheduledTraining toEntity(ScheduledTrainingDto scheduledTrainingDto);

    @Named("mapTrainerEmail")
    default Trainer mapTrainerEmail(String email) {
        if (email == null) return null;
        Trainer trainer = new Trainer();
        trainer.setEmail(email);
        return trainer;
    }

    @Named("mapUserEmail")
    default User mapUserEmail(String email) {
        if (email == null) return null;
        User user = new User();
        user.setEmail(email);
        return user;
    }

    @Named("mapTrainingTypeToString")
    default String mapTrainingTypeToString(TrainingType trainingType) {
        return trainingType != null ? trainingType.getValue() : null;
    }

    @Named("mapStringToTrainingType")
    default TrainingType mapStringToTrainingType(String value) {
        if (value == null) return null;
        for (TrainingType type : TrainingType.values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Nieznany typ treningu: " + value);
    }

    @Named("mapTrainingStatusToString")
    default String mapTrainingStatusToString(TrainingStatus trainingStatus) {
        return trainingStatus != null ? trainingStatus.getValue() : null;
    }

    @Named("mapStringToTrainingStatus")
    default TrainingStatus mapStringToTrainingStatus(String value) {
        if (value == null) return null;
        for (TrainingStatus status : TrainingStatus.values()) {
            if (status.getValue().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Nieznany typ treningu: " + value);
    }
}