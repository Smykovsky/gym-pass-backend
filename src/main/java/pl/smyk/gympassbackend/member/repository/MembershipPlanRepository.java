package pl.smyk.gympassbackend.member.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.smyk.gympassbackend.member.model.MembershipPlan;

import java.util.List;
import java.util.Optional;

@Repository
public interface MembershipPlanRepository extends MongoRepository<MembershipPlan, String> {

    Optional<MembershipPlan> findByCode(String code);

    boolean existsByCode(String code);

    List<MembershipPlan> findAllByActiveTrueOrderBySortOrderAscMonthsAsc();

    List<MembershipPlan> findAllByOrderBySortOrderAscMonthsAsc();
}
