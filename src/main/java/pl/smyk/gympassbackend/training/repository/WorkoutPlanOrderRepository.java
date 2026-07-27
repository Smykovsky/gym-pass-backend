package pl.smyk.gympassbackend.training.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.smyk.gympassbackend.training.model.OrderStatus;
import pl.smyk.gympassbackend.training.model.WorkoutPlanOrder;

import java.util.List;

@Repository
public interface WorkoutPlanOrderRepository extends MongoRepository<WorkoutPlanOrder, String> {
    List<WorkoutPlanOrder> findAllByUserIdOrderByCreatedAtDesc(String userId);
    List<WorkoutPlanOrder> findAllByTrainerIdOrderByCreatedAtDesc(String trainerId);
    List<WorkoutPlanOrder> findAllByTrainerIdAndStatusOrderByCreatedAtDesc(String trainerId, OrderStatus status);
}
