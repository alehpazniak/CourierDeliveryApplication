package domain.locations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
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
public class Item {

    private String href;
    private String name;
    private List<String> type;
    private String status;
    private Location location;
    @JsonProperty("location_type")
    private String locationType;
    @JsonProperty("location_date")
    private String locationDate;
    @JsonProperty("location_description")
    private String locationDescription;
    @JsonProperty("location_description_1")
    private String locationDescription1;
    @JsonProperty("location_description_2")
    private String locationDescription2;
    private double distance;
    @JsonProperty("opening_hours")
    private String openingHours;
    private Address address;
    @JsonProperty("address_details")
    private AddressDetails addressDetails;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("payment_point_descr")
    private String paymentPointDescr;
    private List<String> functions;
    @JsonProperty("partner_id")
    private Integer partnerId;
    @JsonProperty("is_next")
    private Boolean isNext;
    @JsonProperty("payment_available")
    private Boolean paymentAvailable;
    @JsonProperty("payment_type")
    private PaymentType paymentType;
    private String virtual;
    @JsonProperty("recommended_low_interest_box_machines_list")
    private String recommendedLowInterestBoxMachinesList;
    @JsonProperty("location_247")
    private boolean location247;

}
