package idea.service;

import javax.ws.rs.WebApplicationException;
import idea.model.entity.User;
import idea.model.request.RegistrationRequestModel;

public interface UserService {
  User createNewUser(RegistrationRequestModel user) throws WebApplicationException;
  void deleteUser(RegistrationRequestModel user) throws WebApplicationException;
}
