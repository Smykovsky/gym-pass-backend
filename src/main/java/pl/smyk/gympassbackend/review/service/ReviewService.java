package pl.smyk.gympassbackend.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.smyk.gympassbackend.auth.exception.UserNotFoundException;
import pl.smyk.gympassbackend.auth.model.User;
import pl.smyk.gympassbackend.auth.service.AuthService;
import pl.smyk.gympassbackend.review.dto.CommentDto;
import pl.smyk.gympassbackend.review.dto.RatingDto;
import pl.smyk.gympassbackend.review.dto.SubCommentDto;
import pl.smyk.gympassbackend.review.exception.*;
import pl.smyk.gympassbackend.review.mapper.CommentMapper;
import pl.smyk.gympassbackend.review.mapper.RatingMapper;
import pl.smyk.gympassbackend.review.mapper.SubCommentMapper;
import pl.smyk.gympassbackend.review.model.Comment;
import pl.smyk.gympassbackend.review.model.Rating;
import pl.smyk.gympassbackend.review.model.SubComment;
import pl.smyk.gympassbackend.review.repository.CommentRepository;
import pl.smyk.gympassbackend.review.repository.RatingRepository;
import pl.smyk.gympassbackend.review.repository.SubCommentRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final CommentRepository commentRepository;
    private final SubCommentRepository subCommentRepository;
    private final RatingRepository ratingRepository;
    private final AuthService authService;


    //add rating
    public RatingDto addRating(RatingDto ratingRequest) {
        User user = authService.findByEmail(ratingRequest.getUserEmail()).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        if (ratingRequest.getRating() < 1 || ratingRequest.getRating() > 5) {
            throw new BadRatingException();
        }

        Rating existingRating = ratingRepository.findByUser(user);

        if (existingRating != null) {
            existingRating.setRating(ratingRequest.getRating());
            Rating updatedRating = ratingRepository.save(existingRating);
            return RatingMapper.INSTANCE.ratingToRatingDto(updatedRating);
        }

        Rating newRating = Rating.builder().rating(ratingRequest.getRating()).user(user).build();
        Rating savedRating = ratingRepository.save(newRating);

        return RatingMapper.INSTANCE.ratingToRatingDto(savedRating);
    }

    public boolean isUserRating(String userEmail) {
        User user = authService.findByEmail(userEmail).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        return ratingRepository.findAll().stream()
                .filter(rating -> rating.getUser() != null)
                .map(rating -> rating.getUser().getEmail())
                .anyMatch(email -> email.equals(user.getEmail()));
    }

    public Map<String, Double> getRatingSummary() {
        List<Rating> ratings = ratingRepository.findAll();

        int totalCount = ratings.size();
        double averageRating = totalCount > 0
                ? ratings.stream().mapToInt(Rating::getRating).average().orElse(0.0)
                : 0.0;

        averageRating = Math.round(averageRating * 2) / 2.0;

        Map<String, Double> result = new HashMap<>();
        result.put("totalCount", (double) totalCount);
        result.put("averageRating", averageRating);

        return result;
    }

    public RatingDto getUserRating(String userEmail) {
        User user = authService.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Rating byUser = ratingRepository.findByUser(user);

        if (byUser == null) {
            throw new RatingNotFoundException();
        }

        return RatingMapper.INSTANCE.ratingToRatingDto(byUser);
    }

    //delete rating
    public void deleteRating(String userEmail) {
        User user = authService.findByEmail(userEmail).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        Rating byUser = ratingRepository.findByUser(user);
        if (byUser == null) {
            throw new RatingNotFoundException();
        }

        ratingRepository.delete(byUser);
    }


    //add comment
    public CommentDto addComment(String userEmail, String commentText) {
        User user = authService.findByEmail(userEmail).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        Comment build = Comment.builder().commentText(commentText).user(user).subComments(new ArrayList<>()).build();
        Comment saved = commentRepository.save(build);

        return CommentMapper.INSTANCE.commentToCommentDto(saved);
    }

    //get comments
    public List<CommentDto> getComments() {
        return commentRepository.findAll().stream()
                .map(comment -> {
                    CommentDto commentDto = CommentMapper.INSTANCE.commentToCommentDto(comment);

                    if (comment.getSubComments() != null) {
                        List<SubCommentDto> subCommentDtos = comment.getSubComments().stream()
                                .map(SubCommentMapper.INSTANCE::subCommentToSubCommentDto)
                                .toList();
                        commentDto.setSubComments(subCommentDtos);
                    }

                    return commentDto;
                }).toList();
    }

    //update comment
    public CommentDto updateComment(CommentDto commentRequest) {
        User user = authService.findByEmail(commentRequest.getUserEmail()).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        assert commentRequest.getId() != null;
        Comment comment = commentRepository.findById(commentRequest.getId()).orElse(null);
        if (comment == null) {
            throw new CommentNotFoundException();
        }

        if (!comment.getUser().getEmail().equals(commentRequest.getUserEmail())) {
            throw new NotAuthorizedCommentException();
        }

        comment.setCommentText(commentRequest.getCommentText());
        Comment updatedComment = commentRepository.save(comment);
        return CommentMapper.INSTANCE.commentToCommentDto(updatedComment);
    }

    //delete comment
    public void deleteComment(String commentId, String userEmail) {
        User user = authService.findByEmail(userEmail).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment == null) {
            throw new CommentNotFoundException();
        }

        if (!comment.getUser().getEmail().equals(userEmail)) {
            throw new NotAuthorizedCommentException("You are not authorized to delete this comment!");
        }

        commentRepository.delete(comment);
    }

    public void addSubComment(SubCommentDto subCommentDto) {
        User user = authService.findByEmail(subCommentDto.getUserEmail()).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        assert subCommentDto.getId() != null;
        Comment comment = commentRepository.findById(subCommentDto.getParentCommentId()).orElse(null);
        if (comment == null) {
            throw new CommentNotFoundException();
        }

        SubComment subComment = SubComment.builder()
                .commentText(subCommentDto.getCommentText())
                .user(user)
                .parentCommentId(comment.getId())
                .build();

        SubComment saved = subCommentRepository.save(subComment);

        comment.addSubComment(saved);
        commentRepository.save(comment);
    }

    public void removeSubComment(SubCommentDto subCommentDto) {
        User user = authService.findByEmail(subCommentDto.getUserEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Comment parent = commentRepository.findById(subCommentDto.getParentCommentId())
                .orElseThrow(CommentNotFoundException::new);

        SubComment subComment = parent.getSubComments().stream()
                .filter(sub -> sub.getId().equals(subCommentDto.getId()))
                .findFirst()
                .orElseThrow(CommentNotFoundException::new);

        parent.removeSubComment(subComment);
        subCommentRepository.delete(subComment);

        commentRepository.save(parent);
    }
}
