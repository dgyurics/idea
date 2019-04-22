package idea.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import idea.model.entity.Reset;

public interface ResetRepository extends CrudRepository<Reset, Long> {
  @Query(value = "select * from password_reset pr where pr.email = ?1 AND pr.valid = true", nativeQuery = true)
  Optional<Reset> findByEmail(String email);

  @Modifying
  @Query(value = "update password_reset pr set pr.valid = false where pr.email = ?1", nativeQuery = true)
  void invalidateToken(String email);
}
