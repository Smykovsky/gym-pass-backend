package pl.smyk.gympassbackend.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.smyk.gympassbackend.review.dto.CommentDto;
import pl.smyk.gympassbackend.review.dto.RatingDto;
import pl.smyk.gympassbackend.review.dto.SubCommentDto;
import pl.smyk.gympassbackend.review.service.ReviewService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/rating")
    public ResponseEntity<?> addRating(Authentication authentication, @RequestBody RatingDto ratingDto) {
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        ratingDto.setUserEmail(authentication.getName());

        RatingDto response = reviewService.addRating(ratingDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/rating/summary")
    public ResponseEntity<?> getRatingSummary(Authentication authentication) {
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        Map<String, Double> ratingSummary = reviewService.getRatingSummary();
        return ResponseEntity.ok(ratingSummary);
    }

    @GetMapping("/rating/user")
    public ResponseEntity<?> getUserRating(Authentication authentication) {
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        RatingDto userRating = reviewService.getUserRating(authentication.getName());
        return ResponseEntity.ok(userRating);
    }

    @GetMapping("/rating/user/isRating")
    public ResponseEntity<?> isUserRating(Authentication authentication) {
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        boolean isRating = reviewService.isUserRating(authentication.getName());
        return ResponseEntity.ok(isRating);
    }

    @DeleteMapping("/rating")
    public ResponseEntity<?> deleteRating(Authentication authentication) {
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        reviewService.deleteRating(authentication.getName());
        return ResponseEntity.ok("Ocena została pomyślnie usunięta");
    }

    @PostMapping("/comment")
    public ResponseEntity<?> addComment(Authentication authentication, @RequestBody String commentText) {
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        CommentDto response = reviewService.addComment(authentication.getName(), commentText);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/comment/sub")
    public ResponseEntity<?> addSubComment(Authentication authentication, @RequestBody SubCommentDto subCommentDto) {
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        subCommentDto.setUserEmail(authentication.getName());

        System.out.println(subCommentDto);
        reviewService.addSubComment(subCommentDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/comment/sub")
    public ResponseEntity<?> deleteSubComment(
            Authentication authentication,
            @RequestParam String id,
            @RequestParam String parentId) {

        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        SubCommentDto commentDto = new SubCommentDto();
        commentDto.setId(id);
        commentDto.setParentCommentId(parentId);
        commentDto.setUserEmail(authentication.getName());

        reviewService.removeSubComment(commentDto);
        return ResponseEntity.ok("Usunięto sub komentarz");
    }

    @GetMapping("/comment")
    public ResponseEntity<?> getComments(Authentication authentication) {
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        List<CommentDto> allComments = reviewService.getComments();
        return ResponseEntity.ok(allComments);
    }

    @PatchMapping("/comment")
    public ResponseEntity<?> updateComment(Authentication authentication, @RequestBody CommentDto commentDto) {
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        commentDto.setUserEmail(authentication.getName());

        CommentDto updatedComment = reviewService.updateComment(commentDto);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/comment")
    public ResponseEntity<?> deleteComment(Authentication authentication, @RequestParam String commentId) {
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        reviewService.deleteComment(commentId, authentication.getName());
        return ResponseEntity.ok("Komentarz został pomyślnie usunięty");
    }
}
