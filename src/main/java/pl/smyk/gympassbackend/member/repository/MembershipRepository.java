package pl.smyk.gympassbackend.member.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.smyk.gympassbackend.member.model.Membership;

@Repository
public interface MembershipRepository extends MongoRepository<Membership, String> {

}
