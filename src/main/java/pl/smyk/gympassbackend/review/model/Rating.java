package pl.smyk.gympassbackend.review.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import pl.smyk.gympassbackend.auth.model.User;

@Document("rating")
@TypeAlias("rating")
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class Rating {
    @MongoId
    private String id;
    @Min(value = 1)
    @Max(value = 5)
    private int rating;
    @DBRef
    private User user;
}
