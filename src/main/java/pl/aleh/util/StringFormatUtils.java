package pl.aleh.util;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.LF;
import static org.apache.commons.lang3.StringUtils.SPACE;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@UtilityClass
@Slf4j
public class StringFormatUtils {

  public static final String COURIER_REQUEST = "\nCourier request:\n";
  public static final String COURIER_RESPONSE = "\nCourier response:\n";

  public static String midString(final String value, final int from, final int requiredLength) {
    var result = StringUtils.normalizeSpace(value);
    var length = requiredLength;
    var index = from;
    if (result == null) {
      return result;
    }
    if (result.length() > index && Character.isLowSurrogate(result.charAt(index))) {
      index++;
    }
    if (result.length() >= (index + length) && Character.isHighSurrogate(result.charAt(index + length - 1))) {
      length--;
    }
    var mid = StringUtils.mid(result, index, length);
    return mid.equals(EMPTY) ? null : mid;
  }


  public static String trimJoin(String delimiter, String... strings) {
    return Stream.of(strings)
        .filter(Objects::nonNull)
        .filter(Predicate.not(String::isBlank))
        .map(String::trim)
        .collect(Collectors.joining(delimiter));
  }

  public static String toCourierLogs(final Throwable t) {
    return toCourierLogs(t, null, null);
  }

  public static String toCourierLogs(final Throwable t, final String courierRequest, final String courierResponse) {
    WebClientResponseException e = t instanceof WebClientResponseException ? (WebClientResponseException) t : null;

    var message = new StringBuilder();
    if (e != null || courierRequest != null) {
      message.append(COURIER_REQUEST);
      if (e != null) {
        var request = e.getRequest();
        message.append(request.getMethod()).append(SPACE).append(request.getURI()).append(SPACE)
            .append(request.getHeaders());
      }
      if (courierRequest != null) {
        message.append(courierRequest);
      }
    }

    if (e != null || courierResponse != null) {
      message.append(COURIER_RESPONSE);
      if (e != null) {
        message.append(e.getStatusCode()).append(SPACE)
            .append(URLDecoder.decode(e.getHeaders().toString(), StandardCharsets.UTF_8))
            .append(LF)
            .append(e.getResponseBodyAsString(StandardCharsets.UTF_8));
      }
      if (courierResponse != null) {
        message.append(courierResponse);
      }
    }
    return message.toString();
  }

  @SneakyThrows
  public static String asString(final Object o, final StringFormat format) {
    if (format == null) {
      return String.valueOf(o);
    }
    return JsonConverterUtils.getAsString(o);
  }

}
