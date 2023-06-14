package pl.aleh.domain.delivery;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class Product {

  private int productId;
  private String brand;
  private String partNumber;
  private String category;
  private String description;
  private int quantity;
  private BigDecimal cost;
  private BigDecimal unitTax;
  private double unitWeight;
  private String countryOfOrigin;


}
