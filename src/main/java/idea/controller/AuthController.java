package idea.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import idea.model.request.UserRequestModel;

@RestController
public class AuthController {

  private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

  @PostMapping("/login")
  public void login(@RequestBody UserRequestModel user) {
    logger.info(user.toString());
  }

  @PostMapping("/logout")
  public void logout(@RequestBody UserRequestModel user) {
    logger.info(user.toString());
  }

  @PostMapping("/register")
  public void register(@RequestBody UserRequestModel user) {
    logger.info(user.toString());
  }
}
