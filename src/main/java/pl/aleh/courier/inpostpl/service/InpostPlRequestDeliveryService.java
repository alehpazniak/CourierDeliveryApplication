package pl.aleh.courier.inpostpl.service;

import static pl.aleh.courier.inpostpl.config.AccountConstants.ORGANIZATION_ID;
import static pl.aleh.courier.inpostpl.config.AccountConstants.TOKEN;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.aleh.domain.delivery.DeliveryRequest;
import pl.aleh.domain.delivery.DeliveryResponse;
import pl.aleh.courier.inpostpl.InpostPlClient;
import pl.aleh.courier.inpostpl.config.InpostPlClientConfiguration;
import pl.aleh.courier.inpostpl.transformer.InpostPlRequestDeliveryTransformer;

@Service
@RequiredArgsConstructor
public class InpostPlRequestDeliveryService {

  private final InpostPlClient inpostPlClient;
  private final InpostPlRequestDeliveryTransformer inpostPlRequestDeliveryTransformer;
  private final InpostPlDatabaseService inpostPlDatabaseService;
  private final InpostPlClientConfiguration configuration;

  public DeliveryResponse requestDelivery(final DeliveryRequest deliveryRequest) {
    var token = deliveryRequest.getAccountSettings().getSettings().get(TOKEN);
    var organizationId = deliveryRequest.getAccountSettings().getSettings().get(ORGANIZATION_ID);
    var serviceName = inpostPlDatabaseService.getServiceName(deliveryRequest.getServiceId());

    var createShipmentRequest = inpostPlRequestDeliveryTransformer.transformToCreateShipmentRequest(deliveryRequest,
        serviceName);
    var shipmentId = inpostPlClient.createShipment(createShipmentRequest, organizationId, token).getId();
    var trackingNumber = inpostPlClient.retrieveTrackingNumber(shipmentId, token);
    var labelAsByteArray = inpostPlClient.getLabel(shipmentId, token);
    var label = inpostPlRequestDeliveryTransformer.convertToLabel(labelAsByteArray);

    return inpostPlRequestDeliveryTransformer.transformToDeliveryResponse(trackingNumber, label,
        configuration.getTrackingUrlFormat());
  }
}
