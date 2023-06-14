package pl.aleh.exception;

import org.springframework.http.HttpStatus;

public class CourierApiServiceNotFoundException extends CourierException {

  public CourierApiServiceNotFoundException(Integer id) {
    super(HttpStatus.NOT_FOUND, ERROR_NO_DATA, String.format("Service with id=[%s] not found", id));
  }
}
