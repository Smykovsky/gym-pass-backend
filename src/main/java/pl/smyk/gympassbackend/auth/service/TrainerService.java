package pl.smyk.gympassbackend.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.smyk.gympassbackend.auth.exception.UserNotFoundException;
import pl.smyk.gympassbackend.auth.model.Trainer;
import pl.smyk.gympassbackend.auth.repository.TrainerRepository;

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

    //check is trainer available


}
