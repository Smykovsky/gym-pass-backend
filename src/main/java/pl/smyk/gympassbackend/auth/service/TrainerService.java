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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TrainerService {
    private final TrainerRepository trainerRepository;

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

        if (trainer.getScheduledTrainings().isEmpty()) {
            return true;
        }

        return trainer.getScheduledTrainings().stream()
                .noneMatch(training -> {
                    LocalDateTime trainingStart = training.getTrainingDateTime();
                    LocalDateTime trainingEnd = trainingStart.plusHours(1);
                    return !dateTime.isBefore(trainingStart) && !dateTime.isAfter(trainingEnd.minusMinutes(1));
                });
    }
}
