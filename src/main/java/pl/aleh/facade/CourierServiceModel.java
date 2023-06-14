package pl.aleh.facade;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "CourierServiceModel")
public class CourierServiceModel {

  @Id
  private Integer serviceId;
  private String className;

}
