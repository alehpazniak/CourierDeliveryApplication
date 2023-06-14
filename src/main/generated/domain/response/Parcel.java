
package domain.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Parcel {

    private Integer id;
    @JsonProperty("tracking_number")
    private Object trackingNumber;
    @JsonProperty("is_non_standard")
    private Boolean isNonStandard;
    private Object template;
    private Dimensions dimensions;
    private Weight weight;

}
