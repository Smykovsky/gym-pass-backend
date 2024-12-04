package pl.smyk.gympassbackend.areceptionist.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.smyk.gympassbackend.areceptionist.model.Receptionist;

@Repository
public interface ReceptionistRepository extends MongoRepository<Receptionist, String> {
}
