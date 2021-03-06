package idea.config.security;

import javax.ws.rs.WebApplicationException;
import idea.model.entity.User;
import idea.model.dto.UserRequestModel;

public interface UserService {
  User createNewUser(UserRequestModel user) throws WebApplicationException;
  void deleteUser(UserRequestModel user) throws WebApplicationException;
  void requestResetPassword(String email);
  void validateResetCode(long userId, UserRequestModel user);
  void resetPassword(long userId, UserRequestModel user);
}
