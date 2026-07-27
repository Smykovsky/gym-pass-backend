package pl.smyk.gympassbackend.training.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.smyk.gympassbackend.training.model.WorkoutPlan;
import pl.smyk.gympassbackend.training.model.WorkoutStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutPlanRepository extends MongoRepository<WorkoutPlan, String> {
    List<WorkoutPlan> findAllByTrainerIdOrderByCreatedAtDesc(String trainerId);
    List<WorkoutPlan> findAllByAssignedUserIdOrderByCreatedAtDesc(String userId);
    Optional<WorkoutPlan> findFirstByAssignedUserIdAndStatusOrderByCreatedAtDesc(String userId, WorkoutStatus status);
}
