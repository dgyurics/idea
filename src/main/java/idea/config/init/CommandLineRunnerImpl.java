package idea.config.init;

import idea.model.entity.User;
import idea.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * The run method is executed at application startup
 */
@Component
public class CommandLineRunnerImpl implements CommandLineRunner {
  private final Logger logger = LoggerFactory.getLogger(CommandLineRunnerImpl.class);
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  CommandLineRunnerImpl(PasswordEncoder passwordEncoder, UserRepository userRepository) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  @Override
  public void run(String... args) throws Exception {
    logger.info("CommandLineRunner run method executed");
    if(args.length == 2) createAdminUser(args[0], args[1]);
  }

  private void createAdminUser(String email, String password) {
    logger.info("CommandLineRunner creating ADMIN user {}", email);
    userRepository.findByUsername(email).ifPresentOrElse((user) -> {
      logger.info("CommandLineRunner user {} already exists", email);
    }, () -> {
      User userEntity = new User();
      userEntity.setPassword(passwordEncoder.encode(password));
      userEntity.setUsername(email);
      userEntity.setActive(true);
      userEntity.setRole("ADMIN");
      userRepository.save(userEntity);
    });
  }
}
