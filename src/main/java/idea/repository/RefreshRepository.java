package idea.repository;

import idea.model.entity.Refresh;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface RefreshRepository extends CrudRepository<Refresh, UUID> {
  Optional<Refresh> findByUserUsername(String username);
  void deleteByUserId(Long userId);
}
