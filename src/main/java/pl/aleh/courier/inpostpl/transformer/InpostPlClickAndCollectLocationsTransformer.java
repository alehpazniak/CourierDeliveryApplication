package pl.aleh.courier.inpostpl.transformer;

import static pl.aleh.domain.clickandcollect.ClickAndCollectLocationType.LOCKER;
import static pl.aleh.domain.clickandcollect.ClickAndCollectLocationType.SHOP;
import static pl.aleh.util.StringFormatUtils.trimJoin;

import domain.locations.AddressDetails;
import domain.locations.InpostPlLocationsQueryParams;
import domain.locations.Item;
import domain.locations.Response;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.aleh.domain.clickandcollect.BusinessHours;
import pl.aleh.domain.clickandcollect.ClickAndCollectLocation;
import pl.aleh.domain.clickandcollect.ClickAndCollectLocationsRequest;
import pl.aleh.domain.clickandcollect.ClickAndCollectLocationsResponse;
import pl.aleh.domain.delivery.Address;

@Component
@RequiredArgsConstructor
public class InpostPlClickAndCollectLocationsTransformer {

  public static final String MAX_DISTANCE = "10000";
  private static final List<String> PUDO_LOCKERS_TYPE_LIST = List.of("parcel_locker", "parcel_locker_only",
      "parcel_locker_superpop");
  private static final String STATUS = "Operating";

  public ClickAndCollectLocationsResponse transformToClickAndCollectLocationsResponse(
      final Response response,
      final InpostBusinessHoursParser businessHoursParser, final String countryCode
  ) {
    var locations = response.getItems().stream()
        .filter(item -> STATUS.equalsIgnoreCase(item.getStatus()))
        .map(item -> getLocation(item, businessHoursParser, countryCode))
        .collect(Collectors.toList());
    return ClickAndCollectLocationsResponse.builder()
        .clickAndCollectLocations(locations)
        .build();
  }

  public InpostPlLocationsQueryParams transformToQueryParams(final ClickAndCollectLocationsRequest request) {
    return InpostPlLocationsQueryParams.builder()
        .relativePostCode(request.getPostcode())
        .maxDistance(MAX_DISTANCE)
        .relativePoint(trimJoin(",", request.getLongitude().toString(), request.getLatitude().toString()))
        .build();
  }

  private ClickAndCollectLocation getLocation(
      final Item item, final InpostBusinessHoursParser businessHoursParser, final String countryCode
  ) {
    var addressDetails = item.getAddressDetails();
    return ClickAndCollectLocation.builder()
        .id(item.getName())
        .latitude(item.getLocation().getLatitude())
        .longitude(item.getLocation().getLongitude())
        .address(getAddress(addressDetails, countryCode))
        .distance(item.getDistance())
        .businessHours(getBusinessHours(item, businessHoursParser))
        .type(PUDO_LOCKERS_TYPE_LIST.contains(item.getType().get(0)) ? LOCKER : SHOP)
        .build();
  }

  private Address getAddress(final AddressDetails addressDetails, final String countryCode) {
    return Address.builder()
        .city(addressDetails.getCity())
        .countryCode(countryCode)
        .houseNameNumber(addressDetails.getBuildingNumber())
        .postcode(addressDetails.getPostCode())
        .state(addressDetails.getProvince())
        .streetName(addressDetails.getStreet())
        .build();
  }

  private List<BusinessHours> getBusinessHours(final Item item, final InpostBusinessHoursParser businessHoursParser) {
    return item.isLocation247() ? businessHoursParser.parseOpeningHours("24/7")
        : businessHoursParser.parseOpeningHours(item.getOpeningHours());
  }

}
