package pl.smyk.gympassbackend.atrainer.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.smyk.gympassbackend.atrainer.model.Trainer;

import java.util.Optional;

@Repository
public interface TrainerRepository extends MongoRepository<Trainer, String> {
    Optional<Trainer> findById(String id);
}
