package idea.service.impl;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import javax.ws.rs.WebApplicationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import idea.model.entity.Reset;
import idea.model.entity.User;
import idea.model.request.UserRequestModel;
import idea.repository.ResetRepository;
import idea.repository.UserRepository;
import idea.service.EmailService;
import idea.service.UserService;
import idea.utility.Validate;

@Component
public class UserServiceImpl implements UserService {
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final EmailService emailService;
  private final UserRepository userRepository;
  private final ResetRepository resetRepository;

  UserServiceImpl(UserRepository repository, EmailService emailService, ResetRepository resetRepository) {
    this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    this.emailService = emailService;
    this.userRepository = repository;
    this.resetRepository = resetRepository;
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
    return userRepository.save(userEntity);
  }

  @Transactional
  @Override
  public void deleteUser(UserRequestModel user) throws WebApplicationException {
    Long userId = validateDeleteRequest(user);
    userRepository.deleteById(userId);
  }

  @Transactional
  @Override
  public void requestResetPassword(UserRequestModel user) {
    Optional<User> userDb = userRepository.findByEmail(user.getEmail());
    validateResetCodeNotExist(user.getEmail());
    validateEmail(userDb);
    final int resetCode = generateResetCode();
    resetRepository.save(new Reset(user.getEmail(), resetCode, true));
    emailResetCode(user.getEmail(), userDb.get().getId().toString(), resetCode);
  }

  @Transactional
  @Override
  public void resetPassword(long userId, UserRequestModel user) {
    // TODO: invalidate after after 15 minutes has elapsed
    final Optional<User> userToUpdate = userRepository.findById(userId);
    validateUser(userToUpdate);
    validateResetCode(userToUpdate.get().getEmail(), user.getResetCode());
    updateUser(userToUpdate.get(), user.getPassword());
  }

  private void updateUser(User existingUser, String newPassword) {
    existingUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
    userRepository.save(existingUser);
    resetRepository.invalidateToken(existingUser.getEmail());
  }

  private void emailResetCode(String toAddress, String userId, int code) {
    emailService.sendResetCode(toAddress, userId, code);
  }

  private int generateResetCode() {
    return ThreadLocalRandom.current().nextInt(100000, 999999 + 1);
  }

  private void validateUserNotExist(UserRequestModel user) {
    Validate.isTrue(!userRepository.findByUsername(user.getUsername()).isPresent(), "User with that username already exists", 409);
    if (StringUtils.isNotEmpty(user.getEmail()))
      Validate.isTrue(!userRepository.findByEmail(user.getEmail()).isPresent(), "User with that email already exists", 409);
  }

  private Long validateDeleteRequest(UserRequestModel registrationRequest) throws WebApplicationException {
    Optional<User> user = userRepository.findByUsername(registrationRequest.getUsername());
    Validate.isTrue(user.isPresent(), "User does not exist", 400);
    Validate.isTrue(bCryptPasswordEncoder.matches(registrationRequest.getPassword(), user.get().getPassword()), "Passwords did not match", 400);
    return user.get().getId();
  }

  private void validateUser(Optional<User> user) {
    Validate.isTrue(user.isPresent(), "Invalid userId", 400);
    Validate.isNotNull(user.get().getEmail(), "Email for user is empty", 400);
  }

  private void validateResetCodeNotExist(String email) {
    final Optional<Reset> reset = resetRepository.findByEmail(email);
    Validate.isFalse(reset.isPresent(), "Reset code already exists", 409);
  }

  private void validateResetCode(String email, int resetCode) {
    final Optional<Reset> reset = resetRepository.findByEmail(email);
    Validate.isTrue(reset.isPresent(), "Reset code expired", 400);
    Validate.isTrue(reset.get().getResetCode() == resetCode, "Invalid reset code", 400);
  }

  private void validateEmail(Optional<User> user) {
    Validate.isTrue(user.isPresent(), "Email not found", 400);
  }
}
