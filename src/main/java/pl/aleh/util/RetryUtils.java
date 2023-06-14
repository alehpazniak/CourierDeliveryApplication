package pl.aleh.util;

import static reactor.util.retry.Retry.backoff;

import java.time.Duration;
import lombok.experimental.UtilityClass;
import pl.aleh.exception.CourierClientException;
import reactor.util.retry.RetryBackoffSpec;

@UtilityClass
public class RetryUtils {

  public static RetryBackoffSpec getRetrySpec(final long retryNumbers, final Duration backoffSeconds) {
    return backoff(retryNumbers, backoffSeconds).onRetryExhaustedThrow(
        (retryBackoffSpec, retrySignal) -> {
          var failure = retrySignal.failure();
          var courierClientException = new CourierClientException(
              String.format("Failure reason: %s. Amount of retry calls: %d", failure.getMessage(),
                  retrySignal.totalRetries()), failure);
          if (failure instanceof CourierClientException) {
            var ex = (CourierClientException) failure;
            return courierClientException
                .withCourierResponseAsString(ex.getCourierResponse())
                .withCourierRequestAsString(ex.getCourierRequest());
          }
          return courierClientException;
        }
    );
  }

}
