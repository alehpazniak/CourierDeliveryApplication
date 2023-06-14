package pl.aleh.exception;

import static java.lang.String.join;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import lombok.experimental.UtilityClass;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pl.aleh.util.JsonConverterUtils;
import reactor.core.publisher.Mono;

@UtilityClass
public class ExceptionUtils {

  public static final String ERROR_DETAILS_KEY = "error";
  public static final Set<MediaType> JSON_MEDIA_TYPE_SET = Set.of(APPLICATION_JSON, APPLICATION_PROBLEM_JSON);

  @NotNull
  public static <T> Mono<T> getFormattedTimeoutError(final Object request, final Class<?> clazz, final Duration timeout) {
    return Mono.error(
        new CourierClientException(
            join(SPACE, "TimeoutException with value of", timeout.toString(),
                "occurred for", clazz.getSimpleName(), ":", Thread.currentThread().getStackTrace()[2].getMethodName(),
                "method")
        ).withCourierRequestAsJson(request)
    );
  }

  public static Map<String, Object> getWebClientResponseDetailsAsMap(final Throwable cause) {
    var details = getWebClientResponseAsObject(cause);
    return details == null ? null : Map.of(ERROR_DETAILS_KEY, details);
  }

  public static String getWebClientResponseDetailsAsString(final Throwable cause) {
    var details = getWebClientResponseAsObject(cause);
    return details == null ? cause.getMessage() : details.toString();
  }

  public static Function<ClientResponse, Mono<? extends Throwable>> handleClientResponseError() {
    return response -> response.createException().flatMap(e -> Mono.error(
        new CourierClientException(e)
    ));
  }

  public static Function<ClientResponse, Mono<? extends Throwable>> handleClientResponseJsonError(
      final Object request
  ) {
    return response -> response.createException().flatMap(e -> Mono.error(
        new CourierClientException(e)
            .withCourierRequestAsJson(request)
    ));
  }

  private static Object getWebClientResponseAsObject(final Throwable cause) {
    Object result = null;
    if (cause instanceof WebClientResponseException) {
      var webClientException = (WebClientResponseException) cause;
      var responseBody = webClientException.getResponseBodyAsString(StandardCharsets.UTF_8);
      var contentType = webClientException.getHeaders().getContentType();
      if (contentType != null && JSON_MEDIA_TYPE_SET.contains(contentType)) {
        result = JsonConverterUtils.getAsObject(responseBody, Object.class, true);
      }
      if (result == null) {
        result = responseBody;
      }
    }
    return result;
  }

}
