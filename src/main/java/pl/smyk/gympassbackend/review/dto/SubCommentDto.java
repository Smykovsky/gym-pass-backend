package pl.smyk.gympassbackend.review.dto;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SubCommentDto {
    @Nullable
    private String id;
    private String commentText;
    private String userEmail;
    private String parentCommentId;
}
