package pl.aleh.exception;

import org.springframework.http.HttpStatus;

public class CourierNoDataException extends CourierException {

  public CourierNoDataException(String message) {
    super(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_NO_DATA, message);
  }

  public CourierNoDataException(Class<?> courierClientClass) {
    super(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_NO_DATA,
        courierClientClass.getSimpleName() + " response has no data");
  }

}
