package pl.smyk.gympassbackend.atrainer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.smyk.gympassbackend.atrainer.model.Trainer;
import pl.smyk.gympassbackend.atrainer.repository.TrainerRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainerService {
    private final TrainerRepository trainerRepository;

    Trainer findById(String id) {
        Optional<Trainer> byId = trainerRepository.findById(id);

        return byId.orElse(null);
    }
}
