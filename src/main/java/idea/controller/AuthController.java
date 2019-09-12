package idea.controller;

import idea.model.validation.group.ResetCodeValidationGroup;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import idea.model.request.UserRequestModel;
import idea.model.validation.group.NewUserGroup;
import idea.model.validation.group.PasswordConfirmationGroup;
import idea.model.validation.group.PasswordRequestGroup;
import idea.model.validation.group.RemoveUserGroup;
import idea.service.UserService;

@RestController
@RequestMapping
public class AuthController {
  private final UserService service;

  AuthController(UserService service) {
    this.service = service;
  }

  @PostMapping("/register")
  public void register(@RequestBody @Validated(NewUserGroup.class) UserRequestModel user) {
    service.createNewUser(user);
  }

  @DeleteMapping("/register")
  public void delete(@RequestBody @Validated(RemoveUserGroup.class) UserRequestModel user) {
    service.deleteUser(user);
  }

  @PostMapping("/forgot-password")
  public void forgotPassword(@RequestBody @Validated(PasswordRequestGroup.class) UserRequestModel user) {
    service.requestResetPassword(user.getUsername());
  }

  @PostMapping("/forgot-password/valid-reset-code/{userId}")
  public void isValidResetCode(
      @RequestBody @Validated(ResetCodeValidationGroup.class) UserRequestModel user,
      @PathVariable long userId) {
    service.validateResetCode(userId, user);
  }

  @PostMapping("/forgot-password/{userId}")
  public void resetPassword(
      @RequestBody @Validated(PasswordConfirmationGroup.class) UserRequestModel user,
      @PathVariable long userId) {
    service.resetPassword(userId, user);
  }
}
