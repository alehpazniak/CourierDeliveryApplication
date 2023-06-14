package pl.aleh.courier.inpostpl.transformer;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.right;
import static pl.aleh.exception.CourierException.ERROR_NO_DATA;
import static pl.aleh.util.AddressUtils.getClickAndCollectLocationId;

import domain.request.Address;
import domain.request.CreateShipmentRequest;
import domain.request.Dimensions;
import domain.request.Parcel;
import domain.request.Receiver;
import domain.request.Weight;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import pl.aleh.domain.Label;
import pl.aleh.domain.delivery.DeliveryRequest;
import pl.aleh.domain.delivery.DeliveryResponse;
import pl.aleh.domain.delivery.Shipment;
import pl.aleh.domain.delivery.Status;
import pl.aleh.domain.tracking.TrackingDetails;
import pl.aleh.exception.CourierException;
import pl.aleh.util.Name;
import pl.aleh.util.NameUtils;

@Component
public class InpostPlRequestDeliveryTransformer {

  public static final String ITALY_CODE = "IT";
  public static final String ITALY_PHONE_NUMBER_REPLACE_REGEX = "[^0-9+]";
  private static final String TEMPLATE = "medium";
  private static final String SMS = "sms";
  private static final String EMAIL = "email";
  private static final String KG = "kg";
  private static final String MM = "mm";
  private static final String TARGET_POINT = "target_point";
  private static final String INPOST_LOCKER_STANDARD = "inpost_locker_standard";
  private static final String INPOST_COURIER_STANDARD = "inpost_courier_standard";
  private static final DefaultUriBuilderFactory DEFAULT_URI_BUILDER_FACTORY = new DefaultUriBuilderFactory();

  public CreateShipmentRequest transformToCreateShipmentRequest(
      final DeliveryRequest deliveryRequest, final String serviceName
  ) {
    var deliveryAddress = deliveryRequest.getDeliveryAddress().getAddress();
    var deliveryShipment = deliveryRequest.getShipment();

    var name = NameUtils.parsName(deliveryAddress.getName());

    Address address = null;
    Parcel parcel;
    Map<String, String> customAttributes = null;
    List<String> additionalServices = null;

    switch (serviceName) {
      case INPOST_COURIER_STANDARD:
        address = getAddress(deliveryAddress);
        parcel = getParcel(deliveryShipment);
        additionalServices = new ArrayList<>();
        if (StringUtils.isNotBlank(deliveryAddress.getPhoneNumber())) {
          additionalServices.add(SMS);
        }
        if (StringUtils.isNoneBlank(deliveryAddress.getEmailAddress())) {
          additionalServices.add(EMAIL);
        }
        break;
      case INPOST_LOCKER_STANDARD:
        parcel = Parcel.builder().template(TEMPLATE).build();
        customAttributes = Map.of(TARGET_POINT, getClickAndCollectLocationId(deliveryRequest.getDeliveryAddress()));
        break;
      default:
        throw new CourierException(ERROR_NO_DATA, String.format("Service %s is not supported", serviceName));
    }

    var receiver = getReceiver(deliveryAddress, name, address);

    return CreateShipmentRequest.builder()
        .receiver(receiver)
        .parcels(parcel)
        .service(serviceName)
        .customAttributes(customAttributes)
        .additionalServices(additionalServices)
        .build();
  }

  private Receiver getReceiver(
      final pl.aleh.domain.delivery.Address deliveryAddress, final Name name,
      final Address address
  ) {
    return Receiver.builder()
        .name(deliveryAddress.getName())
        .companyName(deliveryAddress.getCompanyName())
        .firstName(name.getFirstName())
        .lastName(name.getLastName())
        .email(deliveryAddress.getEmailAddress())
        .phone(getPhoneNumber(deliveryAddress))
        .address(address)
        .build();
  }

  private Parcel getParcel(final Shipment deliveryShipment) {
    return Parcel.builder()
        .dimensions(Dimensions.builder()
            .length(String.valueOf(deliveryShipment.getLength()))
            .width(String.valueOf(deliveryShipment.getWidth()))
            .height(String.valueOf(deliveryShipment.getHeight()))
            .unit(MM)
            .build())
        .weight(Weight.builder()
            .amount(String.valueOf(convertGToKg(deliveryShipment.getWeight())))
            .unit(KG)
            .build())
        .build();
  }

  private Address getAddress(final pl.aleh.domain.delivery.Address deliveryAddress) {
    return Address.builder()
        .street(deliveryAddress.getStreetName())
        .buildingNumber(deliveryAddress.getHouseNameNumber())
        .city(deliveryAddress.getCity())
        .postCode(deliveryAddress.getPostcode())
        .countryCode(deliveryAddress.getCountryCode())
        .build();
  }

  private BigDecimal convertGToKg(final int weight) {
    return BigDecimal.valueOf(weight).divide(BigDecimal.valueOf(1000L), 2, RoundingMode.HALF_UP);
  }

  public DeliveryResponse transformToDeliveryResponse(
      final String trackingNumber, final Label label, final String urlFormat
  ) {
    var trackingDetails = TrackingDetails.builder().trackingNumber(trackingNumber)
        .trackingUrl(DEFAULT_URI_BUILDER_FACTORY.expand(urlFormat, trackingNumber).toString())
        .build();
    return DeliveryResponse.builder().status(Status.SUCCESS).label(label).trackingDetails(trackingDetails).build();
  }

  private String getPhoneNumber(final pl.aleh.domain.delivery.Address deliveryAddress) {
    if (deliveryAddress.getCountryCode().equals(ITALY_CODE)) {
      return sanitizeItalyPhoneNumber(deliveryAddress.getPhoneNumber());
    } else {
      return right(deliveryAddress.getPhoneNumber(), 9);
    }
  }

  private String sanitizeItalyPhoneNumber(String number) {
    var numbersOnly = number.replaceAll(ITALY_PHONE_NUMBER_REPLACE_REGEX, EMPTY);
    return right(numbersOnly, 10);
  }

  public Label convertToLabel(final byte[] labelAsByteArray) {
    var base64Label = Base64.encodeBase64String(labelAsByteArray);
    return Label.builder().mediaType(MediaType.APPLICATION_PDF_VALUE).base64Label(base64Label).build();
  }
}
