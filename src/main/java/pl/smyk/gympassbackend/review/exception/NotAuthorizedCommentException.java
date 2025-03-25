package pl.smyk.gympassbackend.review.exception;

public class NotAuthorizedCommentException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "You do not have permission to update this comment!";
    public NotAuthorizedCommentException() {
      super(DEFAULT_MESSAGE);
    }

    public NotAuthorizedCommentException(String message) {
        super(message);
    }
}
