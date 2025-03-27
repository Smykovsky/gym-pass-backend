package pl.smyk.gympassbackend.auth.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.smyk.gympassbackend.auth.model.Receptionist;
import pl.smyk.gympassbackend.auth.model.Trainer;

import java.util.Optional;

@Repository
public interface ReceptionistRepository extends MongoRepository<Receptionist, String> {
    Optional<Receptionist> findByEmail(String email);
}
