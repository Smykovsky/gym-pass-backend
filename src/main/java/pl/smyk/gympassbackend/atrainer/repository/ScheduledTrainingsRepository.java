package pl.smyk.gympassbackend.atrainer.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.smyk.gympassbackend.atrainer.model.ScheduledTraining;

@Repository
public interface ScheduledTrainingsRepository extends MongoRepository<ScheduledTraining, String> {
}
