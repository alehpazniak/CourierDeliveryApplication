package pl.aleh;

import pl.aleh.domain.clickandcollect.ClickAndCollectLocationActiveRequest;
import pl.aleh.domain.clickandcollect.ClickAndCollectLocationActiveResponse;
import pl.aleh.domain.clickandcollect.ClickAndCollectLocationsRequest;
import pl.aleh.domain.clickandcollect.ClickAndCollectLocationsResponse;
import pl.aleh.domain.delivery.DeliveryRequest;
import pl.aleh.domain.delivery.DeliveryResponse;
import pl.aleh.domain.tracking.TrackingRequest;
import pl.aleh.domain.tracking.TrackingResponse;

public abstract class CourierApi {

  public ClickAndCollectLocationActiveResponse isClickAndCollectLocationActive(
      ClickAndCollectLocationActiveRequest clickAndCollectLocationActiveRequest
  ) {
    throw new UnsupportedOperationException("Service does not implement isClickAndCollectLocationActive");
  }

  public ClickAndCollectLocationsResponse getClickAndCollectLocations(
      ClickAndCollectLocationsRequest clickAndCollectLocationsRequest
  ) {
    throw new UnsupportedOperationException("Service does not implement getClickAndCollectLocations");
  }

  public abstract DeliveryResponse requestDelivery(DeliveryRequest deliveryRequest);


  public abstract TrackingResponse getTracking(TrackingRequest trackingRequest);

}
