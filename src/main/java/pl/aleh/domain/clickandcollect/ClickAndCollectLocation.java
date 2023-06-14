package pl.aleh.domain.clickandcollect;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.NonFinal;
import pl.aleh.domain.delivery.Address;

@Value
@NonFinal
@Builder(toBuilder = true)
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClickAndCollectLocation {

  String id;
  Double latitude;
  Double longitude;
  Address address;
  Double distance;
  List<BusinessHours> businessHours;
  List<Holiday> holidays;
  ClickAndCollectLocationType type;
}
