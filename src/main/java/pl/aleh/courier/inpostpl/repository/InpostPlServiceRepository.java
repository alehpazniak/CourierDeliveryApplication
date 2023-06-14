package pl.aleh.courier.inpostpl.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pl.aleh.courier.inpostpl.repository.model.InpostPlServiceMapping;
@Repository
public interface InpostPlServiceRepository extends CrudRepository<InpostPlServiceMapping, Integer> {

  @Override
//  @NotNull
  @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
  Optional<InpostPlServiceMapping> findById(Integer internalId);

}
