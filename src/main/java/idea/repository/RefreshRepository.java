package idea.repository;

import idea.model.entity.Refresh;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RefreshRepository extends CrudRepository<Refresh, UUID> {
  Optional<Refresh> findByUserUsername(String username);
  void deleteByUserId(Long userId);

  // does not throw exception when not found, unlike default deleteById
  @Modifying
  @Query(nativeQuery = true, value="delete from auth_refresh_token where id = ?1")
  void deleteById(UUID id);
}
