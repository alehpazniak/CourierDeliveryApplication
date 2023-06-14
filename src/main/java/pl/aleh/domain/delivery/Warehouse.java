package pl.aleh.domain.delivery;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Warehouse {

  private Address address;
  private int id;

}
