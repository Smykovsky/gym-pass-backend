package pl.smyk.gympassbackend.auth.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.smyk.gympassbackend.auth.model.Receptionist;

@Repository
public interface ReceptionistRepository extends MongoRepository<Receptionist, String> {
}
