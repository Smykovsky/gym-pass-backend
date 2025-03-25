package pl.smyk.gympassbackend.review.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.smyk.gympassbackend.review.model.SubComment;

@Repository
public interface SubCommentRepository extends MongoRepository<SubComment, String> {
}
