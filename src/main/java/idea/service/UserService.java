package idea.service;

import java.util.Collection;
import idea.model.request.UserRequestModel;

public interface UserService {
  UserRequestModel save(UserRequestModel user);
  Boolean delete(long id);
  UserRequestModel update(UserRequestModel user);
  UserRequestModel findById(long id);
  UserRequestModel findByUsername(String username);
  Collection<UserRequestModel> findAll();
}
