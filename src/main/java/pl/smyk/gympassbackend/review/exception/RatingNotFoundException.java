package pl.smyk.gympassbackend.review.exception;

public class RatingNotFoundException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "Rating has been deleted or not found";
    public RatingNotFoundException() {
      super(DEFAULT_MESSAGE);
    }

    public RatingNotFoundException(String message) {
        super(message);
    }
}
