package pl.aleh.courier.inpostpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.aleh.CourierApi;
import pl.aleh.courier.inpostpl.service.InpostPlClickAndCollectLocationsService;
import pl.aleh.courier.inpostpl.service.InpostPlRequestDeliveryService;
import pl.aleh.courier.inpostpl.service.InpostPlTrackingService;
import pl.aleh.domain.clickandcollect.ClickAndCollectLocationActiveRequest;
import pl.aleh.domain.clickandcollect.ClickAndCollectLocationActiveResponse;
import pl.aleh.domain.clickandcollect.ClickAndCollectLocationsRequest;
import pl.aleh.domain.clickandcollect.ClickAndCollectLocationsResponse;
import pl.aleh.domain.delivery.DeliveryRequest;
import pl.aleh.domain.delivery.DeliveryResponse;
import pl.aleh.domain.tracking.TrackingRequest;
import pl.aleh.domain.tracking.TrackingResponse;

@Component
@RequiredArgsConstructor
public class InpostPlCourierApi extends CourierApi {

  private final InpostPlClickAndCollectLocationsService clickAndCollectLocationsService;
  private final InpostPlRequestDeliveryService inpostPlRequestDeliveryService;
  private final InpostPlTrackingService inpostPlTrackingService;

  @Override
  public DeliveryResponse requestDelivery(final DeliveryRequest deliveryRequest) {
    return inpostPlRequestDeliveryService.requestDelivery(deliveryRequest);
  }

  @Override
  public TrackingResponse getTracking(final TrackingRequest trackingRequest) {
    return inpostPlTrackingService.getTracking(trackingRequest);
  }

  @Override
  public ClickAndCollectLocationActiveResponse isClickAndCollectLocationActive(
      final ClickAndCollectLocationActiveRequest clickAndCollectLocationActiveRequest
  ) {
    return clickAndCollectLocationsService.isLocationActive(clickAndCollectLocationActiveRequest);
  }

  @Override
  public ClickAndCollectLocationsResponse getClickAndCollectLocations(
      final ClickAndCollectLocationsRequest clickAndCollectLocationsRequest
  ) {
    return clickAndCollectLocationsService.getLocations(clickAndCollectLocationsRequest);
  }

}
