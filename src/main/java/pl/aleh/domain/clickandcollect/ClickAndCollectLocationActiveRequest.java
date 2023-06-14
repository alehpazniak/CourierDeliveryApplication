package pl.aleh.domain.clickandcollect;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.NonFinal;
import pl.aleh.domain.AccountSettings;

@Value
@NonFinal
@Builder(toBuilder = true)
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClickAndCollectLocationActiveRequest {

  AccountSettings accountSettings;
  int serviceId;
  String locationId;
  String postcode;
  String countryCode;
  String locality;

}
