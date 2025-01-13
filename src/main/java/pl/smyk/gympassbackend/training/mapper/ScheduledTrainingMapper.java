package pl.smyk.gympassbackend.training.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import pl.smyk.gympassbackend.auth.model.Trainer;
import pl.smyk.gympassbackend.auth.model.User;
import pl.smyk.gympassbackend.training.dto.ScheduledTrainingDto;
import pl.smyk.gympassbackend.training.model.ScheduledTraining;

@Mapper(componentModel = "spring")
public interface ScheduledTrainingMapper {
    ScheduledTrainingMapper INSTANCE = Mappers.getMapper(ScheduledTrainingMapper.class);

    @Mapping(source = "trainer.email", target = "trainerEmail")
    @Mapping(source = "user.email", target = "userEmail")
    ScheduledTrainingDto toDto(ScheduledTraining scheduledTraining);

    @Mapping(source = "trainerEmail", target = "trainer", qualifiedByName = "mapTrainerEmail")
    @Mapping(source = "userEmail", target = "user", qualifiedByName = "mapUserEmail")
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
}
