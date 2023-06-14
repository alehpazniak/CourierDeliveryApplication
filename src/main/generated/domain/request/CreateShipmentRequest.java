
package domain.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
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
public class CreateShipmentRequest {

    private Receiver receiver;
    private Parcel parcels;
    private Insurance insurance;
    private Cod cod;
    private String service;
    @JsonProperty("additional_services")
    private List<String> additionalServices;
    private String reference;
    private String comments;
    @JsonProperty("external_customer_id")
    private String externalCustomerId;
    private String mpk;
    @JsonProperty("custom_attributes")
    private Map<String,String> customAttributes;

}
