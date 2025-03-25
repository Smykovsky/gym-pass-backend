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

import java.util.List;


@Document("comment")
@TypeAlias("comment")
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class Comment {
    @MongoId
    private String id;
    private String commentText;
    @DBRef
    private User user;
    @DBRef
    private List<SubComment> subComments;

    public void addSubComment(SubComment subComment) {
        this.subComments.add(subComment);
    }

    public void removeSubComment(SubComment subComment) {
        this.subComments.remove(subComment);
    }
}
