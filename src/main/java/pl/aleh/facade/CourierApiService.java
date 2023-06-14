package pl.aleh.facade;

import static org.apache.commons.lang3.ClassUtils.getSimpleName;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.aleh.CourierApi;
import pl.aleh.exception.CourierApiServiceNotFoundException;

@Service
public class CourierApiService {

  private final List<CourierApi> courierApis;

  private final CourierServiceRepository courierServiceRepository;

  @Autowired
  public CourierApiService(final List<CourierApi> courierApis, final CourierServiceRepository courierServiceRepository) {
    this.courierApis = courierApis;
    this.courierServiceRepository = courierServiceRepository;
  }

  public CourierApi getCourierApiObject(final int serviceId) {
    var courierServiceObject = courierServiceRepository.findById(serviceId)
        .orElseThrow(() -> new CourierApiServiceNotFoundException(serviceId));
    var className = courierServiceObject.getClassName();
    return courierApis.stream()
        .filter(c -> getSimpleName(c).equals(className))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException(
            String.format("ServiceId %d with className %s is not setup correctly", serviceId, className)));
  }

  public CourierServiceModel createCourierService(final CourierServiceModel courierServiceModel) {
    courierServiceRepository.createNewService(courierServiceModel.getServiceId(), courierServiceModel.getClassName());
    return courierServiceModel;
  }
}
