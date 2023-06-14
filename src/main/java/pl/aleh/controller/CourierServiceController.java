package pl.aleh.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.aleh.facade.CourierApiService;
import pl.aleh.facade.CourierServiceModel;

@RequiredArgsConstructor
@RestController
@RequestMapping("/courier-service")
public class CourierServiceController {

  private final CourierApiService courierApiService;

  @PostMapping
  public CourierServiceModel createCourierService(@RequestBody final CourierServiceModel courierServiceModel) {
    return courierApiService.createCourierService(courierServiceModel);
  }
}
