package pl.aleh.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.aleh.domain.clickandcollect.ClickAndCollectLocationActiveRequest;
import pl.aleh.domain.clickandcollect.ClickAndCollectLocationActiveResponse;
import pl.aleh.domain.clickandcollect.ClickAndCollectLocationsRequest;
import pl.aleh.domain.clickandcollect.ClickAndCollectLocationsResponse;
import pl.aleh.facade.CourierApiService;

@RequiredArgsConstructor
@RestController
public class ClickAndCollectLocationsController {

  private final CourierApiService courierApiService;

  @PostMapping
  public ResponseEntity<ClickAndCollectLocationsResponse> getClickAndCollectLocations(
      @RequestBody final ClickAndCollectLocationsRequest clickAndCollectLocationsRequest
  ) {
    var courierApi = courierApiService.getCourierApiObject(clickAndCollectLocationsRequest.getServiceId());
    var response = courierApi.getClickAndCollectLocations(clickAndCollectLocationsRequest);
    return ResponseEntity.ok(response);
  }

  @PostMapping(value = {"/status"})
  public ResponseEntity<ClickAndCollectLocationActiveResponse> isCLickAndCollectLocationActive(
      @RequestBody final ClickAndCollectLocationActiveRequest clickAndCollectLocationActiveRequest
  ) {
    var courierApi = courierApiService.getCourierApiObject(clickAndCollectLocationActiveRequest.getServiceId());
    var response = courierApi.isClickAndCollectLocationActive(clickAndCollectLocationActiveRequest);
    return ResponseEntity.ok(response);
  }
}
