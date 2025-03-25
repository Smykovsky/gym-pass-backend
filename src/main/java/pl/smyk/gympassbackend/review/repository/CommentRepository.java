package pl.smyk.gympassbackend.review.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.smyk.gympassbackend.review.model.Comment;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
}
