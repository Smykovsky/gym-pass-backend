package pl.smyk.gympassbackend.member.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.smyk.gympassbackend.member.model.Entry;

import java.util.List;

@Repository
public interface EntryRepository extends MongoRepository<Entry, String> {
    List<Entry> findAllByUserEmail(String userEmail);
}
