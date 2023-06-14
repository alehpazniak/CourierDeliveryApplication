package pl.aleh.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.aleh.domain.delivery.DeliveryRequest;
import pl.aleh.domain.delivery.DeliveryResponse;
import pl.aleh.facade.CourierApiService;

@RequiredArgsConstructor
@RestController
public class RequestDeliveryController {

  private final CourierApiService courierAPIService;

  @PostMapping(value = {"/delivery"}, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<DeliveryResponse> requestDelivery(@RequestBody final DeliveryRequest deliveryRequest) {
    var courierApi = courierAPIService.getCourierApiObject(deliveryRequest.getServiceId());
    var deliveryResponse = courierApi.requestDelivery(deliveryRequest);
    return ResponseEntity.ok(deliveryResponse);
  }
}
