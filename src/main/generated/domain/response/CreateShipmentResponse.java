
package domain.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.HashMap;
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
public class CreateShipmentResponse {

    private String href;
    private Integer id;
    private String status;
    @JsonProperty("tracking_number")
    private String trackingNumber;
    private String service;
    private String reference;
    @JsonProperty("is_return")
    private Boolean isReturn;
    @JsonProperty("application_id")
    private Integer applicationId;
    @JsonProperty("created_by_id")
    private Object createdById;
    @JsonProperty("external_customer_id")
    private String externalCustomerId;
    @JsonProperty("sending_method")
    private Object sendingMethod;
    private Mpk mpk;
    private String comments;
    @JsonProperty("additional_services")
    @Builder.Default
    private List<String> additionalServices = new ArrayList<>();
    @JsonProperty("custom_attributes")
    @Builder.Default
    private Map<String,String> customAttributes = new HashMap<>();
    private Cod cod;
    private Insurance insurance;
    private Sender sender;
    private Receiver receiver;
    @JsonProperty("selected_offer")
    private Object selectedOffer;
    @Builder.Default
    private List<Object> offers = new ArrayList<>();
    @Builder.Default
    private List<Object> transactions = new ArrayList<>();
    @Builder.Default
    private List<Parcel> parcels = new ArrayList<>();
    @JsonProperty("end_of_week_collection")
    private Boolean endOfWeekCollection;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;

}
