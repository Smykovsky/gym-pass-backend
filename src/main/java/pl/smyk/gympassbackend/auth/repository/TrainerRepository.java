package pl.smyk.gympassbackend.auth.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import pl.smyk.gympassbackend.auth.model.Trainer;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends MongoRepository<Trainer, String> {
    Optional<Trainer> findById(String id);
    Optional<Trainer> findByEmail(String email);
    @Query("{ '_class' : 'trainer' }")
    List<Trainer> findAllTrainers();
}
