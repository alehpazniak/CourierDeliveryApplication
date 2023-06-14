package pl.aleh.courier.inpostpl.transformer;

import domain.tracking.GetTrackingResponse;
import domain.tracking.TrackingDetail;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import pl.aleh.domain.tracking.TrackingData;
import pl.aleh.domain.tracking.TrackingEvent;

@Component
public class InpostPlTrackingTransformer {

  public TrackingData convertToTrackingData(final GetTrackingResponse getTrackingResponse) {
    return TrackingData.builder()
        .trackingNumber(getTrackingResponse.getTrackingNumber())
        .trackingEvents(getTrackingResponse.getTrackingDetails().stream().map(this::convertToTrackingEvent)
            .collect(Collectors.toList()))
        .build();
  }

  private TrackingEvent convertToTrackingEvent(final TrackingDetail trackingDetail) {
    return TrackingEvent.builder()
        .message(trackingDetail.getStatus())
        .code(trackingDetail.getOriginStatus())
        .eventDate(OffsetDateTime.parse(trackingDetail.getDatetime()).toInstant().atOffset(ZoneOffset.UTC))
        .build();
  }
}
