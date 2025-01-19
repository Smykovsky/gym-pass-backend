package pl.smyk.gympassbackend.training.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.smyk.gympassbackend.auth.model.Trainer;
import pl.smyk.gympassbackend.auth.model.User;
import pl.smyk.gympassbackend.auth.repository.TrainerRepository;
import pl.smyk.gympassbackend.auth.service.AuthService;
import pl.smyk.gympassbackend.training.dto.NewTrainingRequest;
import pl.smyk.gympassbackend.training.dto.ScheduledTrainingDto;
import pl.smyk.gympassbackend.training.mapper.ScheduledTrainingMapper;
import pl.smyk.gympassbackend.training.model.ScheduledTraining;
import pl.smyk.gympassbackend.training.model.TrainingStatus;
import pl.smyk.gympassbackend.training.repository.ScheduledTrainingRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingService {
    private final ScheduledTrainingRepository scheduledTrainingRepository;
    private final AuthService authService;
    private final TrainerRepository trainerRepository;

    //get user training list
    public List<ScheduledTrainingDto> getUserTrainings(String userEmail) {
        if (userEmail.isEmpty()) {
            return null;
        }

        Optional<User> byEmail = authService.findByEmail(userEmail);
        if (!byEmail.isPresent()) {
            return null;
        }

        User user = byEmail.get();
        List<ScheduledTraining> allUserTrainings = scheduledTrainingRepository.findAllByUser(user);

        return allUserTrainings.stream()
                .map(ScheduledTrainingMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }


    //get user training by id
    public ScheduledTrainingDto getUserTrainingById(String userEmail, String trainingId) {
      if (userEmail.isEmpty()) {
        return null;
      }

      Optional<User> byEmail = authService.findByEmail(userEmail);
      if (byEmail.isEmpty()) {
        return null;
      }

      User user = byEmail.get();
      List<ScheduledTraining> allByUser = scheduledTrainingRepository.findAllByUser(user);
      Optional<ScheduledTraining> first = allByUser.stream().filter(scheduledTraining -> scheduledTraining.getId().equals(trainingId)).findFirst();

      if (first.isEmpty()) {
        return null;
      }
      ScheduledTraining scheduledTraining = first.get();
      return ScheduledTrainingMapper.INSTANCE.toDto(scheduledTraining);
    }

    //send training request
    public ScheduledTrainingDto sendTrainingRequest(NewTrainingRequest request, String userEmail) {
        Optional<Trainer> trainerByEmail = trainerRepository.findByEmail(request.getTrainerEmail());

        Trainer trainer = trainerByEmail.orElse(null);
        User user = authService.findByEmail(userEmail).orElse(null);

        ScheduledTraining newTraining = ScheduledTraining.builder().trainingDateTime(LocalDateTime.now()).trainer(trainer).user(user).trainingType(request.getTrainingType()).trainingStatus(TrainingStatus.WAITING).build();
        ScheduledTraining saved = scheduledTrainingRepository.save(newTraining);
        return ScheduledTrainingMapper.INSTANCE.toDto(saved);
    }

    //cancel training by user
    public void cancelTraining(String userEmail, String id) {
        Optional<User> byEmail = authService.findByEmail(userEmail);
        if (byEmail.isEmpty()) {
            return;
        }

        User user = byEmail.get();
        List<ScheduledTraining> allByUser = scheduledTrainingRepository.findAllByUser(user);
        Optional<ScheduledTraining> first = allByUser.stream().filter(scheduledTraining -> scheduledTraining.getId().equals(id)).findFirst();
        if (first.isEmpty()) {
            return;
        }

        scheduledTrainingRepository.delete(first.get());
    }


}
