package pl.smyk.gympassbackend.training.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.smyk.gympassbackend.training.model.ScheduledTraining;

@Repository
public interface ScheduledTrainingRepository extends MongoRepository<ScheduledTraining, String> {
}
