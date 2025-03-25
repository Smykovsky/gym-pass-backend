package pl.smyk.gympassbackend.review.exception;

public class RatingAlreadyExistsException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "Rating already exists";
    public RatingAlreadyExistsException() {
        super(DEFAULT_MESSAGE);
    }

    public RatingAlreadyExistsException(String message) {
        super(message);
    }
}
