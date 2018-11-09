package idea.service.impl;

import javax.ws.rs.WebApplicationException;
import org.apache.commons.lang3.Validate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import idea.model.entity.User;
import idea.model.request.RegistrationRequestModel;
import idea.repository.UserRepository;
import idea.service.UserService;

@Component
public class UserServiceImpl implements UserService {

  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final UserRepository repository;

  UserServiceImpl(UserRepository repository) {
    this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    this.repository = repository;
  }

  @Override
  public User createNewUser(RegistrationRequestModel user) throws WebApplicationException {
    validateUserNotExist(user);
    User userEntity = new User();
    userEntity.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    userEntity.setUsername(user.getUsername());
    userEntity.setActive(true);
    userEntity.setRole("USER");
    return repository.save(userEntity);
  }

  private void validateUserNotExist(RegistrationRequestModel user) throws WebApplicationException {
    try {
      Validate.isTrue(!repository.findByUsername(user.getUsername()).isPresent(), "User already exists");
    } catch(IllegalArgumentException e) {
      throw new WebApplicationException(e.getMessage(), 409);
    }    
  }
}
