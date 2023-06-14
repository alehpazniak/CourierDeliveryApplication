package pl.aleh.domain.clickandcollect;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@NonFinal
@Builder(toBuilder = true)
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusinessHours {

  int dayOfWeek;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  LocalTime openTime;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  LocalTime closeTime;
}
