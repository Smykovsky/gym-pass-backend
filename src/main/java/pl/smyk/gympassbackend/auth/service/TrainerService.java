package pl.smyk.gympassbackend.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.smyk.gympassbackend.auth.dto.TrainerDto;
import pl.smyk.gympassbackend.auth.dto.UserDto;
import pl.smyk.gympassbackend.auth.exception.UserNotFoundException;
import pl.smyk.gympassbackend.auth.mapper.TrainerMapper;
import pl.smyk.gympassbackend.auth.mapper.UserMapper;
import pl.smyk.gympassbackend.auth.model.Trainer;
import pl.smyk.gympassbackend.auth.repository.TrainerRepository;
import pl.smyk.gympassbackend.training.model.ScheduledTraining;
import pl.smyk.gympassbackend.training.model.TrainingStatus;
import pl.smyk.gympassbackend.training.repository.ScheduledTrainingRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TrainerService {
    private final TrainerRepository trainerRepository;
    private final ScheduledTrainingRepository scheduledTrainingRepository;

    //find trainer by email
    public Trainer findByEmail(String email) {
        return trainerRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
    }

    //find trainer by id
    public List<TrainerDto> getAllTrainers() {
        List<Trainer> all = trainerRepository.findAllTrainers();
        if (all.isEmpty()) {
            return new ArrayList<>();
        }
        return all.stream().map(TrainerMapper.INSTANCE::trainerToTrainerDto).toList();
    }

    public boolean isTrainerAvailable(String trainerEmail, LocalDateTime dateTime) {
        Trainer trainer = trainerRepository.findByEmail(trainerEmail)
                .orElseThrow(() -> new UserNotFoundException("Trainer not found!"));

        List<ScheduledTraining> allByTrainer = scheduledTrainingRepository.findAllByTrainer(trainer);

        if (allByTrainer.isEmpty()) {
            return true;
        }

        LocalDate today = LocalDate.now();
        if (dateTime.toLocalDate().isBefore(today)) {
            return false;
        }

        return allByTrainer.stream()
                .noneMatch(training -> {
                    LocalDateTime trainingStart = training.getTrainingDateTime();
                    LocalDateTime trainingEnd = trainingStart.plusHours(1);

                    LocalDateTime allowedPrevious = trainingStart.minusHours(1);

                    return dateTime.isAfter(allowedPrevious) && dateTime.isBefore(trainingEnd);
                });
    }
}
