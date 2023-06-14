package pl.aleh.courier.inpostpl.service;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.aleh.domain.tracking.TrackingData;
import pl.aleh.domain.tracking.TrackingRequest;
import pl.aleh.domain.tracking.TrackingResponse;
import pl.aleh.courier.inpostpl.InpostPlClient;
import pl.aleh.courier.inpostpl.transformer.InpostPlTrackingTransformer;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class InpostPlTrackingService {

  private final InpostPlClient inpostPlClient;
  private final InpostPlTrackingTransformer inpostTrackingTransformer;

  public TrackingResponse getTracking(final TrackingRequest trackingRequest) {
    return TrackingResponse.builder()
        .trackingDataMap(Flux.fromIterable(trackingRequest.getTrackingNumbers())
            .flatMap(inpostPlClient::getTracking)
            .publishOn(Schedulers.parallel())
            .filter(Objects::nonNull)
            .map(inpostTrackingTransformer::convertToTrackingData)
            .collect(Collectors.toMap(TrackingData::getTrackingNumber, Function.identity()))
            .block())
        .build();
  }

}
