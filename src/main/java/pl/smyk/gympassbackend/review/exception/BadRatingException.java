package pl.smyk.gympassbackend.review.exception;

public class BadRatingException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Rating should be between 1 and 5";

    public BadRatingException() {
      super(DEFAULT_MESSAGE);
    }
    public BadRatingException(String message) {
        super(message);
    }
}
