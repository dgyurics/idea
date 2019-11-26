package idea.service.impl;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import javax.ws.rs.WebApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import idea.model.entity.Reset;
import idea.model.entity.User;
import idea.model.request.UserRequestModel;
import idea.repository.ResetRepository;
import idea.repository.UserRepository;
import idea.service.EmailService;
import idea.service.UserService;
import idea.utility.Validate;

@Service
public class UserServiceImpl implements UserService {
  private Logger logger = LoggerFactory.getLogger(UserService.class);
  private final PasswordEncoder passwordEncoder;
  private final EmailService emailService;
  private final UserRepository userRepository;
  private final ResetRepository resetRepository;

  UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository repository,
      EmailService emailService, ResetRepository resetRepository) {
    this.passwordEncoder = passwordEncoder;
    this.emailService = emailService;
    this.userRepository = repository;
    this.resetRepository = resetRepository;
  }

  @Override
  public User createNewUser(UserRequestModel user) throws WebApplicationException {
    validateUserNotExist(user);
    // validateRegistrationCode(user.getRegistrationCode());
    User userEntity = new User();
    userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
    userEntity.setUsername(user.getUsername());
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

  @Async
  @Transactional
  @Override
  public void requestResetPassword(final String username) {
    logger.info("New reset password request received for user {}", username);
    final Optional<User> user = userRepository.findByUsername(username);
    if(!user.isPresent()) {
      logger.error("User does not exist {}", username);
      return;
    }
    final int resetCode = generateResetCode();
    final Reset result  = resetRepository.save(
        Reset.builder()
            .resetCode(resetCode)
            .valid(true)
            .username(user.get().getUsername()).build());
    emailResetCode(username, user.get().getId().toString(), resetCode);
  }

  @Transactional
  @Override
  public void validateResetCode(long userId, UserRequestModel user) {
    final Optional<User> userToUpdate = userRepository.findById(userId);
    Validate.isTrue(userToUpdate.isPresent(), "Invalid user ID", 400);
    validateResetCode(userToUpdate.get().getUsername(), user.getResetCode());
  }

  @Transactional
  @Override
  public void resetPassword(long userId, UserRequestModel user) {
    // TODO: invalidate after after 15 minutes has elapsed
    final Optional<User> userToUpdate = userRepository.findById(userId);
    Validate.isTrue(userToUpdate.isPresent(), "Invalid user ID", 400);
    validateResetCode(userToUpdate.get().getUsername(), user.getResetCode());
    updateUser(userToUpdate.get(), user.getPassword());
  }

  private void updateUser(User existingUser, String newPassword) {
    existingUser.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(existingUser);
    resetRepository.invalidateToken(existingUser.getUsername());
  }

  private void emailResetCode(String toAddress, String userId, int code) {
    emailService.sendResetCode(toAddress, userId, code);
  }

  private int generateResetCode() {
    return ThreadLocalRandom.current().nextInt(100000, 999999 + 1);
  }

  private void validateUserNotExist(UserRequestModel user) {
    Validate.isTrue(!userRepository.findByUsername(user.getUsername()).isPresent(),
        "User with that username already exists", 409);
  }

  private void validateRegistrationCode(int registrationCode) {
    // TODO create tool to generate unique codes and validate per user
    Validate.isTrue(123456 == registrationCode,"Invalid registration code", 401);
  }

  private Long validateDeleteRequest(UserRequestModel registrationRequest)
      throws WebApplicationException {
    Optional<User> user = userRepository.findByUsername(registrationRequest.getUsername());
    Validate.isTrue(user.isPresent(), "User does not exist", 400);
    Validate.isTrue(
        passwordEncoder.matches(registrationRequest.getPassword(), user.get().getPassword()),
        "Passwords did not match", 400);
    return user.get().getId();
  }

  private void validateResetCode(String email, int resetCode) {
    final Optional<Reset> reset = resetRepository.findByUsername(email);
    Validate.isTrue(reset.isPresent(), "Reset code expired", 400);
    Validate.isTrue(reset.get().getResetCode() == resetCode, "Invalid reset code", 400);
  }
}
