package pl.smyk.gympassbackend.auth.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.smyk.gympassbackend.atrainer.model.Trainer;
import pl.smyk.gympassbackend.auth.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}
