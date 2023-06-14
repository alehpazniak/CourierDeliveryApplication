package pl.aleh.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.aleh.facade.CourierApiService;
import pl.aleh.domain.tracking.TrackingRequest;
import pl.aleh.domain.tracking.TrackingResponse;

@RestController
public class TrackingController {

  private final CourierApiService courierApiService;

  public TrackingController(final CourierApiService courierApiService) {
    this.courierApiService = courierApiService;
  }

  @PostMapping("/tracking")
  public ResponseEntity<TrackingResponse> getTracking(@RequestBody final TrackingRequest trackingRequest) {
    var courierApi = courierApiService.getCourierApiObject(trackingRequest.getServiceId());
    var response = courierApi.getTracking(trackingRequest);
    return ResponseEntity.ok(response);
  }

}
