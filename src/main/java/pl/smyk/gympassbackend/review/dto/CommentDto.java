package pl.smyk.gympassbackend.review.dto;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class CommentDto {
    @Nullable
    private String id;
    private String commentText;
    private String userEmail;
    private List<SubCommentDto>subComments;
}
