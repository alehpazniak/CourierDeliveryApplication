package pl.aleh.domain.delivery;

import pl.aleh.domain.AccountSettings;

public interface DeliveryRequestData {

  int getServiceId();
  DeliveryAddress getDeliveryAddress();
  Order getOrder();
  Shipment getShipment();
  AccountSettings getAccountSettings();
  Warehouse getWarehouse();
}
