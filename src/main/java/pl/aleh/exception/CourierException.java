package pl.aleh.exception;

import static pl.aleh.exception.ExceptionUtils.getWebClientResponseDetailsAsMap;
import static pl.aleh.exception.ExceptionUtils.getWebClientResponseDetailsAsString;
import static pl.aleh.util.StringFormatUtils.asString;
import static pl.aleh.util.StringFormatUtils.toCourierLogs;

import java.util.Map;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.aleh.util.JsonConverterUtils;
import pl.aleh.util.StringFormat;

public class CourierException extends RuntimeException{


  public static final String ERROR_INTERNAL = "internal";
  public static final String ERROR_NO_DATA = "no_data";
  public static final String ERROR_VALIDATION = "validation";
  public static final String ERROR_CLIENT = "client";

  private final String errorCode;
  private final HttpStatus httpStatus;
  private Map<String, Object> details;
  protected String courierRequest;
  protected String courierResponse;

  public CourierException(String errorCode, String message) {
    this(HttpStatus.INTERNAL_SERVER_ERROR, errorCode, message, null);
  }

  public CourierException(HttpStatus httpStatus, String errorCode, String message) {
    this(httpStatus, errorCode, message, null);
  }

  public CourierException(HttpStatus httpStatus, String errorCode, Throwable cause) {
    super(getWebClientResponseDetailsAsString(cause), cause);
    this.httpStatus = httpStatus;
    this.errorCode = errorCode;
  }

  public CourierException(HttpStatus httpStatus, String errorCode, String message, Throwable cause) {
    super(message, cause);
    this.httpStatus = httpStatus;
    this.errorCode = errorCode;
    this.details = getWebClientResponseDetailsAsMap(cause);
  }

  public CourierException withCourierRequestAsJson(final Object courierRequest) {
    this.courierRequest = asString(courierRequest, StringFormat.JSON);
    return this;
  }

  public CourierException withCourierRequestAsString(final Object courierRequest) {
    this.courierRequest = String.valueOf(courierRequest);
    return this;
  }

  public CourierException withCourierResponseAsJson(final Object courierResponse) {
    this.courierResponse = asString(courierResponse, StringFormat.JSON);
    return this;
  }

  public CourierException withCourierResponseAsString(final Object courierResponse) {
    this.courierResponse = String.valueOf(courierResponse);
    return this;
  }

  public String getCourierRequest() {
    return courierRequest;
  }

  public String getCourierResponse() {
    return courierResponse;
  }

}
