package pl.aleh.domain.delivery;

import lombok.Builder;
import lombok.Data;
import pl.aleh.domain.Label;
import pl.aleh.domain.tracking.TrackingDetails;

@Builder
@Data
public class DeliveryResponse {

  private Status status;
  private String failureReason;
  private TrackingDetails trackingDetails;
  private Label label;


}
