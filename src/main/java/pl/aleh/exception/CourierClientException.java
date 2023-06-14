package pl.aleh.exception;

import org.springframework.http.HttpStatus;

public class CourierClientException extends CourierException{

  public CourierClientException(String message) {
    this(message, null);
  }

  public CourierClientException(Throwable cause) {
    super(HttpStatus.BAD_REQUEST, ERROR_CLIENT, cause);
  }

  public CourierClientException(String message, Throwable cause) {
    super(HttpStatus.BAD_REQUEST, ERROR_CLIENT, message, cause);
  }

  @Override
  public CourierClientException withCourierRequestAsJson(Object courierRequest) {
    super.withCourierRequestAsJson(courierRequest);
    return this;
  }

  @Override
  public CourierClientException withCourierResponseAsJson(Object courierResponse) {
    super.withCourierResponseAsJson(courierResponse);
    return this;
  }

  @Override
  public CourierClientException withCourierRequestAsString(Object courierRequest) {
    super.withCourierRequestAsString(courierRequest);
    return this;
  }

  @Override
  public CourierClientException withCourierResponseAsString(Object courierResponse) {
    super.withCourierResponseAsString(courierResponse);
    return this;
  }
}
