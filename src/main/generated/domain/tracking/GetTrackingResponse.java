
package domain.tracking;

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
public class GetTrackingResponse {

    @JsonProperty("tracking_number")
    private String trackingNumber;
    private String type;
    private String service;
    private String status;
    @JsonProperty("custom_attributes")
    private CustomAttributes customAttributes;
    @JsonProperty("tracking_details")
    private List<TrackingDetail> trackingDetails;
    @JsonProperty("expected_flow")
    private List<Object> expectedFlow;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;

}
