package pl.smyk.gympassbackend.gymPass.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.smyk.gympassbackend.gymPass.model.Membership;

@Repository
public interface MembershipRepository extends MongoRepository<Membership, String> {

}
