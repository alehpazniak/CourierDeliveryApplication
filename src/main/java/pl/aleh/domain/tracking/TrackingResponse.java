package pl.aleh.domain.tracking;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@NonFinal
@Builder(toBuilder = true)
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackingResponse {

  Map<String, TrackingData> trackingDataMap;
  @JsonIgnore
  @Deprecated(since = "1.9.8", forRemoval = true)
  public Set<String> getTrackingNumbers() {
    return trackingDataMap.keySet();
  }

  @JsonIgnore
  @Deprecated(since = "1.9.8", forRemoval = true)
  public Optional<TrackingData> getTrackingData(String trackingNumber) {
    return Optional.ofNullable(trackingDataMap.get(trackingNumber));
  }

}
