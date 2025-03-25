package pl.smyk.gympassbackend.review.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import pl.smyk.gympassbackend.auth.model.User;

@Document("subComment")
@TypeAlias("subComment")
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class SubComment {
    @MongoId
    private String id;
    private String commentText;
    @DBRef
    private User user;
    private String parentCommentId;
}
