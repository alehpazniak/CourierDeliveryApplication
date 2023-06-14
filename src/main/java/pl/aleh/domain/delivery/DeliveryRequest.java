package pl.aleh.domain.delivery;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import pl.aleh.domain.AccountSettings;


@JsonInclude(Include.NON_NULL)
@Data
public class DeliveryRequest implements DeliveryRequestData {


  private int serviceId;
  private DeliveryAddress deliveryAddress;
  private Address returnAddress;
  private Order order;
  private Shipment shipment;
  private AccountSettings accountSettings;
  private Warehouse warehouse;

}
