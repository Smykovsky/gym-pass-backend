package pl.smyk.gympassbackend.training.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.smyk.gympassbackend.auth.service.AuthService;
import pl.smyk.gympassbackend.training.repository.ScheduledTrainingRepository;

@Service
@RequiredArgsConstructor
public class TrainingService {
    private final ScheduledTrainingRepository scheduledTrainingRepository;
    private final AuthService authService;

    //get user training list

    //send training request


}
