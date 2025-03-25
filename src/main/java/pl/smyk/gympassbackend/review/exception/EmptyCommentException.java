package pl.smyk.gympassbackend.review.exception;

public class EmptyCommentException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Comment can not be empty";
    public EmptyCommentException() {
        super(DEFAULT_MESSAGE);
    }

    public EmptyCommentException(String message) {
        super(message);
    }
}
