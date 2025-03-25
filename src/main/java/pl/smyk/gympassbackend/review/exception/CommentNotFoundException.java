package pl.smyk.gympassbackend.review.exception;

public class CommentNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Comment has been deleted or not exists!";

    public CommentNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public CommentNotFoundException(String message) {
        super(message);
    }
}
