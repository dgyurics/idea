package idea.service.impl;

import javax.ws.rs.WebApplicationException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import idea.model.entity.User;
import idea.model.request.UserRequestModel;
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
  public User createNewUser(UserRequestModel user) throws WebApplicationException {
    validateUser(user);
    validateUserNotExist(user);

    User userEntity = new User();
    userEntity.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    userEntity.setUsername(user.getUsername());
    userEntity.setActive(true);
    userEntity.setRole("USER");
    return repository.save(userEntity);
  }

  private void validateUser(UserRequestModel user) throws WebApplicationException {
    try {
      Validate.notNull(user, "User was null");
      Validate.isTrue(StringUtils.isNotBlank(user.getUsername()), "Username cannot be empty");
      Validate.isTrue(StringUtils.isAlpha(user.getUsername()), "Username must be alphabetical");
      Validate.isTrue(StringUtils.isNotBlank(user.getPassword()), "Password cannot be empty");
      Validate.isTrue(StringUtils.isAsciiPrintable(user.getPassword()), "Password must be ascii printable");      
    } catch(IllegalArgumentException e) {
      throw new WebApplicationException(e.getMessage(), 400);
    }
  }

  private void validateUserNotExist(UserRequestModel user) throws WebApplicationException {
    try {
      Validate.isTrue(!repository.findByUsername(user.getUsername()).isPresent(), "User already exists");
    } catch(IllegalArgumentException e) {
      throw new WebApplicationException(e.getMessage(), 409);
    }    
  }
}
