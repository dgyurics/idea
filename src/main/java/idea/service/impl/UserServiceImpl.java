package idea.service.impl;

import java.util.Optional;
import javax.ws.rs.WebApplicationException;
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
    validateUserNotExist(user);
    User userEntity = new User();
    userEntity.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    userEntity.setUsername(user.getUsername());
    userEntity.setEmail(user.getEmail());
    userEntity.setActive(true);
    userEntity.setRole("USER");
    return repository.save(userEntity);
  }

  @Override
  public void deleteUser(UserRequestModel user) throws WebApplicationException {
    Long userId = validateDeleteRequest(user);
    repository.deleteById(userId);
  }

  private void validateUserNotExist(UserRequestModel user) throws WebApplicationException {
    try {
      Validate.isTrue(!repository.findByUsername(user.getUsername()).isPresent(), "User with that username already exists");
      Validate.isTrue(!repository.findByEmail(user.getEmail()).isPresent(), "User with that email already exists");
    } catch(IllegalArgumentException e) {
      throw new WebApplicationException(e.getMessage(), 409);
    }
  }

  private Long validateDeleteRequest(UserRequestModel registrationRequest) throws WebApplicationException {
    try {
      Optional<User> user = repository.findByUsername(registrationRequest.getUsername());
      Validate.isTrue(user.isPresent(), "User does not exist");
      Validate.isTrue(bCryptPasswordEncoder.matches(registrationRequest.getPassword(), user.get().getPassword()), "Passwords did not match");
      return user.get().getId();
    } catch(IllegalArgumentException e) {
      throw new WebApplicationException(e.getMessage(), 403);
    }    
  }

  @Override
  public void requestPassword(UserRequestModel user) {
    // TODO
    // validate reset code
    // update password using supplied email
  }

  @Override
  public void resetPassword(UserRequestModel user) {
    // TOOD
    // validate email exists
    // generate a unique code for this email, save it in redis with a time to live (TTL)
    // email user their username and reset code
  }
}
