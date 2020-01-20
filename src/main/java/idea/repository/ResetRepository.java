package idea.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import idea.model.entity.Reset;

public interface ResetRepository extends CrudRepository<Reset, Long> {
  @Query(value = "select * from auth_password_reset pr where pr.username = ?1 AND pr.valid = true", nativeQuery = true)
  Optional<Reset> findByUsername(String username);

  @Modifying
  @Query(value = "update auth_password_reset set valid = false where username = ?1", nativeQuery = true)
  void invalidateToken(String email);
}
