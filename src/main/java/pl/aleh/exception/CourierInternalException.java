package pl.aleh.exception;

import org.springframework.http.HttpStatus;

public class CourierInternalException extends CourierException{

  public CourierInternalException(String message, Throwable cause) {
    super(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_INTERNAL, message, cause);
  }

}
