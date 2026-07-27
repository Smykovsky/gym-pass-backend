package pl.smyk.gympassbackend.contest.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.smyk.gympassbackend.contest.model.Contest;
import pl.smyk.gympassbackend.contest.model.ContestStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ContestRepository extends MongoRepository<Contest, String> {
    List<Contest> findAllByStatusOrderByEndDateAsc(ContestStatus status);
    List<Contest> findAllByOrderByCreatedAtDesc();
    List<Contest> findAllByStatusAndEndDateBefore(ContestStatus status, LocalDateTime now);
}
