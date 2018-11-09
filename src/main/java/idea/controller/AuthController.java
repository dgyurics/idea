package idea.controller;

import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import idea.model.request.RegistrationRequestModel;
import idea.model.request.UserRequestModel;
import idea.service.UserService;

@RestController
@RequestMapping
public class AuthController {
  private final UserService service;
  
  AuthController(UserService service) {
    this.service = service;
  }

  @PostMapping("/register")
  public void register(@RequestBody @Valid RegistrationRequestModel user) {
    service.createNewUser(user);
  }

  @PostMapping("/forgot-password")
  public void forgotPassword(@RequestBody UserRequestModel user) {
  }
}
