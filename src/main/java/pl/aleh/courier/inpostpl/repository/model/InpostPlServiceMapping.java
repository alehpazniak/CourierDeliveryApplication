package pl.aleh.courier.inpostpl.repository.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table
public class InpostPlServiceMapping {

  @Id
  private Integer internalServiceId;
  private String externalServiceName;

}
