package pl.smyk.gympassbackend.training.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.smyk.gympassbackend.auth.model.Trainer;
import pl.smyk.gympassbackend.auth.model.User;
import pl.smyk.gympassbackend.training.model.ScheduledTraining;

import java.util.List;

@Repository
public interface ScheduledTrainingRepository extends MongoRepository<ScheduledTraining, String> {
    List<ScheduledTraining> findAllByUser(User user);
    List<ScheduledTraining> findAllByTrainer(Trainer trainer);


}
