package mission.application.domain.exception;

public class FishNotFound extends RuntimeException {
  public FishNotFound(String message) {
    super(message);
  }
}
