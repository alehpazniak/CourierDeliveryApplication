package pl.aleh.courier.inpostpl;

import static org.springframework.http.MediaType.APPLICATION_PDF;
import static pl.aleh.exception.ExceptionUtils.getFormattedTimeoutError;
import static pl.aleh.exception.ExceptionUtils.handleClientResponseError;
import static pl.aleh.exception.ExceptionUtils.handleClientResponseJsonError;
import static pl.aleh.util.RetryUtils.getRetrySpec;

import domain.locations.InpostPlLocationsQueryParams;
import domain.locations.Item;
import domain.locations.Response;
import domain.request.CreateShipmentRequest;
import domain.response.CreateShipmentResponse;
import domain.tracking.GetTrackingResponse;
import java.time.Duration;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import pl.aleh.courier.inpostpl.config.InpostPlClientConfiguration;
import pl.aleh.exception.CourierClientException;
import pl.aleh.exception.CourierNoDataException;
import reactor.core.publisher.Mono;

@Component
public class InpostPlClient {

  public static final String MAX_DISTANCE = "max_distance";
  public static final String RELATIVE_POST_CODE = "relative_post_code";
  public static final String RELATIVE_POINT = "relative_point";
  public static final String TYPE = "type";
  public static final String LOCKERS_ONLY = "parcel_locker";
  private final WebClient webClient;
  private final Duration timeout;
  private final InpostPlClientConfiguration configuration;

  public InpostPlClient(final Builder webClientBuilder, final InpostPlClientConfiguration configuration) {
    this.timeout = configuration.getReadTimeout();
    this.configuration = configuration;
    this.webClient = webClientBuilder
        .baseUrl(configuration.getBaseUrl())
        .build();
  }

  public CreateShipmentResponse createShipment(
      final CreateShipmentRequest createShipmentRequest, final String organizationId, final String token
  ) {
    return webClient.post()
        .uri("/v1/organizations/{organizationId}/shipments", organizationId)
        .headers(headers -> headers.setBearerAuth(token))
        .body(BodyInserters.fromValue(createShipmentRequest))
        .retrieve()
        .onStatus(HttpStatusCode::isError, handleClientResponseJsonError(createShipmentRequest))
        .bodyToMono(CreateShipmentResponse.class)
        .timeout(timeout, getFormattedTimeoutError(createShipmentRequest, this.getClass(), timeout))
        .blockOptional()
        .orElseThrow(() -> new CourierNoDataException(getClass()));
  }

  public String retrieveTrackingNumber(final Integer shipmentId, final String token) {
    return webClient.get()
        .uri("/v1/shipments/{shipmentId}", shipmentId)
        .headers(headers -> headers.setBearerAuth(token))
        .accept(APPLICATION_PDF)
        .retrieve()
        .onStatus(HttpStatusCode::isError, handleClientResponseError())
        .bodyToMono(CreateShipmentResponse.class)
        .timeout(timeout, getFormattedTimeoutError(shipmentId, this.getClass(), timeout))
        .flatMap(createShipmentResponse -> createShipmentResponse.getTrackingNumber() != null
            ? Mono.just(createShipmentResponse.getTrackingNumber())
            : Mono.error(new CourierClientException("TrackingNumber in response is empty shipmentId: " + shipmentId)
                .withCourierResponseAsString(createShipmentResponse))
        )
        .retryWhen(getRetrySpec(configuration.getRetryNumber(), configuration.getRetryBackoff()))
        .block();
  }

  public byte[] getLabel(final Integer shipmentId, final String token) {
    return webClient.get()
        .uri("/v1/shipments/{shipmentId}/label?format=Pdf&type=A6", shipmentId)
        .headers(headers -> headers.setBearerAuth(token))
        .accept(APPLICATION_PDF)
        .retrieve()
        .onStatus(HttpStatusCode::isError, handleClientResponseError())
        .bodyToMono(byte[].class)
        .timeout(timeout, getFormattedTimeoutError(shipmentId, this.getClass(), timeout))
        .retryWhen(getRetrySpec(3, Duration.ofSeconds(3)))
        .blockOptional()
        .orElseThrow(() -> new CourierNoDataException(getClass()));
  }

  public Mono<GetTrackingResponse> getTracking(final String trackingNumber) {
    return webClient.get()
        .uri("/v1/tracking/{trackingNumber}", trackingNumber)
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode().is2xxSuccessful()) {
            return clientResponse.bodyToMono(GetTrackingResponse.class);
          } else {
            return Mono.empty();
          }
        })
        .timeout(timeout, getFormattedTimeoutError(trackingNumber, this.getClass(), timeout));
  }

  public Response getLocations(final InpostPlLocationsQueryParams params) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder.path("/v1/points")
            .queryParam(RELATIVE_POST_CODE, params.getRelativePostCode())
            .queryParam(MAX_DISTANCE, params.getMaxDistance())
            .queryParam(RELATIVE_POINT, params.getRelativePoint())
            .queryParam(TYPE, LOCKERS_ONLY)
            .build())
        .retrieve()
        .onStatus(HttpStatusCode::isError, handleClientResponseError())
        .bodyToMono(Response.class)
        .timeout(timeout, getFormattedTimeoutError(params, this.getClass(), timeout))
        .blockOptional()
        .orElseThrow(() -> new CourierNoDataException(getClass()));
  }

  public Item getLocation(final String locationId) {
    return webClient.get()
        .uri("/v1/points/{locationId}", locationId)
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.empty())
        .onStatus(HttpStatusCode::is5xxServerError, handleClientResponseError())
        .bodyToMono(Item.class)
        .timeout(timeout, getFormattedTimeoutError(locationId, this.getClass(), timeout))
        .blockOptional()
        .orElseThrow(() -> new CourierNoDataException(getClass()));
  }

}
