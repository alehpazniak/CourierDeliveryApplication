package pl.aleh.facade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CourierServiceRepository extends JpaRepository<CourierServiceModel, Integer> {

  @Query(value = "insert into courier_service_model values (serviceId, className)",
      nativeQuery = true)
  void createNewService(Integer serviceId, String className);

}
