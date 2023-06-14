package pl.aleh.domain.delivery;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import lombok.Data;

@Entity
@Data
public class Order {

  @Id
  private String orderId;
  private String currencyCode;
  private String clientName;
  private BigDecimal value;
  private String citizenID;

}
