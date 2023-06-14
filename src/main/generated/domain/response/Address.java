
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
public class Address {

    private Integer id;
    private String street;
    @JsonProperty("building_number")
    private String buildingNumber;
    private Object line1;
    private Object line2;
    private String city;
    @JsonProperty("post_code")
    private String postCode;
    @JsonProperty("country_code")
    private String countryCode;

}
