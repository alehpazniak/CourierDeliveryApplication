package pl.aleh.courier.inpostpl.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.aleh.domain.clickandcollect.ClickAndCollectLocationActiveRequest;
import pl.aleh.domain.clickandcollect.ClickAndCollectLocationActiveResponse;
import pl.aleh.domain.clickandcollect.ClickAndCollectLocationsRequest;
import pl.aleh.domain.clickandcollect.ClickAndCollectLocationsResponse;
import pl.aleh.courier.inpostpl.InpostPlClient;
import pl.aleh.courier.inpostpl.transformer.InpostPlBusinessHoursParser;
import pl.aleh.courier.inpostpl.transformer.InpostPlClickAndCollectLocationsTransformer;

@Service
@RequiredArgsConstructor
public class InpostPlClickAndCollectLocationsService {

  private final InpostPlClickAndCollectLocationsTransformer transformer;
  private final InpostPlClient client;
  private final InpostPlBusinessHoursParser businessHoursParser;

  public ClickAndCollectLocationsResponse getLocations(
      final ClickAndCollectLocationsRequest clickAndCollectLocationsRequest
  ) {
    var params = transformer.transformToQueryParams(clickAndCollectLocationsRequest);
    var inpostPlResponse = client.getLocations(params);
    return transformer.transformToClickAndCollectLocationsResponse(inpostPlResponse, businessHoursParser,
        clickAndCollectLocationsRequest.getCountryCode());
  }

  public ClickAndCollectLocationActiveResponse isLocationActive(
      final ClickAndCollectLocationActiveRequest clickAndCollectLocationActiveRequest
  ) {
    var location = client.getLocation(clickAndCollectLocationActiveRequest.getLocationId());
    return new ClickAndCollectLocationActiveResponse("Operating".equals(location.getStatus()));
  }
}
