package pl.smyk.gympassbackend.review.dto;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class RatingDto {
    @Nullable
    private String id;
    private int rating;
    private String userEmail;
}
