package idea.controller;

import idea.config.security.JwtTokenService;
import idea.config.security.RefreshTokenService;
import idea.config.security.UserService;
import idea.model.dto.UserRequestModel;
import idea.model.entity.Refresh;
import idea.model.entity.User;
import idea.model.validation.group.AuthenticateGroup;
import idea.model.validation.group.NewUserGroup;
import idea.model.validation.group.PasswordConfirmationGroup;
import idea.model.validation.group.PasswordRequestGroup;
import idea.model.validation.group.RemoveUserGroup;
import idea.model.validation.group.ResetCodeValidationGroup;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
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
@RequiredArgsConstructor
public class AuthController {
  private final UserService userService;
  private final JwtTokenService jwtService;
  private final RefreshTokenService refreshService;
  private final AuthenticationManager authenticationManager;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/register")
  public String register(@RequestBody @Validated(NewUserGroup.class) UserRequestModel user, HttpServletResponse res) {
    final User newUser = userService.createNewUser(user);
    refreshService.injectToken(newUser, res);
    return jwtService.generateToken(newUser);
  }

  @ResponseStatus(HttpStatus.OK)
  @PostMapping("/logout")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
  public void logout(HttpServletRequest req, HttpServletResponse res) {
    refreshService.expireToken(req, res);
    SecurityContextHolder.clearContext();
  }

  @ResponseStatus(HttpStatus.OK)
  @PostMapping("/login")
  public String login(@RequestBody @Validated(AuthenticateGroup.class) UserRequestModel user, HttpServletResponse res) {
    final Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
    refreshService.injectToken(authentication, res);
    return jwtService.generateToken(authentication);
  }

  @ResponseStatus(HttpStatus.CREATED)
  @GetMapping("/refresh")
  public String refresh(@CookieValue("refresh") String cookie, HttpServletRequest req, HttpServletResponse res) {
    final Refresh token = refreshService.getToken(UUID.fromString(cookie))
        .orElseThrow(() -> {
          refreshService.expireToken(req, res);
          return new AccessDeniedException("Invalid refresh token");
        });
    return jwtService.generateToken(token.getUser());
  }

  @DeleteMapping("/register")
  public void delete(@RequestBody @Validated(RemoveUserGroup.class) UserRequestModel user) {
    userService.deleteUser(user);
    SecurityContextHolder.clearContext();
  }

  @PostMapping("/forgot-password")
  public void forgotPassword(@RequestBody @Validated(PasswordRequestGroup.class) UserRequestModel user) {
    userService.requestResetPassword(user.getUsername());
  }

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
