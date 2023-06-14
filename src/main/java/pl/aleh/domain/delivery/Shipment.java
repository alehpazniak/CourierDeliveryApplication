package pl.aleh.domain.delivery;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class Shipment {

  private String shipmentId;
  private Integer height;
  private Integer length;
  private Integer width;
  private int weight;
  private int packagingWeight;
  private int totalWeightAllocated;
  private String currency;
  private BigDecimal value;
  private BigDecimal shippingCost;
  private BigDecimal shippingTax;
  private IncoTerms incoTerms;
  private String contentDescription;
  private String palletId;
  private List<Integer> deliveryTypeIds;
  private List<Product> products;


}
