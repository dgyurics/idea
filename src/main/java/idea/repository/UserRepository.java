package idea.repository;

import org.springframework.data.repository.CrudRepository;
import idea.model.entity.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
  UserEntity findByUsername(String username);
}
