package idea.controller;

import idea.config.security.JwtTokenService;
import idea.config.security.UserService;
import idea.model.dto.UserRequestModel;
import idea.model.validation.group.AuthenticateGroup;
import idea.model.validation.group.NewUserGroup;
import idea.model.validation.group.PasswordConfirmationGroup;
import idea.model.validation.group.PasswordRequestGroup;
import idea.model.validation.group.RemoveUserGroup;
import idea.model.validation.group.ResetCodeValidationGroup;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class AuthController {
  private final UserService userService;
  private final JwtTokenService jwtService;
  private final AuthenticationManager authenticationManager;

  AuthController(UserService service, JwtTokenService jwtService, AuthenticationManager authenticationManager) {
    this.userService = service;
    this.jwtService = jwtService;
    this.authenticationManager = authenticationManager;
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/register")
  public String register(@RequestBody @Validated(NewUserGroup.class) UserRequestModel user) {
    return jwtService.generateToken(userService.createNewUser(user));
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/refresh-token/{tokenId}")
  public String getJwtToken(@PathVariable UUID tokenId) {
    return jwtService.generateToken(tokenId);
  }

  @ResponseStatus(HttpStatus.CREATED)
  @GetMapping("/refresh-token")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
  public UUID getRefreshToken(HttpServletRequest req) {
    return jwtService.generateRefreshToken(req.getRemoteUser());
  }

  @ResponseStatus(HttpStatus.OK)
  @PostMapping("/logout")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
  public void logout(HttpServletRequest req) {
    jwtService.invalidateRefreshToken(req.getRemoteUser());
  }

  @ResponseStatus(HttpStatus.OK)
  @PostMapping("/login")
  public String login(@RequestBody @Validated(AuthenticateGroup.class) UserRequestModel user) {
    final Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
    return jwtService.generateToken(authentication);
  }

  // FIXME: Use security annotations to validate request permissions
  @DeleteMapping("/register")
  public void delete(@RequestBody @Validated(RemoveUserGroup.class) UserRequestModel user) {
    userService.deleteUser(user);
    SecurityContextHolder.clearContext();
  }

  @PostMapping("/forgot-password")
  public void forgotPassword(@RequestBody @Validated(PasswordRequestGroup.class) UserRequestModel user) {
    userService.requestResetPassword(user.getUsername());
  }

  // FIXME: Use security annotations to validate request permissions
  @PostMapping("/forgot-password/valid-reset-code/{userId}")
  public void isValidResetCode(
      @RequestBody @Validated(ResetCodeValidationGroup.class) UserRequestModel user,
      @PathVariable long userId) {
    userService.validateResetCode(userId, user);
    SecurityContextHolder.clearContext();
  }

  @PostMapping("/forgot-password/{userId}")
  public void resetPassword(
      @RequestBody @Validated(PasswordConfirmationGroup.class) UserRequestModel user,
      @PathVariable long userId) {
    userService.resetPassword(userId, user);
  }

  @GetMapping("/whoami")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
  public String secureEndpoint(HttpServletRequest req) {
    return req.getRemoteUser();
  }
}
