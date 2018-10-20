package idea.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import idea.model.request.UserRequestModel;

@RestController
@RequestMapping("auth")
public class AuthController {
  @PostMapping("/login")
  public void login(@RequestBody UserRequestModel user) {
  }

  @PostMapping("/logout")
  public void logout(@RequestBody UserRequestModel user) {
  }

  @PostMapping("/register")
  public void register(@RequestBody UserRequestModel user) {
  }

  @PostMapping("/reset")
  public void forgotPassword(@RequestBody UserRequestModel user) {
  }
}
