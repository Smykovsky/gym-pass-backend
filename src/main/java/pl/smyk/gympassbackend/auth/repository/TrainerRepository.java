package pl.smyk.gympassbackend.auth.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.smyk.gympassbackend.auth.model.Trainer;
import pl.smyk.gympassbackend.training.model.ScheduledTraining;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends MongoRepository<Trainer, String> {
    Optional<Trainer> findById(String id);
    Optional<Trainer> findByEmail(String email);
}
