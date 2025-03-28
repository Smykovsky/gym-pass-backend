package pl.smyk.gympassbackend.training.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.smyk.gympassbackend.auth.model.Trainer;
import pl.smyk.gympassbackend.auth.repository.TrainerRepository;
import pl.smyk.gympassbackend.auth.service.TrainerService;
import pl.smyk.gympassbackend.training.dto.ScheduledTrainingDto;
import pl.smyk.gympassbackend.training.dto.TrainingActionRequest;
import pl.smyk.gympassbackend.training.exception.TrainingNotFoundException;
import pl.smyk.gympassbackend.training.exception.UnauthorizedActionException;
import pl.smyk.gympassbackend.training.mapper.ScheduledTrainingMapper;
import pl.smyk.gympassbackend.training.model.ScheduledTraining;
import pl.smyk.gympassbackend.training.model.TrainingStatus;
import pl.smyk.gympassbackend.training.repository.ScheduledTrainingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainerTrainingService {
    private final TrainerRepository trainerRepository;
    private final TrainerService trainerService;
    private final ScheduledTrainingRepository scheduledTrainingRepository;

    public List<ScheduledTrainingDto> getTrainerTrainings(String trainerEmail) {
        Trainer trainer = trainerService.findByEmail(trainerEmail);

        List<ScheduledTraining> trainings = scheduledTrainingRepository.findAllByTrainer(trainer);

        return trainings.stream()
                .map(ScheduledTrainingMapper.INSTANCE::toDto)
                .toList();
    }

    //accept training request
    @Transactional
    public ScheduledTrainingDto acceptTrainingRequest(TrainingActionRequest request) {
        ScheduledTraining scheduledTraining = scheduledTrainingRepository.findById(request.getTrainingId())
                .orElseThrow(() -> new TrainingNotFoundException("Training with ID: " + request.getTrainingId() + " not found"));

        if (!scheduledTraining.getTrainer().getEmail().equals(request.getTrainerEmail())) {
            throw new UnauthorizedActionException("Trainer: " + request.getTrainerEmail() + " is not authorized to accept this request!");
        }

        Optional<Trainer> byEmail = trainerRepository.findByEmail(request.getTrainerEmail());
        if (byEmail.isEmpty()) {
            return null;
        }

        Trainer trainer = byEmail.get();

        trainerRepository.save(trainer);
        scheduledTraining.setTrainingStatus(TrainingStatus.CONFIRMED);
        ScheduledTraining saved = scheduledTrainingRepository.save(scheduledTraining);
        return ScheduledTrainingMapper.INSTANCE.toDto(saved);
    }

    //decline training request
    @Transactional
    public ScheduledTrainingDto declineTraining(TrainingActionRequest request) {
        ScheduledTraining scheduledTraining = scheduledTrainingRepository.findById(request.getTrainingId())
                .orElseThrow(() -> new TrainingNotFoundException("Training with ID " + request.getTrainingId() + " not found"));

        if (!scheduledTraining.getTrainer().getEmail().equals(request.getTrainerEmail())) {
            throw new UnauthorizedActionException("Trainer is not authorized to decline this training");
        }

        scheduledTraining.setTrainingStatus(TrainingStatus.DECLINED);
        scheduledTraining.setRejectionReason(request.getRejectionReason());

        ScheduledTraining saved = scheduledTrainingRepository.save(scheduledTraining);
        return ScheduledTrainingMapper.INSTANCE.toDto(saved);

    }

    //get not available days/datetime
    public List<LocalDateTime> getOccupiedDatesForTrainer(String trainerEmail) {
        Trainer trainer = trainerRepository.findByEmail(trainerEmail).orElse(null);

        List<ScheduledTraining> trainings = scheduledTrainingRepository.findAllByTrainer(trainer);
        return trainings.stream()
                .map(ScheduledTraining::getTrainingDateTime)
                .collect(Collectors.toList());
    }



}
