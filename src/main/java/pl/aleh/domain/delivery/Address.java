package pl.aleh.domain.delivery;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table
public class Address {

  @Id
  private long addressId;
  private String name;
  private String companyName;
  private String houseNameNumber;
  private String streetName;
  private String line2;
  private String city;
  private String state;
  private String postcode;
  private String countryCode;
  private String emailAddress;
  private String phoneNumber;

}
