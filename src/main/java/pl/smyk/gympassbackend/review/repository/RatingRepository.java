package pl.smyk.gympassbackend.review.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.smyk.gympassbackend.auth.model.User;
import pl.smyk.gympassbackend.review.model.Rating;

@Repository
public interface RatingRepository extends MongoRepository<Rating, String> {
    public Rating findByUser(User user);
}
