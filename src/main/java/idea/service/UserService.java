package idea.service;

import java.util.Collection;
import idea.model.entity.UserEntity;
import idea.model.request.UserRequestModel;

public interface UserService {
  UserEntity createNewUser(UserRequestModel user);
  void resetPassword(long id);
  void delete(long id);
  UserEntity findById(long id);
  UserEntity findByUsername(String username);
  Collection<UserEntity> findAll();
}
