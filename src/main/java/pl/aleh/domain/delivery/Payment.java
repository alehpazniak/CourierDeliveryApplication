package pl.aleh.domain.delivery;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class Payment {

  private Address billingAddress;
  private String payMethod;
  private BigDecimal payAmount;
  private String paymentPayId;
  private String payId;
  private String paidAt;

}
