package pl.aleh.courier.inpostpl.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.aleh.courier.inpostpl.repository.InpostPlServiceRepository;
import pl.aleh.exception.CourierNoDataException;
import pl.aleh.courier.inpostpl.repository.model.InpostPlServiceMapping;

@Service
@RequiredArgsConstructor
public class InpostPlDatabaseService {

  private final InpostPlServiceRepository inpostPlServiceRepository;

  public String getServiceName(final Integer serviceId) {
    return inpostPlServiceRepository.findById(serviceId).map(InpostPlServiceMapping::getExternalServiceName)
        .orElseThrow(() -> new CourierNoDataException(
            String.format("internalServiceId: %d does not exist in database config", serviceId)));
  }

}
